package com.library.repository;

import com.library.entity.BookWriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookWriteOffRepository extends JpaRepository<BookWriteOff,Long> {
}
