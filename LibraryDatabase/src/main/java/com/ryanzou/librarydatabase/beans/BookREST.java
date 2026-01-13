package com.ryanzou.librarydatabase.beans;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookREST {
    private Long id;
    private String title;
    private String author;
    private List<Review> reviews;
}
