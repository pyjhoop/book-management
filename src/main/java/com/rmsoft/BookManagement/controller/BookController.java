package com.rmsoft.BookManagement.controller;

import com.rmsoft.BookManagement.domain.Book;
import com.rmsoft.BookManagement.dto.Api;
import com.rmsoft.BookManagement.dto.BookRequest;
import com.rmsoft.BookManagement.dto.BookResponse;
import com.rmsoft.BookManagement.dto.MemberInfoRequest;
import com.rmsoft.BookManagement.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<Api<?>> enrollBook(@RequestBody BookRequest bookRequest)
    {
        bookService.enrollBook(bookRequest);
        log.info("Book enrolled successfully. book title: {}", bookRequest.getTitle());

        return ResponseEntity.status(201)
                .body(Api.builder()
                        .code(201)
                        .status("Created")
                        .message("도서 등록이 성공적으로 진행되었습니다.")
                        .build());
    }

    @PatchMapping("/books/{bookId}")
    public ResponseEntity<Api<?>> updateBook(
            @PathVariable String bookId,
            @RequestBody BookRequest bookRequest
    ){

        bookService.updateBook(bookId, bookRequest);
        log.info("Book updated successfully. book title: {}", bookRequest.getTitle());

        return ResponseEntity.status(200)
                .body(Api.builder()
                        .code(200)
                        .status("Ok")
                        .message("도서 수정이 성공적으로 진행되었습니다.")
                        .build());
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<Api<?>> getBookInfo(@PathVariable String bookId)
    {
        BookResponse response = bookService.getBookById(bookId);
        log.info("Get book successfully. book title: {}", response.getTitle());

        return ResponseEntity.status(200)
                .body(Api.builder()
                        .code(200)
                        .status("Ok")
                        .message("도서 정보가 성공적으로 조회되었습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/books")
    public ResponseEntity<Api<?>> getBooksInfo(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            )
    {

        Page<BookResponse> responses = bookService.getBooks(pageable);
        log.info("Get books successfully");

        return ResponseEntity.status(200)
                .body(Api.builder()
                        .code(200)
                        .status("Ok")
                        .message("도서 정보가 성공적으로 조회되었습니다.")
                        .data(responses)
                        .build());
    }


    @PostMapping("/books/{bookId}/loan")
    public ResponseEntity<Api<?>> loanBook(
            @PathVariable String bookId,
            @RequestBody MemberInfoRequest memberInfoRequest
            )
    {
        bookService.loanBookWithUserInfo(bookId, memberInfoRequest);
        log.info("Lent book successfully. bookId: {}", bookId);

        return ResponseEntity.status(200)
                .body(Api.builder()
                        .code(200)
                        .status("Ok")
                        .message("도서를 성공적으로 대출하였습니다.")
                        .build());
    }

    @PostMapping("/books/{bookId}/return")
    public ResponseEntity<Api<?>> returnBook(
            @PathVariable String bookId
    )
    {
        bookService.returnBook(bookId);
        log.info("Return book successfully. bookId: {}", bookId);

        return ResponseEntity.status(200)
                .body(Api.builder()
                        .code(200)
                        .status("Ok")
                        .message("도서를 성공적으로 반납하였습니다.")
                        .build());
    }



}
