package com.rmsoft.BookManagement.repository;

import com.rmsoft.BookManagement.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
}
