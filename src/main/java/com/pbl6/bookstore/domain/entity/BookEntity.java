package com.pbl6.bookstore.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author lkadai0801
 * @since 01/10/2022
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "publication_date")
    private Timestamp publicationDate;

    private Integer edition;

    private Long price;

    private String publisher;

    private Double weight;

    private String size;

    private Integer pages;

    private String description;

    private String author;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private BookCategoryEntity bookCategory;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<CartDetailEntity> cartDetails;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<OrderDetailEntity> orderDetails;
}
