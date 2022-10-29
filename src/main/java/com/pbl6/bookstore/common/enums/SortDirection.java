package com.pbl6.bookstore.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author lkadai0801
 * @since 28/10/2022
 */

@AllArgsConstructor
@Getter
public enum SortDirection {
    ASC("asc", "ascending"),
    DESC("desc", "descending");

    private final String shortname;
    private final String name;

    public static boolean isInvalid(String given){
        return !(ASC.shortname.equalsIgnoreCase(given) || ASC.name.equalsIgnoreCase(given) ||
                DESC.shortname.equalsIgnoreCase(given) || DESC.name.equalsIgnoreCase(given));
    }

    public static SortDirection parse(String given){
        if (!StringUtils.hasText(given)){
            return ASC;
        }

        return Arrays.stream(values())
                .filter(s -> s.name.equalsIgnoreCase(given) || s.shortname.equalsIgnoreCase(given))
                .findFirst()
                .orElse(ASC);
    }
}
