package org.echocat.kata.java.part1.csv;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.echocat.kata.java.part1.models.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

public class DataStore {

  private static final String AUTHORS_PATH = "authors.csv";
  private static final String BOOKS_PATH = "books.csv";
  private static final String MAGAZINES_PATH = "magazines.csv";

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  private Map<String, Medium> mediumByIsbn;
  private Map<String, Author> authorsByEmail;
  public static final CSVParser CSV_PARSER = new CSVParserBuilder()
    .withSeparator(';')
    .withIgnoreQuotations(true)
    .build();

  public DataStore() {
    this.authorsByEmail = initAuthors();
    this.mediumByIsbn = initMedium();
  }

  public List<Author> getAllAuthors() {
    return Collections.unmodifiableList(new ArrayList<>(authorsByEmail.values()));
  }

  public Optional<Author> getAuthorByEmail(String email) {
    return Optional.ofNullable(authorsByEmail.get(email));
  }

  public List<Medium> getAllMedia() {
    return Collections.unmodifiableList(new ArrayList<>(mediumByIsbn.values()));
  }

  public List<Book> getAllBooks() {
    return Collections.unmodifiableList(
      mediumByIsbn.values()
        .stream()
        .filter(medium -> medium.getMediumType() == MediumType.BOOK)
        .map(book -> (Book) book)
        .collect(Collectors.toList()));
  }

  public List<Magazine> getAllMagazines() {
    return Collections.unmodifiableList(
      mediumByIsbn.values()
        .stream()
        .filter(medium -> medium.getMediumType() == MediumType.MAGAZINE)
        .map(magazine -> (Magazine) magazine)
        .collect(Collectors.toList()));
  }

  public Optional<Medium> getMediumByIsbn(String isbn) {
    return Optional.ofNullable(mediumByIsbn.get(isbn));
  }

  private Map<String, Medium> initMedium() {
    try (Reader booksReader = Files.newBufferedReader(Paths.get(this.getClass().getClassLoader().getResource(BOOKS_PATH).toURI()));
         Reader magazinesReader = Files.newBufferedReader(Paths.get(this.getClass().getClassLoader().getResource(MAGAZINES_PATH).toURI()))) {

      HashMap<String, Medium> tempMap = new HashMap<>();
      for(Book book: readBooks(booksReader)) {
        tempMap.put(book.getIsbn(), book);
      }

      for(Magazine magazine: readMagazines(magazinesReader)) {
        tempMap.put(magazine.getIsbn(), magazine);
      }

      return Collections.unmodifiableMap(tempMap);
    } catch (URISyntaxException | IOException | CsvException e) {
      e.printStackTrace();
      return Collections.emptyMap();
    }
  }

  private Map<String, Author> initAuthors() {
    try (Reader reader = Files.newBufferedReader(Paths.get(this.getClass().getClassLoader().getResource(AUTHORS_PATH).toURI()))) {
      return readAuthors(reader).stream()
        .collect(Collectors.toMap(Author::getEmail, identity()));
    } catch (IOException | URISyntaxException | CsvException e) {
      System.out.println("Couldn't read the authors");
      e.printStackTrace();
      return Collections.emptyMap();
    }
  }

  private List<Author> readAuthors(Reader reader) throws IOException, CsvException {
    CSVReader csvReader = initCsvReader(reader);

    return csvReader.readAll().stream()
      .map(this::toAuthor)
      .collect(Collectors.toList());
  }

  private List<Book> readBooks(Reader reader) throws IOException, CsvException {
    CSVReader csvReader = initCsvReader(reader);

    return csvReader.readAll().stream()
      .map(this::toBook)
      .collect(Collectors.toList());
  }

  private List<Magazine> readMagazines(Reader reader) throws IOException, CsvException {
    CSVReader csvReader = initCsvReader(reader);

    return csvReader.readAll().stream()
      .map(this::toMagazine)
      .collect(Collectors.toList());
  }

  private CSVReader initCsvReader(Reader reader) {
    return new CSVReaderBuilder(reader)
      .withSkipLines(1)
      .withCSVParser(CSV_PARSER)
      .build();
  }

  private Author toAuthor(String[] columns) {
    return Author.builder()
      .email(columns[0])
      .firstname(columns[1])
      .lastname(columns[2])
      .build();
  }

  private Book toBook(String[] columns) {
    return Book.builder()
      .title(columns[0])
      .isbn(columns[1])
      .authors(emailsToAuthors(columns[2]))
      .description(columns[3])
      .build();
  }

  private Magazine toMagazine(String[] columns) {
    return Magazine.builder()
      .title(columns[0])
      .isbn(columns[1])
      .authors(emailsToAuthors(columns[2]))
      .publishedAt(toDate(columns[3]))
      .build();
  }

  private List<Author> emailsToAuthors(String emailsStr) {
    return Stream.of(emailsStr.split(","))
      .map(email -> authorsByEmail.get(email))
      .collect(Collectors.toList());
  }

  private LocalDate toDate(String dateStr) {
    return LocalDate.parse(dateStr, DATE_FORMATTER);
  }
}
