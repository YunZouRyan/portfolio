package com.ryanzou.librarydatabase.controllers;

import com.ryanzou.librarydatabase.beans.BookREST;
import com.ryanzou.librarydatabase.beans.ErrorMessage;
import com.ryanzou.librarydatabase.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    DatabaseAccess databaseAccess;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookAndReviews(@PathVariable long id) {
        BookREST book = databaseAccess.getBookAndReviews(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No such record"));
        }
    }

}
