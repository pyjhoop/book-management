package com.rmsoft.BookManagement.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "bookCategory")
    private final List<Book> books = new ArrayList<>();

    private BookCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public static BookCategory of(String categoryName) {
        return new BookCategory(categoryName);
    }
}
