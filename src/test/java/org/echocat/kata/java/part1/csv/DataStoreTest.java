package org.echocat.kata.java.part1.csv;

import org.echocat.kata.java.part1.models.Author;
import org.echocat.kata.java.part1.models.Book;
import org.echocat.kata.java.part1.models.Magazine;
import org.echocat.kata.java.part1.models.Medium;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DataStoreTest {

  private DataStore dataStore;
  @Before
  public void setUp() {
    this.dataStore = new DataStore();
  }

  @Test
  public void getAllAuthors() {
    List<Author> authors = dataStore.getAllAuthors();
    assertThat(authors.size(), is(6));
  }

  @Test
  public void getAuthorByEmail() {
    Author expected = Author.builder().email("null-ferdinand@echocat.org").firstname("Franz").lastname("Ferdinand").build();
    Optional<Author> authorByEmail = dataStore.getAuthorByEmail("null-ferdinand@echocat.org");
    assertTrue(authorByEmail.isPresent());
    assertThat(authorByEmail.get(), is(expected));
  }

  @Test
  public void getMediumByIsbnMagazineIsbn() {
    Author author = Author.builder().email("null-ferdinand@echocat.org").firstname("Franz").lastname("Ferdinand").build();
    Magazine expected = Magazine.builder()
      .title("Gourmet")
      .isbn("2365-8745-7854")
      .authors(Collections.singletonList(author))
      .publishedAt(LocalDate.of(2010, 6, 14))
      .build();
    Optional<Medium> result = dataStore.getMediumByIsbn("2365-8745-7854");

    assertTrue(result.isPresent());
    assertThat(result.get(), is(expected));
  }

  @Test
  public void getMediumByIsbnBookIsbn() {
    Author author = Author.builder().email("null-ferdinand@echocat.org").firstname("Franz").lastname("Ferdinand").build();
    Author author2 = Author.builder().email("null-lieblich@echocat.org").firstname("Werner").lastname("Lieblich").build();
    Book expected = Book.builder()
      .title("Das große GU-Kochbuch Kochen für Kinder")
      .isbn("2145-8548-3325")
      .authors(Arrays.asList(author, author2))
      .description("Es beginnt mit... den ersten Löffelchen für Mami, Papi und den Rest der Welt. Ja, und dann? Was Hersteller von Babynahrung können, das ist oft im Handumdrehen auch selbst gemacht, vielleicht sogar gesünder, oftmals frischer. Ältere Babys und Schulkinder will das Kochbuch ansprechen und das tut es auf eine verspielte Art angenehm altersgemäß.")
      .build();
    Optional<Medium> result = dataStore.getMediumByIsbn("2145-8548-3325");

    assertTrue(result.isPresent());
    assertThat(result.get(), is(expected));
  }
}
