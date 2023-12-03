package com.rmsoft.BookManagement.domain;

import com.rmsoft.BookManagement.dto.BookRequest;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
public class Book extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false)
    private String title;

    @Setter @Column(nullable = false)
    private String author;

    @Setter @Column(nullable = false)
    private String publisher;

    @Setter
    private String content;

    @Setter
    private String history;

    @Setter @ManyToOne
    private BookCategory bookCategory;

    @Setter @ManyToOne
    private Member member;

    private Book(String title, String author, String publisher, String content, BookCategory bookCategory) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.content = content;
        this.bookCategory = bookCategory;
    }

    public static Book of(String title, String author, String publisher, String content, BookCategory bookCategory) {
        return new Book(title, author, publisher, content, bookCategory);
    }

    public void update(BookRequest bookRequest, BookCategory bookCategory){
        if (bookRequest.getTitle() != null) {
            this.title = bookRequest.getTitle();
        }
        if (bookRequest.getAuthor() != null) {
            this.author = bookRequest.getAuthor();
        }
        if (bookRequest.getPublisher() != null) {
            this.publisher = bookRequest.getPublisher();
        }
        if (bookRequest.getContent() != null) {
            this.content = bookRequest.getContent();
        }
        if (bookCategory != null) {
            this.bookCategory = bookCategory;
        }
    }
}
