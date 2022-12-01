package com.library.repository;

import com.library.entity.BookMeta;
import com.library.utils.dto.Book.UpdateMetaDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMetaRepository extends JpaRepository<BookMeta, Long> {

    @Modifying
    @Query(value = """
            UPDATE book_meta bm
            SET
            book_type = coalesce(cast(cast(:#{#updateParams.bookType} as text) as character varying),bm.book_type),
            page_numbers = coalesce(cast(cast(:#{#updateParams.pageNumbers} as text) as int),bm.page_numbers),
            binding = coalesce(cast(cast(:#{#updateParams.binding} as text) as character varying),bm.binding),
            size = coalesce(cast(cast(:#{#updateParams.size} as text) as character varying),bm.size),
            shape = coalesce(cast(cast(:#{#updateParams.shape} as text) as character varying),bm.shape),
            e_form = coalesce(cast(cast(:#{#updateParams.EForm} as text) as character varying),bm.e_form),
            e_location = coalesce(cast(cast(:#{#updateParams.ELocation} as text) as character varying),bm.e_location)
            WHERE bm.book_id = :bookId""", nativeQuery = true)
    void updateBookMeta(@Param(value = "bookId") Long bookId, UpdateMetaDto updateParams);
}
