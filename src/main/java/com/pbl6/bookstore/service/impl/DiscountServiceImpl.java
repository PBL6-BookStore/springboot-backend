package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.domain.entity.DiscountEntity;
import com.pbl6.bookstore.domain.repository.jpa.DiscountRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.exception.ValidateException;
import com.pbl6.bookstore.payload.request.AddDiscountRequest;
import com.pbl6.bookstore.payload.request.GetAllDiscountRequest;
import com.pbl6.bookstore.payload.response.*;
import com.pbl6.bookstore.payload.response.discount.DiscountDTO;
import com.pbl6.bookstore.service.DiscountService;
import com.pbl6.bookstore.service.converter.DiscountMapper;
import com.pbl6.bookstore.util.DateTimeUtils;
import com.pbl6.bookstore.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 12/11/2022
 */

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final SecurityUtils securityUtils;

    @Override
    public Response<ListDTO<DiscountDTO>> getAllDiscount(GetAllDiscountRequest request) {
        var discounts = discountRepository.findAllDiscount(Boolean.TRUE.equals(request.getIsActive()));
        if (!securityUtils.getPrincipal().isAdmin()){
            DiscountEntity discount = null;
            if (StringUtils.hasText(request.getCode())){
                discount = discounts.stream().filter(d -> d.getCode().equals(request.getCode())).findFirst().orElse(null);
            } else {
                return Response.<ListDTO<DiscountDTO>>newBuilder()
                        .setSuccess(false)
                        .setErrorCode(ErrorCode.REQUIRED_FIELD_MISSING)
                        .setErrors(List.of(ErrorDTO.of("code", ErrorCode.REQUIRED_FIELD_MISSING)))
                        .setMessage("Bad request")
                        .build();
            }
            if (discount == null){
                return Response.<ListDTO<DiscountDTO>>newBuilder()
                        .setSuccess(false)
                        .setErrorCode(ErrorCode.NOT_FOUND)
                        .setErrors(List.of(ErrorDTO.of("code", ErrorCode.NOT_FOUND)))
                        .setMessage("Discount not found")
                        .build();
            }
            return Response.<ListDTO<DiscountDTO>>newBuilder()
                    .setSuccess(true)
                    .setData(ListDTO.<DiscountDTO>newBuilder()
                            .setTotalElements((long) discounts.size())
                            .setItems(List.of(DiscountMapper.map(discount)))
                            .build())
                    .build();

        }
        return Response.<ListDTO<DiscountDTO>>newBuilder()
                .setSuccess(true)
                .setData(ListDTO.<DiscountDTO>newBuilder()
                        .setTotalElements((long) discounts.size())
                        .setItems(discounts.stream()
                                .map(DiscountMapper::map)
                                .collect(Collectors.toUnmodifiableList()))
                        .build())
                .build();
    }

    @Override
    public Response<DiscountDTO> getDiscountById(Long discountId) {
        var discount = discountRepository.findById(discountId).orElseThrow(
                () -> new ObjectNotFoundException("discountId", discountId)
        );

        return Response.<DiscountDTO>newBuilder()
                .setSuccess(true)
                .setData(DiscountMapper.map(discount))
                .build();
    }

    @Override
    public Response<OnlyIdDTO> addDiscount(AddDiscountRequest request) {
        List<ErrorDTO> errors = new ArrayList<>();
        validate(request, errors);
        if (!errors.isEmpty()){
            throw new ValidateException(errors);
        }

        DiscountEntity discount = new DiscountEntity();
        setPropertiesDiscount(request, discount);
        var discountSaved = discountRepository.save(discount);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(discountSaved.getId())
                        .build())
                .build();
    }

    @Override
    public Response<OnlyIdDTO> updateDiscount(AddDiscountRequest request, Long discountId) {
        List<ErrorDTO> errors = new ArrayList<>();
        validate(request, errors);
        if (!errors.isEmpty()){
            throw new ValidateException(errors);
        }
        var discount = discountRepository.findById(discountId).orElseThrow(
                () -> new ObjectNotFoundException("discountId", discountId)
        );
        setPropertiesDiscount(request, discount);
        var discountSave = discountRepository.save(discount);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(discountSave.getId())
                        .build())
                .build();

    }

    @Override
    public Response<NoContentResponse> deleteDiscount(Long discountId) {
        var discount = discountRepository.findById(discountId).orElseThrow(
                () -> new ObjectNotFoundException("discountId", discountId)
        );
        discountRepository.delete(discount);
        return Response.<NoContentResponse>newBuilder()
                .setSuccess(true)
                .setData(NoContentResponse.builder()
                        .build())
                .build();
    }

    private void setPropertiesDiscount(AddDiscountRequest request, DiscountEntity discount) {
        discount.setCode(request.getCode());
        discount.setDescription(request.getDescription());
        discount.setName(request.getName());
        discount.setValue(request.getValue());
        try {
            discount.setStartDate(DateTimeUtils.string2Timestamp(request.getStartDate()));
            discount.setEndDate(DateTimeUtils.string2Timestamp(request.getEndDate()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(AddDiscountRequest request, List<ErrorDTO> errors) {
        if (request.getCode() == null){
            errors.add(ErrorDTO.of("code", ErrorCode.REQUIRED_FIELD_MISSING));
        } else if (!StringUtils.hasText(request.getCode())){
            errors.add(ErrorDTO.of("code", ErrorCode.NOT_EMPTY));
        }

        if (request.getName() == null){
            errors.add(ErrorDTO.of("name", ErrorCode.REQUIRED_FIELD_MISSING));
        } else if (!StringUtils.hasText(request.getName())){
            errors.add(ErrorDTO.of("name", ErrorCode.NOT_EMPTY));
        }

        if (request.getValue() == null){
            errors.add(ErrorDTO.of("value", ErrorCode.REQUIRED_FIELD_MISSING));
        }

        if (request.getValue() <= 0 || request.getValue() > 100){
            errors.add(ErrorDTO.of("value", ErrorCode.INVALID_VALUE));
        }

        if (request.getDescription() == null){
            errors.add(ErrorDTO.of("description", ErrorCode.REQUIRED_FIELD_MISSING));
        } else if (!StringUtils.hasText(request.getDescription())){
            errors.add(ErrorDTO.of("description", ErrorCode.NOT_EMPTY));
        }

        if (request.getStartDate() == null){
            errors.add(ErrorDTO.of("startDate", ErrorCode.REQUIRED_FIELD_MISSING));
        } else if (!StringUtils.hasText(request.getStartDate())){
            errors.add(ErrorDTO.of("startDate", ErrorCode.NOT_EMPTY));
        } else {
            try {
                DateTimeUtils.string2Timestamp(request.getStartDate());
            } catch (ParseException e) {
                errors.add(ErrorDTO.of("startDate", ErrorCode.DATE_FORMAT_INVALID));
            }
        }

        if (request.getEndDate() == null){
            errors.add(ErrorDTO.of("endDate", ErrorCode.REQUIRED_FIELD_MISSING));
        } else if (!StringUtils.hasText(request.getEndDate())){
            errors.add(ErrorDTO.of("endDate", ErrorCode.NOT_EMPTY));
        } else {
            try {
                DateTimeUtils.string2Timestamp(request.getEndDate());
            } catch (ParseException e) {
                errors.add(ErrorDTO.of("endDate", ErrorCode.DATE_FORMAT_INVALID));
            }
        }
    }
}
