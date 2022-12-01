package com.library.repository;

import com.library.entity.CoAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoAuthorRepository extends JpaRepository<CoAuthor,Long> {
}
