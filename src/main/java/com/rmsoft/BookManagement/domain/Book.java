package com.rmsoft.BookManagement.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "author"),
        @Index(columnList = "publisher")

})
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

    @ManyToOne
    private BookCategory bookCategory;

    @OneToMany(mappedBy = "book")
    private final List<LoanHistory> loanHistories = new ArrayList<>();


    private Book(String title, String author, String publisher, String content) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.content = content;
    }

    public static Book of(String title, String author, String publisher, String content) {
        return new Book(title, author, publisher, content);
    }
}
