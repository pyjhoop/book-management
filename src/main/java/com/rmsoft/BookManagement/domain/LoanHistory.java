package com.rmsoft.BookManagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class LoanHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Book book;

    @Setter
    private String history;

    private LoanHistory(Member member, Book book, String history) {
        this.member = member;
        this.book = book;
        this.history = history;
    }

    public static LoanHistory of(Member member, Book book, String history) {
        return new LoanHistory(member, book, history);
    }
}
