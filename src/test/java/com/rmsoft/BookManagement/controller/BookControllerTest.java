package com.rmsoft.BookManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmsoft.BookManagement.domain.Book;
import com.rmsoft.BookManagement.domain.BookCategory;
import com.rmsoft.BookManagement.dto.BookRequest;
import com.rmsoft.BookManagement.dto.BookResponse;
import com.rmsoft.BookManagement.dto.MemberInfoRequest;
import com.rmsoft.BookManagement.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookController-Test")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @MockBean private BookService bookService;
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    BookRequest bookRequest;

    @BeforeEach
    void init(){
        bookRequest = new BookRequest("자바의신","남궁성","한빛","자바의 신입니다.","1");
    }

    @Nested
    @DisplayName("도서 등록 컨트롤러 테스트")
    class EnrollBook{

        @DisplayName("201")
        @Test
        void enrollBook_WhenBookRequestIsAvailable_ThenReturnCreatedStatus() throws Exception {

            doNothing().when(bookService).enrollBook(bookRequest);

            mvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.status").value("Created"))
                    .andExpect(jsonPath("$.message").value("도서 등록이 성공적으로 진행되었습니다."));
        }

        @DisplayName("400")
        @Test
        void enrollBook_WhenBookRequestIsNotAvailable_ThenReturnBadRequestStatus() throws Exception {

            doThrow(new RuntimeException("잘못된 카테고리번호 입니다.")).when(bookService).enrollBook(any(BookRequest.class));

            mvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("잘못된 카테고리번호 입니다."));
        }

    }

    @Nested
    @DisplayName("도서 수정 컨트롤러 테스트")
    class UpdateBook{

        @DisplayName("200")
        @Test
        void updateBook_WhenBookIdANdCategoryNoIsAvailable_ThenReturnOkStatus() throws Exception{
            String bookId = "1";

            doNothing().when(bookService).updateBook(bookId, bookRequest);

            mvc.perform(patch("/api/books/"+bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서 수정이 성공적으로 진행되었습니다."));
        }

        @DisplayName("400")
        @Test
        void updateBook_WhenBookIdIsNotAvailable_ThenReturnBadRequestStatus() throws Exception{
            String bookId = "1";

            doThrow(new RuntimeException("bookId : "+bookId+"에 해당하는 도서가 없습니다.")).when(bookService).updateBook(anyString(), any(BookRequest.class));

            mvc.perform(patch("/api/books/"+bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : "+bookId+"에 해당하는 도서가 없습니다."));
        }

    }

    @Nested
    @DisplayName("도서 단건 조회 컨트롤러 테스트")
    class GetBook{

        @DisplayName("200")
        @Test
        void getBookInfo_WhenBookIdIsAvailable_ThenReturnOkStatus() throws Exception {
            String bookId = "1";
            BookResponse bookResponse = new BookResponse("자바의 신", "남궁성", "한빛", "자바 내용", "프로그래밍 언어", "대출 가능");
            given(bookService.getBookById(bookId)).willReturn(bookResponse);

            mvc.perform(get("/api/books/"+bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서 정보가 성공적으로 조회되었습니다."));
        }

        @DisplayName("400")
        @Test
        void getBookInfo_WhenBookIdIsNotAvailable_ThenReturnOkStatus() throws Exception{
            String bookId = "1";
            doThrow(new RuntimeException("bookId : "+bookId+"에 해당하는 도서가 없습니다.")).when(bookService).getBookById(bookId);

            mvc.perform(get("/api/books/"+bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : "+bookId+"에 해당하는 도서가 없습니다."));
        }

    }

    @Nested
    @DisplayName("도서 여러권 조회 컨트롤러 테스트")
    class GetBooks{

        @DisplayName("도서 여러권 조회 - 200")
        @Test
        void getBooksInfo_WhenNothing_ThenReturnOkStatus() throws Exception{
            Page<BookResponse> responses = new PageImpl<>(new ArrayList<>());
            Pageable pageable = Pageable.ofSize(10);
            given(bookService.getBooks(pageable)).willReturn(responses);

            mvc.perform(get("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page","0")
                            .param("size", "10")
                            .param("sort", "createdAt,desc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서 정보가 성공적으로 조회되었습니다."));
        }

    }

    @Nested
    @DisplayName("도서 대출 컨트롤러 테스트")
    class LoanBook{

        @DisplayName("200")
        @Test
        void loanBook_WhenBookIdAndMemberInfoIsAvailable_ThenReturnOkStatus() throws Exception{
            String bookId = "1";
            MemberInfoRequest memberInfoRequest = new MemberInfoRequest("user01","pass01");
            doNothing().when(bookService).loanBookWithUserInfo(anyString(), any(MemberInfoRequest.class));

            mvc.perform(patch("/api/books/"+bookId+"/loan")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberInfoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서를 성공적으로 대출하였습니다."));
        }

        @DisplayName("400")
        @Test
        void loanBook_WhenBookIdIsNotAvailable_ThenReturnBadRequestStatus() throws Exception{
            String bookId = "1";
            MemberInfoRequest memberInfoRequest = new MemberInfoRequest("user01","pass01");
            doThrow(new RuntimeException("bookId : " + bookId + "에 해당하는 도서가 없습니다.")).when(bookService).loanBookWithUserInfo(anyString(), any(MemberInfoRequest.class));

            mvc.perform(patch("/api/books/"+bookId+"/loan")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberInfoRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : " + bookId + "에 해당하는 도서가 없습니다."));
        }

    }

    @Nested
    @DisplayName("도서 반납 컨트롤러 테스트")
    class ReturnBook{
        @DisplayName("200")
        @Test
        void returnBook_WhenBookIdIsAvailable_ThenReturnOkStatus() throws Exception{
            String bookId = "1";
            doNothing().when(bookService).returnBook(bookId);

            mvc.perform(patch("/api/books/"+bookId+"/return")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서를 성공적으로 반납하였습니다."));
        }

        @DisplayName("400")
        @Test
        void returnBook_WhenBookIdIsNotAvailable_ThenReturnOkStatus() throws Exception{
            String bookId = "1";
            doThrow(new RuntimeException("bookId : " + bookId + "에 해당하는 도서가 없습니다.")).when(bookService).returnBook(anyString());

            mvc.perform(patch("/api/books/"+bookId+"/return")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : " + bookId + "에 해당하는 도서가 없습니다."));
        }


    }



}