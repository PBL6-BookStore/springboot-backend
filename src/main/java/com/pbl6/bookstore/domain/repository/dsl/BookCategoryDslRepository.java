package com.pbl6.bookstore.domain.repository.dsl;

import com.pbl6.bookstore.common.Page;
import com.pbl6.bookstore.domain.entity.BookCategoryEntity;
import com.pbl6.bookstore.domain.entity.QBookCategoryEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author lkadai0801
 * @since 01/11/2022
 */

@Repository
@RequiredArgsConstructor
public class BookCategoryDslRepository {
    private final QBookCategoryEntity category = QBookCategoryEntity.bookCategoryEntity;
    private final JPAQueryFactory queryBuilder;

    public Page<BookCategoryEntity> findAll(String searchTerm){
        JPAQuery<BookCategoryEntity> query = queryBuilder.select(category)
                .from(category);
        if (StringUtils.hasText(searchTerm)){
            query.where(category.name.containsIgnoreCase(searchTerm));
        }

        JPAQuery<Long> countQuery = query.clone().select(category.countDistinct());
        return new Page<>(query.fetch(), countQuery.fetchFirst());
    }
}
