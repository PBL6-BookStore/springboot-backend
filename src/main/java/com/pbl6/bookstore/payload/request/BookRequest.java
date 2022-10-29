package com.pbl6.bookstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String title;
    private String publicationDate;
    private Integer edition;
    private String publisher;
    private Long price;
    private Double weight;
    private String size;
    private Integer pages;
    private String description;
    private String author;
    private Long categoryId;
}
