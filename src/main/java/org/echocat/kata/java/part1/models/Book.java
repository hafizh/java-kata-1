package org.echocat.kata.java.part1.models;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString(callSuper = true)
public class Book extends Medium {

  @NonNull
  private final String description;

  @Builder
  public Book(@NonNull String title,
              @NonNull String isbn,
              @NonNull List<Author> authors,
              @NonNull String description) {

    super(title, isbn, authors, MediumType.BOOK);
    this.description = description;
  }
}
