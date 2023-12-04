package com.rmsoft.BookManagement.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BooksResponse {
    private List<BookResponse> bookList;
    private long pageSize;
    private long totalPages;

    public static BooksResponse from(Page<BookResponse> responses) {
        return new BooksResponse(responses.getContent(), responses.getPageable().getPageNumber(), responses.getTotalPages());
    }
}
