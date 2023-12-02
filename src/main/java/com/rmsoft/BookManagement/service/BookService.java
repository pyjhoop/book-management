package com.rmsoft.BookManagement.service;

import com.rmsoft.BookManagement.domain.Book;
import com.rmsoft.BookManagement.domain.BookCategory;
import com.rmsoft.BookManagement.dto.BookRequest;
import com.rmsoft.BookManagement.dto.BookResponse;
import com.rmsoft.BookManagement.dto.MemberInfoRequest;
import com.rmsoft.BookManagement.repository.BookCategoryRepository;
import com.rmsoft.BookManagement.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final MemberService memberService;

    public void enrollBook(BookRequest bookRequest){
        // 카테고리 불러오기
        BookCategory bookCategory = bookCategoryRepository.findById(Long.valueOf(bookRequest.getCategoryNo()))
                .orElseThrow(()-> new RuntimeException("잘못된 카테고리입니다."));

        Book book = bookRequest.toEntity(bookCategory);
        book.setHistory("대출 가능");
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(String bookId, BookRequest bookRequest){
        Book book = bookRepository.findById(Long.valueOf(bookId))
                .orElseThrow(() -> new RuntimeException("bookId : "+bookId+"에 해당하는 도서가 없습니다."));

        BookCategory bookCategory = bookCategoryRepository.findById(Long.valueOf(bookRequest.getCategoryNo()))
                .orElseThrow(()-> new RuntimeException("잘못된 카테고리입니다."));

        book.update(bookRequest, bookCategory);

    }

    public BookResponse getBookById(String bookId){
        Book book = bookRepository.findById(Long.valueOf(bookId))
                .orElseThrow(() -> new RuntimeException("bookId : "+bookId+"에 해당하는 도서가 없습니다."));
        BookResponse response = BookResponse.from(book);

        return response;
    }

    public Page<BookResponse> getBooks(Pageable pageable){
        return bookRepository.findAll(pageable).map(BookResponse::from);
    }


    @Transactional
    public void loanBookWithUserInfo(String bookId, MemberInfoRequest request){

        if(memberService.checkMember(request)){
            Book book = bookRepository.findById(Long.valueOf(bookId))
                    .orElseThrow(() -> new RuntimeException("bookId : " + bookId + "에 해당하는 도서가 없습니다."));

            if(book.getHistory().equals("대출 가능")){
                book.setHistory("대출 중");
            }else{
                throw new RuntimeException("bookId : "+ bookId + "에 해당하는 도서는 이미 대출 중 입니다.");
            }

        }else{
            throw new RuntimeException("회원정보가 일치하지 않습니다.");
        }

    }

    @Transactional
    public void returnBook(String bookId){
        Book book = bookRepository.findById(Long.valueOf(bookId))
                .orElseThrow(() -> new RuntimeException("bookId : "+bookId+"에 해당하는 도서가 없습니다."));

        if(book.getHistory().equals("대출 중")) {
            book.setHistory("대출 가능");
        }else{
            throw new RuntimeException("bookId : "+bookId+"는 현재 대출 가능한 도서입니다. 다시 시도해 주세요");
        }
    }

}
