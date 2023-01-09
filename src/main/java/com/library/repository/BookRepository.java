package com.library.repository;

import com.library.entity.Book;
import com.library.utils.dto.Book.SearchBookDto;
import com.library.utils.projections.BooksView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = """
            SELECT b.book_id,b.source_title,array_agg(concat(a.author_first_name,' ',a.author_last_name)) as authors,
                   b.language,b.release_year,b.isbn,bm.book_type
                                            FROM book_author ba
                                            JOIN book b on ba.book_id = b.book_id
                                            JOIN author a on ba.author_id = a.author_id
                                            LEFT JOIN book_meta bm on b.book_id = bm.book_id
                                            WHERE :#{#searchParams.title} is null or
                                            b.source_title = cast(:#{#searchParams.title} as character varying) AND
                                            :#{#searchParams.bookType} is null
                                            or bm.book_type = cast(:#{#searchParams.bookType} as character varying)
                                            GROUP BY b.book_id,bm.book_type
                                            HAVING ((:#{#searchParams.author} is null or
                                            array[array_agg(concat(a.author_first_name,' ',a.author_last_name)),
                                                         array_agg(a.author_first_name),
                                                         array_agg(a.author_last_name)] &&
                                            cast(array[:#{#searchParams.author}] as text[])) AND
                                            (:#{#searchParams.status} is null or
                                            b.status = cast(:#{#searchParams.status} as character varying)))""",
            nativeQuery = true)
    Page<BooksView> findAllBooks(SearchBookDto searchParams, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE book SET in_stock = in_stock - 1 " +
            "WHERE book_id = :bookId", nativeQuery = true)
    void updateBookStock(@Param(value = "bookId") Long bookId);

    Book findByBookId(Long bookId);
}

