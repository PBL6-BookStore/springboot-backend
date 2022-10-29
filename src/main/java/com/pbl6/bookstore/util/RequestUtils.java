package com.pbl6.bookstore.util;

import com.pbl6.bookstore.payload.request.AbstractPageRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * @author lkadai0801
 * @since 30/09/2022
 */
public class RequestUtils {
    public static String blankIfNull(@Nullable String value){
        return StringUtils.hasText(value) ? value : "";
    }

    public static Long defaultIfNull(@Nullable Long value, Long defaultValue){
        return value == null ? defaultValue : value;
    }

    public static Integer defaultIfNull(@Nullable Integer value, Integer defaultValue){
        return value == null ? defaultValue : value;
    }

    public static int getPage(Integer page){
        return (page == null || page <= 0) ? 0 : page - 1;
    }

    public static int getSize(Integer size){
        return (size == null || size < 1) ? 100 : size;
    }

    public static int getTotalPage(Long totalElement, AbstractPageRequest pageRequest){
        return (int)(totalElement + pageRequest.getSize() - 1) / pageRequest.getSize();
    }
}
