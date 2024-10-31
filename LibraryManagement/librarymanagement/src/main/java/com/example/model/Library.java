package com.example.model;


import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.TreeSet;

public class Library {
    private String id;
    private String name;
    private Address address;
    Set<User> users;
    Map<Book, Integer> books;
    Map<User, Book> rentedUsers;
    Map<Book, User> rentedBooks; 

    public Library(String name, Address address) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.books = new HashMap<>();
        this.users = new TreeSet<>();
        this.rentedBooks = new HashMap<>();
        this.rentedUsers = new HashMap<>();
    }

    public Set<User> getUsers() {
        return users;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Map<Book, Integer> getBooks() {
        return books;
    }

    public void updateBook(Book book, Integer count) {
        if(this.books.containsKey(book)) {
            this.books.put(book, this.books.get(book)+count);
        } else {
            this.books.put(book,count);
        }
    }

    public void updateBooks(Map<Book, Integer> books) {
        books.entrySet().forEach(
            entry -> updateBook( entry.getKey(), entry.getValue())
        );
    }

    public void addUsers(Set<User> users) {
        users.stream().forEach(user -> this.users.add(user));
    }

    public void setRentedBooks(Map<Book, User> rentedBooks) {
        this.rentedBooks = rentedBooks;
    }

    public void setRentedUsers(Map<User, Book> rentedUsers) {
        this.rentedUsers = rentedUsers;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Address getAddress() {
        return address;
    }

    public Map<Book, User> getRentedBooks() {
        return rentedBooks;
    }

    public Map<User, Book> getRentedUsers() {
        return rentedUsers;
    }

    @Override
    public String toString() {
        return "Library Name: "+ this.name + " \n Books: \n{" + this.books.entrySet().stream().map(entry -> "BookName: " + entry.getKey().toString() + ", " + 
            " Count: "+ entry.getValue()).collect(Collectors.joining(",\n")) + "}";
    }
}
