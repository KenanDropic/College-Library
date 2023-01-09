package com.library.repository;

import com.library.entity.BookMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookMetaRepository extends JpaRepository<BookMeta, Long> {
    @Query(value = """
            SELECT * FROM book_meta bm
            WHERE bm.book_id = :bookId""", nativeQuery = true)
    Optional<BookMeta> findMetaByBookId(@Param(value = "bookId") Long bookId);
}
