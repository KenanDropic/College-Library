package com.library.repository;

import com.library.entity.BookMeta;
import com.library.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Modifying
    @Query(value = """
            UPDATE publisher p
            SET
            publisher_name = coalesce(cast(cast(:#{#publisherName} as text) as character varying),p.publisher_name)
            WHERE (SELECT book_id FROM book b where b.book_id = :bookId)""", nativeQuery = true)
    BookMeta updateBookPressman(@Param(value = "bookId") Long bookId, String publisherName);
}
