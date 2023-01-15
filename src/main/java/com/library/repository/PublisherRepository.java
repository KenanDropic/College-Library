package com.library.repository;

import com.library.entity.BookMeta;
import com.library.entity.Publisher;
import com.library.utils.dto.Publisher.SearchPublisherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Modifying
    @Query(value = """
            UPDATE publisher p
            SET
            publisher_name = coalesce(cast(cast(:#{#publisherName} as text) as character varying),p.publisher_name)
            WHERE (SELECT book_id FROM book b where b.book_id = :bookId)""", nativeQuery = true)
    BookMeta updateBookPublisher(@Param(value = "bookId") Long bookId, String publisherName);

    @Query(value = """
            SELECT * FROM publisher p
            WHERE :#{#dto.publisherName} is null or
            p.publisher_name LIKE cast(:#{#dto.publisherName} as character varying)
            """, nativeQuery = true)
    Page<Publisher> findPublishers(SearchPublisherDto dto, Pageable pageable);

    @Query(value = """
            SELECT * FROM publisher p WHERE
            lower(replace(publisher_name,' ','')) = :publisherName
            """, nativeQuery = true)
    Optional<Publisher> findByPublisherName(@Param(value = "publisherName") String publisherName);
}
