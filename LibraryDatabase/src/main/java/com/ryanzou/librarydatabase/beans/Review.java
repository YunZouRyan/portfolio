package com.ryanzou.librarydatabase.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Review {
    private Long id;
    private Long bookId;
    private String text;
}
