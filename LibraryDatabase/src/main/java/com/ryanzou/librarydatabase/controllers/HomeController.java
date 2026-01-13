package com.ryanzou.librarydatabase.controllers;

import com.ryanzou.librarydatabase.beans.Book;
import com.ryanzou.librarydatabase.beans.Review;
import com.ryanzou.librarydatabase.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    DatabaseAccess databaseAccess;

    @GetMapping("/")
    public String goHome(Model model) {
        model.addAttribute("books", databaseAccess.getBooks());
        return "index";
    }

    @GetMapping("/login")
    public String goLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String goRegister(Model model) {
        List<String> authorities = databaseAccess.getAuthorities();
        model.addAttribute("authorities", authorities);
        return "register";
    }

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @PostMapping("/addUser")
    public String addUser(@RequestParam String userName,
                          @RequestParam String password,
                          @RequestParam String[] authorities,
                          RedirectAttributes redirectAttributes) {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        for (String authority : authorities) {
            authorityList.add(new SimpleGrantedAuthority(authority));
        }
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(userName, encodedPassword, authorityList);

        jdbcUserDetailsManager.createUser(user);
        redirectAttributes.addFlashAttribute("message", "Successfully registered. Please log in");
        return "redirect:/";
    }

    @GetMapping("/permissionDenied")
    public String goPermissionDenied() {
        return "error/permission-denied";
    }

    @GetMapping("/secured")
    public String goSecured() {
        return "secured/gateway";
    }

    @GetMapping("/user")
    public String goUserViewBooks(Model model) {
        model.addAttribute("books", databaseAccess.getBooks());
        return "/secured/user/index";
    }

    @GetMapping("/viewReviews/{bookId}")
    public String goViewBookReviews(@PathVariable long bookId, Model model) {
        model.addAttribute("book", databaseAccess.getBookById(bookId).get(0));
        model.addAttribute("reviews", databaseAccess.getReviewsByBookId(bookId));
        return "/view-reviews";
    }

    @GetMapping("/user/viewReviews/{bookId}")
    public String goUserViewBookReviews(@PathVariable long bookId, Model model) {
        model.addAttribute("book", databaseAccess.getBookById(bookId).get(0));
        model.addAttribute("reviews", databaseAccess.getReviewsByBookId(bookId));
        return "/secured/user/view-reviews";
    }

    @GetMapping("/user/addReview/{bookId}")
    public String goAddReview(@PathVariable long bookId, Model model, @ModelAttribute Review review) {
        model.addAttribute("bookToReviewId", bookId);
        return "secured/user/add-review";
    }

    @PostMapping("/user/postReview")
    public String addReviewToDatabase(@ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        databaseAccess.addReview(review);
        redirectAttributes.addFlashAttribute("message", "Review added successfully");
        return "redirect:/user";
    }

    @GetMapping("/admin/addBook")
    public String goAddBook(@ModelAttribute Book book) {
        return "/secured/admin/add-book";
    }

    @PostMapping("/admin/postBook")
    public String addBookToDatabase(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        databaseAccess.addBook(book);
        redirectAttributes.addFlashAttribute("message", "Book added successfully");
        return "redirect:/user";
    }
}
