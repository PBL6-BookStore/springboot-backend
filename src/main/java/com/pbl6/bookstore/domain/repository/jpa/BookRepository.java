package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */
@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    @Override
    @Query("SELECT b from BookEntity b " +
            "left join fetch b.bookCategory " +
            "WHERE b.id = :bookId")
    Optional<BookEntity> findById(@Param("bookId") Long bookId);


    @Modifying
    @Query("UPDATE BookEntity SET image = :path WHERE id = :bookId")
    void updateImage(@Param("bookId") Long bookId, @Param("path") String path);

    @Query("SELECT b FROM BookEntity b WHERE b.title = :title")
    Optional<BookEntity> findBookByTitle(@Param("title") String title);
}
