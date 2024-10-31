package com.example.paymentstrategy;

public class DebitCardPayment implements Payment{
    @Override
    public void pay() {
        System.out.println("Payment via Debit Card");
    }
}
