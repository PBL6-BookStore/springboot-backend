package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.domain.entity.CartDetailEntity;
import com.pbl6.bookstore.domain.repository.jpa.BookRepository;
import com.pbl6.bookstore.domain.repository.jpa.CartDetailRepository;
import com.pbl6.bookstore.domain.repository.jpa.CartRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.request.AddBookToCartRequest;
import com.pbl6.bookstore.payload.response.NoContentResponse;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.book.BookDTO;
import com.pbl6.bookstore.payload.response.cart.ListCartDetailDTO;
import com.pbl6.bookstore.domain.entity.CartEntity;
import com.pbl6.bookstore.service.CartService;
import com.pbl6.bookstore.util.DateTimeUtils;
import com.pbl6.bookstore.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import static com.pbl6.bookstore.util.RequestUtils.*;

/**
 * @author lkadai0801
 * @since 04/11/2022
 */

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final SecurityUtils securityUtils;
    @Override
    @Transactional
    public Response<OnlyIdDTO> addBookToCart(AddBookToCartRequest request) {
        var cartDetails = cartDetailRepository.findAllByCartId(request.getCartId());
        var bookIds = cartDetails.stream().map(c -> c.getBook().getId()).collect(Collectors.toUnmodifiableList());
        var book = bookRepository.findById(request.getBookId()).orElseThrow(
                () -> new ObjectNotFoundException("bookId", request.getBookId())
        );
        var cart = cartRepository.findById(request.getCartId()).orElseThrow(
                () -> new ObjectNotFoundException("cartId", request.getCartId())
        );

        CartDetailEntity cartDetail = null;

        if (bookIds.contains(book.getId())){
            cartDetail = cartDetails.stream()
                    .filter(c -> c.getBook().getId().equals(book.getId()))
                    .findFirst()
                    .orElse(new CartDetailEntity());
            cartDetail.setQuantity(cartDetail.getQuantity() + request.getQuantity());
        } else {
            cartDetail = new CartDetailEntity();
            cartDetail.setCart(cart);
            cartDetail.setBook(book);
            cartDetail.setQuantity(request.getQuantity());
        }

        cartDetailRepository.save(cartDetail);
        cartRepository.save(cart);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(cart.getId())
                        .build())
                .build();
    }

    @Override
    public Response<ListCartDetailDTO> getCart(Long cartId) {
        var cartDetails = cartDetailRepository.findAllByCartIdFetchBook(cartId);

        // Tong gia tri don hang
        var total = cartDetails.stream()
                .reduce(0L, (a, b) -> a + b.getBook().getPrice() * b.getQuantity(), Long::sum);

        return Response.<ListCartDetailDTO>newBuilder()
                .setSuccess(true)
                .setData(ListCartDetailDTO.newBuilder()
                        .setTotalElement((long)cartDetails.size())
                        .setCartId(cartId)
                        .setTotal(total)
                        .setCartDetails(cartDetails.stream()
                                .map(c -> ListCartDetailDTO.CartDetailDTO.newBuilder()
                                        .setBook(BookDTO.newBuilder()
                                                .setId(c.getBook().getId())
                                                .setPrice(c.getBook().getPrice())
                                                .setAuthor(blankIfNull(c.getBook().getAuthor()))
                                                .setImage(blankIfNull(c.getBook().getImage()))
                                                .setPublisher(blankIfNull(c.getBook().getPublisher()))
                                                .setTitle(c.getBook().getTitle())
                                                .setDescription(blankIfNull(c.getBook().getDescription()))
                                                .setPublicationDate(DateTimeUtils.timestamp2String(c.getBook().getPublicationDate()))
                                                .setEdition(defaultIfNull(c.getBook().getEdition(), -1))
                                                .build())
                                        .setQuantity(c.getQuantity())
                                        .setId(c.getId())
                                        .build())
                                .collect(Collectors.toUnmodifiableList()))
                        .build())
                .build();
    }

    @Override
    public Response<NoContentResponse> deleteCartDetail(List<Long> cartDetailIds) {
        var principal = securityUtils.getPrincipal();
        var listCartDetailDel = cartDetailRepository.findAllById(cartDetailIds);
        listCartDetailDel = listCartDetailDel.stream()
                .filter(c ->  c.getCart().getId().equals(principal.getCartId()))
                .collect(Collectors.toUnmodifiableList());
        cartDetailRepository.deleteAll(listCartDetailDel);
        return Response.<NoContentResponse>newBuilder()
                .setData(NoContentResponse.builder().build())
                .build();
    }

    @Override
    public Response<OnlyIdDTO> addNewCart() {
        CartEntity cartEntity = new CartEntity();
        cartRepository.save(cartEntity);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(cartEntity.getId())
                        .build())
                .build();
    }
}
