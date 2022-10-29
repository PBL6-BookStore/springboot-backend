package com.pbl6.bookstore.payload.response.book;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */

@Getter
@Setter
@SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
public class BookDTO {
    private Long id;
    private String title;
    private String publicationDate;
    private Integer edition;
    private Long price;
    private String publisher;
    private String author;
    private String image;
    private String description;
}
