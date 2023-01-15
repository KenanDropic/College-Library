package com.library.repository;

import com.library.entity.Author;
import com.library.utils.dto.Author.SearchAuthorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = """
            SELECT * FROM author a
            WHERE :#{#dto.authorName} is null or
            concat(a.author_first_name,' ',a.author_last_name) LIKE concat('%',:#{#dto.authorName},'%')
            ORDER BY
            CASE WHEN :#{#dto.field} = 'author_id' AND :#{#dto.direction} = 'ASC' THEN
            a.author_id END,
            CASE WHEN :#{#dto.field} = 'author_name' AND :#{#dto.direction} = 'ASC' THEN
            concat(a.author_first_name,'',a.author_last_name) END,
            CASE WHEN :#{#dto.field} = 'author_id' AND :#{#dto.direction} = 'DESC' THEN
            a.author_id END DESC,
            CASE WHEN :#{#dto.field} = 'author_name' AND :#{#dto.direction} = 'DESC' THEN
            concat(a.author_first_name,'',a.author_last_name) END DESC
            """, nativeQuery = true)
    Page<Author> findAuthors(SearchAuthorDto dto, Pageable pageable);

    @Query(value = """
            SELECT * FROM author a
            WHERE lower(concat(author_first_name,'',author_last_name)) = :authorName
            """, nativeQuery = true)
    Optional<Author> findByAuthorName(@Param(value = "authorName") String authorName);
}
