package com.rmsoft.BookManagement.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Table(indexes = {@Index(columnList = "userId")})
@Entity
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(columnDefinition = "VARCHAR(36)", nullable = false, unique = true)
    private String userId;

    @Setter @Column(nullable = false)
    private String password;

    @Setter @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String userName;

    @Setter @Column(columnDefinition = "VARCHAR(12)", nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    private final List<Book> books = new ArrayList<>();

    private Member(String userId, String password, String userName, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public static Member of(String userId, String password, String userName, String phoneNumber) {
        return new Member(userId, password, userName, phoneNumber);
    }

}
