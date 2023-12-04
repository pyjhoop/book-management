package com.rmsoft.BookManagement.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmsoft.BookManagement.domain.Book;
import com.rmsoft.BookManagement.domain.BookCategory;
import com.rmsoft.BookManagement.dto.BookRequest;
import com.rmsoft.BookManagement.dto.BookResponse;
import com.rmsoft.BookManagement.dto.BooksResponse;
import com.rmsoft.BookManagement.dto.MemberInfoRequest;
import com.rmsoft.BookManagement.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("BookController-Test")
@WebMvcTest(BookController.class)
class BookControllerTest extends ControllerConfig{

    @MockBean private BookService bookService;
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

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.status").value("Created"))
                    .andExpect(jsonPath("$.message").value("도서 등록이 성공적으로 진행되었습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 등록")
                                    .requestFields(
                                            fieldWithPath("title").description("도서 제목"),
                                            fieldWithPath("author").description("저자"),
                                            fieldWithPath("publisher").description("출판사"),
                                            fieldWithPath("content").description("간략 내용"),
                                            fieldWithPath("categoryNo").description("카테고리 번호")
                                    ).requestSchema(Schema.schema("BookRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }

        @DisplayName("400")
        @Test
        void enrollBook_WhenBookRequestIsNotAvailable_ThenReturnBadRequestStatus() throws Exception {

            doThrow(new RuntimeException("잘못된 카테고리번호 입니다.")).when(bookService).enrollBook(any(BookRequest.class));

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("잘못된 카테고리번호 입니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 등록")
                                    .requestFields(
                                            fieldWithPath("title").description("도서 제목"),
                                            fieldWithPath("author").description("저자"),
                                            fieldWithPath("publisher").description("출판사"),
                                            fieldWithPath("content").description("간략 내용"),
                                            fieldWithPath("categoryNo").description("카테고리 번호")
                                    ).requestSchema(Schema.schema("BookRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
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

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.patch("/api/books/{bookId}",bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서 수정이 성공적으로 진행되었습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 수정")
                                    .requestFields(
                                            fieldWithPath("title").description("도서 제목"),
                                            fieldWithPath("author").description("저자"),
                                            fieldWithPath("publisher").description("출판사"),
                                            fieldWithPath("content").description("간략 내용"),
                                            fieldWithPath("categoryNo").description("카테고리 번호")
                                    ).requestSchema(Schema.schema("BookRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }

        @DisplayName("400")
        @Test
        void updateBook_WhenBookIdIsNotAvailable_ThenReturnBadRequestStatus() throws Exception{
            String bookId = "1";

            doThrow(new RuntimeException("bookId : "+bookId+"에 해당하는 도서가 없습니다.")).when(bookService).updateBook(anyString(), any(BookRequest.class));

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.patch("/api/books/{bookId}",bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : "+bookId+"에 해당하는 도서가 없습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 수정")
                                    .requestFields(
                                            fieldWithPath("title").description("도서 제목"),
                                            fieldWithPath("author").description("저자"),
                                            fieldWithPath("publisher").description("출판사"),
                                            fieldWithPath("content").description("간략 내용"),
                                            fieldWithPath("categoryNo").description("카테고리 번호")
                                    ).requestSchema(Schema.schema("BookRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
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

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.get("/api/books/{bookId}",bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서 정보가 성공적으로 조회되었습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 단건 조회")
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터"),
                                            fieldWithPath("data.title").description("도서 제목"),
                                            fieldWithPath("data.author").description("저자"),
                                            fieldWithPath("data.publisher").description("출판사"),
                                            fieldWithPath("data.content").description("간략 내용"),
                                            fieldWithPath("data.category").description("카테고리"),
                                            fieldWithPath("data.history").description("대출 이력")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }

        @DisplayName("400")
        @Test
        void getBookInfo_WhenBookIdIsNotAvailable_ThenReturnOkStatus() throws Exception{
            String bookId = "1";
            doThrow(new RuntimeException("bookId : "+bookId+"에 해당하는 도서가 없습니다.")).when(bookService).getBookById(bookId);

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.get("/api/books/{bookId}",bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : "+bookId+"에 해당하는 도서가 없습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 단건 조회")
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }

    }

    @Nested
    @DisplayName("도서 여러권 조회 컨트롤러 테스트")
    class GetBooks{

        @DisplayName("도서 여러권 조회 - 200")
        @Test
        void getBooksInfo_WhenNothing_ThenReturnOkStatus() throws Exception{

            // given
            BookResponse bookResponse = new BookResponse("자바의신","남궁성","한빛","자바의 신입니다.","프로그래밍 언어","대출 가능");
            List<BookResponse> bookResponseList = Arrays.asList(bookResponse);
            Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
            Page<BookResponse> bookResponsePage = new PageImpl<>(bookResponseList, pageable, bookResponseList.size());
            System.out.println(bookResponsePage.getPageable().getPageNumber());
            given(bookService.getBooks(pageable)).willReturn(bookResponsePage);


            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.get("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page","0")
                            .param("size","10")
                            .param("sort","createdAt,desc")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서 정보가 성공적으로 조회되었습니다."))
                    .andExpect(jsonPath("$.data").exists());


            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 여러권 조회")
                                    .requestParameters(
                                            parameterWithName("page").description("현재 페이지 번호").optional(),
                                            parameterWithName("size").description("한 페이지당 아이템 수").optional(),
                                            parameterWithName("sort").description("정렬 방법").optional()
                                    )
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터"),
                                            fieldWithPath("data.bookList").description("도서 리스트"),
                                            fieldWithPath("data.bookList[].title").description("도서 제목"),
                                            fieldWithPath("data.bookList[].author").description("저자"),
                                            fieldWithPath("data.bookList[].publisher").description("출판사"),
                                            fieldWithPath("data.bookList[].content").description("간략 내용"),
                                            fieldWithPath("data.bookList[].category").description("카테고리"),
                                            fieldWithPath("data.bookList[].history").description("대출 이력"),
                                            fieldWithPath("data.pageSize").description("현제 페이지"),
                                            fieldWithPath("data.totalPages").description("토탈 페이지")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
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

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.patch("/api/books/{bookId}/loan",bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberInfoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서를 성공적으로 대출하였습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 대출")
                                    .pathParameters(
                                            parameterWithName("bookId").description("도서 ID")
                                    )
                                    .requestFields(
                                            fieldWithPath("userId").description("유저 아이디"),
                                            fieldWithPath("password").description("비밀번호")
                                    ).requestSchema(Schema.schema("MemberInfoRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }

        @DisplayName("400")
        @Test
        void loanBook_WhenBookIdIsNotAvailable_ThenReturnBadRequestStatus() throws Exception{
            String bookId = "1";
            MemberInfoRequest memberInfoRequest = new MemberInfoRequest("user01","pass01");
            doThrow(new RuntimeException("bookId : " + bookId + "에 해당하는 도서가 없습니다.")).when(bookService).loanBookWithUserInfo(anyString(), any(MemberInfoRequest.class));

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.patch("/api/books/{bookId}/loan",bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberInfoRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : " + bookId + "에 해당하는 도서가 없습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 대출")
                                    .pathParameters(
                                            parameterWithName("bookId").description("도서 ID")
                                    )
                                    .requestFields(
                                            fieldWithPath("userId").description("유저 아이디"),
                                            fieldWithPath("password").description("비밀번호")
                                    ).requestSchema(Schema.schema("MemberInfoRequest"))
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
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

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.patch("/api/books/{bookId}/return",bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("Ok"))
                    .andExpect(jsonPath("$.message").value("도서를 성공적으로 반납하였습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 반납")
                                    .pathParameters(
                                            parameterWithName("bookId").description("도서 ID")
                                    )
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }

        @DisplayName("400")
        @Test
        void returnBook_WhenBookIdIsNotAvailable_ThenReturnOkStatus() throws Exception{
            String bookId = "1";
            doThrow(new RuntimeException("bookId : " + bookId + "에 해당하는 도서가 없습니다.")).when(bookService).returnBook(anyString());

            ResultActions perform = mvc.perform(RestDocumentationRequestBuilders.patch("/api/books/{bookId}/return",bookId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("Bad Request"))
                    .andExpect(jsonPath("$.message").value("bookId : " + bookId + "에 해당하는 도서가 없습니다."));

            perform.andDo(document("{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(
                            ResourceSnippetParameters.builder()
                                    .tag("Book")
                                    .description("도서 반납")
                                    .pathParameters(
                                            parameterWithName("bookId").description("도서 ID")
                                    )
                                    .responseFields(
                                            fieldWithPath("code").description("응답 코드"),
                                            fieldWithPath("status").description("응답 상태"),
                                            fieldWithPath("message").description("응답 메세지"),
                                            fieldWithPath("data").description("응답 데이터")
                                    ).responseSchema(Schema.schema("Api"))
                                    .build()
                    )));
        }


    }



}