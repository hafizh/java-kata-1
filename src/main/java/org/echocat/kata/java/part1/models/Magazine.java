package org.echocat.kata.java.part1.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

@Getter
@EqualsAndHashCode
public class Magazine extends Medium {

  @NonNull
  private final LocalDate publishedAt;

  @Builder
  public Magazine(@NonNull String title,
                  @NonNull String isbn,
                  @NonNull List<Author> authors,
                  @NonNull LocalDate publishedAt) {

    super(title, isbn, authors, MediumType.MAGAZINE);
    this.publishedAt = publishedAt;
  }
}
