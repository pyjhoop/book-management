package com.rmsoft.BookManagement.dto;

import com.rmsoft.BookManagement.domain.Book;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookResponse {

    private String title;
    private String author;
    private String publisher;
    private String content;
    private String category;
    private String history;

    public static BookResponse from(Book book) {
        return new BookResponse(book.getTitle(), book.getAuthor(), book.getPublisher(), book.getContent(), book.getBookCategory().getCategoryName(), book.getHistory());
    }
}
