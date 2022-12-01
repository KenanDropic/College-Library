package com.library.repository;

import com.library.entity.BookMeta;
import com.library.entity.Pressman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PressmanRepository extends JpaRepository<Pressman,Long> {

    @Modifying
    @Query(value = """
            UPDATE pressman p
            SET
            pressman_name = coalesce(cast(cast(:#{#pressmanName} as text) as character varying),p.pressman_name)
            WHERE (SELECT book_id FROM book b where b.book_id = :bookId)""", nativeQuery = true)
    BookMeta updateBookPressman(@Param(value = "bookId") Long bookId, String pressmanName);
}
