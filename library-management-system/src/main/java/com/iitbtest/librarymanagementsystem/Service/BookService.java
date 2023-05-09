package com.iitbtest.librarymanagementsystem.Service;

import com.iitbtest.librarymanagementsystem.entity.Book;
import com.iitbtest.librarymanagementsystem.entity.Status;
import com.iitbtest.librarymanagementsystem.entity.User;
import com.iitbtest.librarymanagementsystem.exception.ResourceNotFoundException;
import com.iitbtest.librarymanagementsystem.repository.BookRepository;
import com.iitbtest.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    public Book addBook(Book book) {
        book.setStatus(Status.AVAILABLE);
        return bookRepository.save(book);
    }

    public Book viewBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "Id", id));

        return book;
    }

    public List<Book> viewBooks() {
        return bookRepository.findAll();
    }

    public List<Book> viewAvailableBooks() {
        return bookRepository.findByStatus(Status.AVAILABLE);
    }
    public void removeBook(Long bookId) {
        // we need to check whether the book exists in the given database or not
        bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("Book", "Id", bookId));

        bookRepository.deleteById(bookId);
    }

    public Book updateBook(Book book, Long bookId) {
        // we need to check whether the book exists in the given database or not
        Book existingBook = bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("Book", "Id", bookId));

        //update basic information only
        existingBook.setBookName(book.getBookName());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublishDate(book.getPublishDate());

        bookRepository.save(existingBook);

        return existingBook;
    }

    public Book borrowBook(Long id, String email) {
        // we need to check whether the book exists in the given database or not
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "Id", id));

        Optional<User> user = userRepository.findByEmail(email);

        if(existingBook.getStatus().equals(Status.BORROWED) || user.isEmpty()) {
            throw new ResourceNotFoundException("Book", "Id", id);
        }

        existingBook.setStatus(Status.BORROWED);
        existingBook.setUser(user.get());
        bookRepository.save(existingBook);

        return existingBook;
    }

    public Book returnBook(Long id, String email) {
        // we need to check whether the book exists in the given database or not
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book", "Id", id));

        Optional<User> user = userRepository.findByEmail(email);

        if(existingBook.getStatus().equals(Status.AVAILABLE) || user.isEmpty() ||
                !Objects.equals(existingBook.getUser().getId(), user.get().getId())) {
            throw new ResourceNotFoundException("Book", "Id", id);
        }

        existingBook.setStatus(Status.AVAILABLE);
        existingBook.setUser(null);

        bookRepository.save(existingBook);

        return existingBook;
    }
}
