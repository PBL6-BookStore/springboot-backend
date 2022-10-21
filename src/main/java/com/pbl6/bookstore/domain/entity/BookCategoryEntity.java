package com.pbl6.bookstore.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author lkadai0801
 * @since 21/10/2022
 */

@Entity
@Table(name = "book_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookCategory")
    private List<BookEntity> books;
}
