package com.ghifar.lms.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;
    private String author;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {

        AVAILABLE, NOT_AVAILABLE, BORROWED
    }
}
