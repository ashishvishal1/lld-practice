package com.example.model;

import java.util.List;
import java.util.stream.Collectors;

public class SplitDetail {
    User author;
    User lender;
    List<User> borrowers;
    double moneyTobeSplited;

    public SplitDetail(User author, User lender, List<User> borrowers, double moneyTobeSplited) {
        this.author = author;
        this.lender = lender;
        this.borrowers = borrowers;
        this.moneyTobeSplited = moneyTobeSplited;
    }

    public User getAuthor() {
        return author;
    }

    public List<User> getBorrowers() {
        return this.borrowers;
    }

    public User getLender() {
        return lender;
    }

    public double getMoneyTobeSplited(){
        return this.moneyTobeSplited;
    }

    @Override
    public String toString() {
        return "Author "+ this.author.getPhoneNumber()
        + " Lender "+ this.lender.getPhoneNumber()
        + " Borrowers " + this.borrowers.stream().map(borrower -> borrower.getPhoneNumber()).collect(Collectors.joining(","))
        + " Amount To be Splitted " + this.moneyTobeSplited;
    }

}