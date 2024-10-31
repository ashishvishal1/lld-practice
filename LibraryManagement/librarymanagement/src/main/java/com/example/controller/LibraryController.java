package com.example.controller;

import com.example.model.Library;
import com.example.model.User;

import java.util.Map;

import com.example.model.Book;

public class LibraryController {


    Library library;

    public LibraryController(Library library) {
        this.library=library;
    }

    public boolean isBookAvailable(Book book) {
        if(library.getBooks().containsKey(book)) {
            return library.getBooks().get(book)>0;
        }
        return false;
    }


    public void reserveBook(Book book, User user) {
        if(isBookAvailable(book)) {
            Map<Book, User> rentedBooks = library.getRentedBooks();
            rentedBooks.put(book, user);
            library.setRentedBooks(rentedBooks);

            Map<User, Book> rentedUsers = library.getRentedUsers();
            rentedUsers.put(user, book);
            library.setRentedUsers(rentedUsers);

            System.out.println(library.getRentedBooks().toString());
            library.updateBook(book, -1);
        } else {
            System.out.println("Book "+book.getTitle()+ " is not Available.");
        }
    }

    public void returnBook(Book book, User user) {
        if(library.getRentedBooks().containsKey(book) && library.getRentedBooks().get(book).equals(user)) {
            Map<Book, User> rentedBooks = library.getRentedBooks();
            rentedBooks.remove(book, user);
            library.setRentedBooks(rentedBooks);

            Map<User, Book> rentedUsers = library.getRentedUsers();
            rentedUsers.remove(user, book);
            library.setRentedUsers(rentedUsers);

            System.out.println(library.getRentedBooks().toString());
            library.updateBook(book, 1);
        } else {
            System.out.println("Book "+book.getTitle()+ " is not booked previously or is in different name.");
        }
    }
    
}