package com.ryanzou.librarydatabase.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private final String STATUS = "error";
    private String message;
}