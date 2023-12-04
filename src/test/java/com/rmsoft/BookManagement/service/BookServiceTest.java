package com.rmsoft.BookManagement.service;

import com.rmsoft.BookManagement.domain.Book;
import com.rmsoft.BookManagement.domain.BookCategory;
import com.rmsoft.BookManagement.domain.Member;
import com.rmsoft.BookManagement.dto.BookRequest;
import com.rmsoft.BookManagement.dto.BookResponse;
import com.rmsoft.BookManagement.dto.MemberInfoRequest;
import com.rmsoft.BookManagement.dto.MemberRequest;
import com.rmsoft.BookManagement.repository.BookCategoryRepository;
import com.rmsoft.BookManagement.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

@DisplayName("BookService-Test")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private BookCategoryRepository bookCategoryRepository;
    @Mock private MemberService memberService;
    @InjectMocks private BookService bookService;

    Book book;
    BookCategory bookCategory;
    BookRequest bookRequest;

    @BeforeEach
    void init(){
        bookCategory = new BookCategory(1L,"프로그래밍 언어");
        book = new Book(1L,"자바의신","남궁성","한빛","자바의 신입니다.","대출 가능", bookCategory, null);
        bookRequest = new BookRequest("자바의신","남궁성","한빛","자바의 신입니다.","1");
    }

    @Nested
    @DisplayName("도서 등록 서비스")
    class EnrollBook{
        @DisplayName("성공 케이스")
        @Test
        void enrollBookWithBookRequest_WhenBookRequestIsAvailable_thenCreateBook(){
            //Given
            given(bookCategoryRepository.findById(1L)).willReturn(Optional.of(bookCategory));

            //When
            bookService.enrollBook(bookRequest);

            //Then
            then(bookCategoryRepository).should().findById(1L);
        }

        @DisplayName("실패 케이스")
        @Test
        void enrollBookWithBookRequest_WhenBookRequestIsNotAvailable_ThenThrowException(){
            //Given
            given(bookCategoryRepository.findById(1L)).willReturn(Optional.empty());

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                bookService.enrollBook(bookRequest);
            });

            //Then
            assertEquals("잘못된 카테고리입니다.", exception.getMessage());
            then(bookCategoryRepository).should().findById(1L);
        }
    }

    @Nested
    @DisplayName("도서 수정 서비스")
    class UpdateBook{

        @DisplayName("성공 케이스")
        @Test
        void updateBookWithBookInfo_WhenBookInfoIsAvailable_ThenUpdateBook(){
            //Given
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));
            given(bookCategoryRepository.findById(1L)).willReturn(Optional.of(bookCategory));

            //When
            bookService.updateBook("1", bookRequest);

            //Then
            then(bookRepository).should().findById(1L);
            then(bookCategoryRepository).should().findById(1L);
        }

        @DisplayName("실패 케이스")
        @Test
        void updateBookWithBookInfo_WhenBookInfoIsNotAvailable_ThenThrowException(){
            //Given
            given(bookRepository.findById(1L)).willReturn(Optional.empty());

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                bookService.updateBook("1", bookRequest);
            });

            //Then
            assertEquals("bookId : 1에 해당하는 도서가 없습니다.", exception.getMessage());
            then(bookRepository).should().findById(1L);
            then(bookCategoryRepository).should(never()).findById(1L);
        }

    }

    @Nested
    @DisplayName("도서 단건 조회 서비스")
    class GetBookById{

        @DisplayName("성공 케이스")
        @Test
        void getBookByIdWithBookId_WhenBookIdIsAvailable_ThenReturnBook(){
            //Given
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));

            //When
            BookResponse result = bookService.getBookById("1");

            //Then
            assertThat(result).hasFieldOrPropertyWithValue("title","자바의신");
            assertThat(result).hasFieldOrPropertyWithValue("author","남궁성");
            assertThat(result).hasFieldOrPropertyWithValue("publisher","한빛");
        }

        @DisplayName("실패 케이스")
        @Test
        void getBookByIdWithBookId_WhenBookIdIsNotAvailable_ThenThrowException(){
            //Given
            given(bookRepository.findById(1L)).willReturn(Optional.empty());

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                BookResponse result = bookService.getBookById("1");
            });

            //Then
            assertEquals("bookId : 1에 해당하는 도서가 없습니다.", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("도서 여러권 조회 서비스")
    class GetBooks{

        @DisplayName("성공 케이스")
        @Test
        void getBooks_whenSelectBooks_thenReturnPage(){
            //Given
            Pageable pageable = Pageable.ofSize(10);
            given(bookRepository.findAll(pageable)).willReturn(Page.empty());

            //When
            Page<BookResponse> bookResponses = bookService.getBooks(pageable);

            //Then
            assertThat(bookResponses).isEmpty();
            then(bookRepository).should().findAll(pageable);
        }

    }

    @Nested
    @DisplayName("도서 대출 서비스")
    class LoanBook{

        @DisplayName("성공 케이스")
        @Test
        void loanBookWithMemberInfo_WhenBookIsAvailable_ThenChangeBookHistory(){
            //Given
            MemberRequest memberRequest = new MemberRequest("qwer123","qwer123!","박연준","01011112222");
            Member member = memberRequest.toEntity();
            String bookId = "1";
            MemberInfoRequest request = new MemberInfoRequest("qwer123","password");

            given(memberService.checkMember(request)).willReturn(member);
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));

            //When
            bookService.loanBookWithUserInfo(bookId,request);

            //Then
            assertEquals("대출 중", book.getHistory());
        }

        @DisplayName("실패 케이스1")
        @Test
        void loanBookWithMemberInfo_WhenBookIsNotAvailable_ThenThrowException(){
            //Given
            MemberRequest memberRequest = new MemberRequest("qwer123","qwer123!","박연준","01011112222");
            Member member = memberRequest.toEntity();
            String bookId = "1";
            book.setHistory("대출 중");
            MemberInfoRequest request = new MemberInfoRequest("qwer123","password");

            given(memberService.checkMember(request)).willReturn(member);
            given(bookRepository.findById(1L)).willReturn(Optional.of(book));

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                bookService.loanBookWithUserInfo(bookId,request);
            });
            //Then
            assertEquals("bookId : 1에 해당하는 도서는 이미 대출 중 입니다.", exception.getMessage());
        }

        @DisplayName("실패 케이스2")
        @Test
        void loanBookWithUserInfo_WhenMemberInfoDoesNotMatch_ThenThrowException() {
            //Given
            String bookId = "1";
            MemberInfoRequest request = new MemberInfoRequest("qwer123","password");
            doThrow(new RuntimeException("아이디가 잘못되었습니다.")).when(memberService).checkMember(request);

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                bookService.loanBookWithUserInfo(bookId,request);
            });

            //Then
            assertEquals("아이디가 잘못되었습니다.", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("도서 반납 서비스")
    class ReturnBook{

        @DisplayName("도서 반납 서비스 - 성공 케이스")
        @Test
        void returnBookWithBookId_WhenBookIsAvailable_ThenChangeBookHistory(){
            //Given
            String bookId = "1";
            book.setHistory("대출 중");
            given(bookRepository.findById(Long.valueOf(bookId))).willReturn(Optional.of(book));

            //When
            bookService.returnBook(bookId);

            //Then
            assertEquals("대출 가능",book.getHistory());
        }

        @DisplayName("도서 반납 서비스 - 실패 케이스1")
        @Test
        void returnBookWithBookId_WhenBookIsNotAvailable_ThenThrowException(){
            //Given
            String bookId = "1";
            given(bookRepository.findById(Long.valueOf(bookId))).willReturn(Optional.empty());

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                bookService.returnBook(bookId);
            });

            //Then
            assertEquals("bookId : 1에 해당하는 도서가 없습니다.",exception.getMessage());
        }

        @DisplayName("도서 반납 서비스 - 실패 케이스2")
        @Test
        void returnBookWithBookId_WhenBookAlreadyReturn_ThenThrowException(){
            //Given
            String bookId = "1";
            given(bookRepository.findById(Long.valueOf(bookId))).willReturn(Optional.of(book));

            //When
            Exception exception = assertThrows(RuntimeException.class, () -> {
                bookService.returnBook(bookId);
            });

            //Then
            assertEquals("bookId : 1는 현재 대출 가능한 도서입니다. 다시 시도해 주세요",exception.getMessage());
        }

    }









}