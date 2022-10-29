package com.pbl6.bookstore.domain.repository.dsl;

import com.pbl6.bookstore.common.Page;
import com.pbl6.bookstore.common.enums.SortDirection;
import com.pbl6.bookstore.domain.entity.BookEntity;
import com.pbl6.bookstore.domain.entity.QBookEntity;
import com.pbl6.bookstore.payload.request.ListBookRequest;
import com.pbl6.bookstore.util.RequestUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author lkadai0801
 * @since 28/10/2022
 */

@Repository
@RequiredArgsConstructor
public class BookDslRepository {
    private final QBookEntity book = QBookEntity.bookEntity;
    private final JPAQueryFactory queryBuilder;

    public Page<BookEntity> getListBook(ListBookRequest request){
        var page = RequestUtils.getPage(request.getPage());
        var size = RequestUtils.getSize(request.getSize());
        var offset = page * size;
        JPAQuery<BookEntity> query = queryBuilder.select(book)
                .from(book);
        if (StringUtils.hasText(request.getSearchTerm())){
            query.where(book.title.contains(request.getSearchTerm())
                    .or(book.author.contains(request.getSearchTerm())));
        }

        if (request.getCategoryId() != null && request.getCategoryId() > 0){
            query.where(book.bookCategory.id.eq(request.getCategoryId()));
        }

        JPAQuery<Long> count = query.clone().select(book.id.count());

        Order _order = StringUtils.hasText(request.getDirection()) &&
                !SortDirection.isInvalid(request.getDirection()) ?
                Order.valueOf(SortDirection.parse(request.getDirection()).getShortname().toUpperCase()) :
                Order.ASC;
        if (StringUtils.hasText(request.getSort())){
            if ("title".equalsIgnoreCase(request.getSort())){
                query.orderBy(new OrderSpecifier<>(_order, book.title));
            } else if ("category".equalsIgnoreCase(request.getSort())){
                query.orderBy(new OrderSpecifier<>(_order, book.id));
            } else if ("author".equalsIgnoreCase(request.getSort())){
                query.orderBy(new OrderSpecifier<>(_order, book.author));
            } else {
                query.orderBy(new OrderSpecifier<>(_order, book.title));
            }
        }
        if (Boolean.TRUE.equals(request.getAllInOne())){
            return new Page<>(query.fetch(), count.fetchFirst());
        }
        query.offset(offset).limit(size);
        return new Page<>(query.fetch(), count.fetchFirst());
    }
}
