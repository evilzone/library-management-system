package com.iitbtest.librarymanagementsystem.controller;

import com.iitbtest.librarymanagementsystem.Service.BookService;
import com.iitbtest.librarymanagementsystem.Service.JwtService;
import com.iitbtest.librarymanagementsystem.entity.Book;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private final BookService bookService;

    @Autowired
    private final JwtService jwtService;

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @GetMapping
    public ResponseEntity<List<Book>> viewBooks() {
        return new ResponseEntity<>(bookService.viewBooks(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @GetMapping("/available")
    public ResponseEntity<List<Book>> viewAvailableBook() {
        return new ResponseEntity<>(bookService.viewAvailableBooks(), HttpStatus.OK);
    }

    // member can borrow a book
    @PreAuthorize("hasAuthority('MEMBER')")
    @PutMapping("/borrow/{id}")
    public ResponseEntity<Book> borrowBook(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtService.retrieveUsername(token);

        return new ResponseEntity<Book>(bookService.borrowBook(id, email), HttpStatus.OK);
    }

    // member can return a book
    @PreAuthorize("hasAuthority('MEMBER')")
    @PutMapping("/return/{id}")
    public ResponseEntity<Book> returnBook(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtService.retrieveUsername(token);

        return new ResponseEntity<Book>(bookService.returnBook(id, email), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return new ResponseEntity<Book>(bookService.addBook(book), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @PutMapping("{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        return new ResponseEntity<Book>(bookService.updateBook(book, id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> removeBook(@PathVariable("id") Long bookId) {
        bookService.removeBook(bookId);
        return new ResponseEntity<String>("Book deleted successfully!!!", HttpStatus.OK);
    }
}
