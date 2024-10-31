package com.example;

import com.example.controller.LibraryController;
import com.example.model.Address;
import com.example.model.Book;
import com.example.model.User;

import java.util.HashMap;
import java.util.TreeSet;

import com.example.model.Library;
/**
 * Hello world!
 *
 */
public class LibraryManagmentApp 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        User user1= new User.UserBuilder()
                        .setFirstName("Apple")
                        .setEmail("apple@gmail.com")
                        .build();

        User user2= new User.UserBuilder()
        .setFirstName("Boy")
        .setEmail("boy@gmail.com")
        .build();

        User user3= new User.UserBuilder()
        .setFirstName("Cat")
        .setEmail("cat@gmail.com")
        .build();

        Book book1 = new Book("premchand", "Push Ki Rat",100);
        Book book2 = new Book("Mahadevi Verma", "Gilhari",200);
        Book book3 = new Book("Ramdhari Singh Dinkar", "Mangar",300);


        Library library1 = new Library("Patna Library", new Address(8, "Lalpari Bhawan", "Nariyalghat Takiyapar", "Danapur", "Patna", "Bihar", "India", 800012));

        library1.updateBooks(
            new HashMap<Book, Integer>(){{
            put(book1, 1);
            put(book2, 1);
            put(book3, 1);
        }}
        );

        library1.addUsers(new TreeSet<User>(){{add(user1); add(user2); add(user3);}});

        System.out.println(library1.toString());
        System.out.println(library1.getUsers());

        LibraryController libraryController = new LibraryController(library1);
        libraryController.reserveBook(book1, user1);
        libraryController.returnBook(book1, user2);
        libraryController.reserveBook(book1, user3);
        libraryController.returnBook(book1, user1);
        libraryController.reserveBook(book1, user3);




    }
}
