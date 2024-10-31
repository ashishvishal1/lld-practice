package com.example.paymentstrategy;

public class CreditCardPayment implements Payment{
    @Override
    public void pay() {
        System.out.println("Payment via Credit card");
    }
}
