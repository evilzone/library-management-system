package com.iitbtest.librarymanagementsystem.repository;

import com.iitbtest.librarymanagementsystem.entity.Book;
import com.iitbtest.librarymanagementsystem.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    public List<Book> findByStatus(Status status);
}
