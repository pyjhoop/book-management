package com.rmsoft.BookManagement.dto;

import com.rmsoft.BookManagement.domain.Book;
import com.rmsoft.BookManagement.domain.BookCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookRequest {

    private String title;
    private String author;
    private String publisher;
    private String content;
    private String categoryNo;


    public Book toEntity(BookCategory bookCategory) {
       return Book.of(title, author, publisher, content, bookCategory);
    }
}
