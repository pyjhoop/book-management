package com.rmsoft.BookManagement.repository;

import com.rmsoft.BookManagement.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
