package com.ryanzou.librarydatabase.database;

import com.ryanzou.librarydatabase.beans.Book;
import com.ryanzou.librarydatabase.beans.BookREST;
import com.ryanzou.librarydatabase.beans.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class DatabaseAccess {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public List<String> getAuthorities() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        // using DISTINCT to not get duplicates
        String query = "SELECT DISTINCT authority FROM authorities";

        return jdbc.queryForList(query, namedParameters,
                String.class);
    }

    public List<Book> getBooks() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM books";

        BeanPropertyRowMapper<Book> bookMapper =
                new BeanPropertyRowMapper<>(Book.class);

        List<Book> books = jdbc.query(query, namedParameters, bookMapper);
        return books;
    }

    public int addBook(Book book) {
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource();

        String query = "INSERT INTO books (title, author)" +
                "VALUES (:title, :author)";

        namedParameters
                .addValue("title", book.getTitle())
                .addValue("author", book.getAuthor());

        int returnValue = jdbc.update(query, namedParameters);
        return returnValue;
    }

    public List<Book> getBookById(long id) {
        long bookId = id;
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        String query = "SELECT * FROM books WHERE id = :id";

        namedParameters.addValue("id", bookId);

        BeanPropertyRowMapper<Book> userInfoMapper =
                new BeanPropertyRowMapper<>(Book.class);

        List<Book> book = jdbc.query(query, namedParameters, userInfoMapper);
        return book;
    }

    public List<Review> getReviewsByBookId(long id) {
        long bookId = id;
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        String query = "SELECT * FROM reviews WHERE bookid = :id";

        namedParameters.addValue("id", bookId);

        BeanPropertyRowMapper<Review> userInfoMapper =
                new BeanPropertyRowMapper<>(Review.class);

        List<Review> reviews = jdbc.query(query, namedParameters, userInfoMapper);
        return reviews;
    }

    public int addReview(Review review) {
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource();

        String query = "INSERT INTO reviews (bookid, text)" +
                "VALUES (:bookid, :text)";

        namedParameters
                .addValue("bookid", review.getBookId())
                .addValue("text", review.getText());

        int returnValue = jdbc.update(query, namedParameters);
        return returnValue;
    }

    public BookREST getBookAndReviews(long id) {
        long bookId = id;
        Book book = getBookById(id).get(0);
        List<Review> reviews = getReviewsByBookId(bookId);
        BookREST bookREST = new BookREST(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                reviews);
        return bookREST;
    }
}
