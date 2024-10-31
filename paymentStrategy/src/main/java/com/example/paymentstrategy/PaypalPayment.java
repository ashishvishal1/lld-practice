package com.example.paymentstrategy;

public class PaypalPayment implements Payment{
    @Override
    public void pay() {
        System.out.println("Paypal Payment");
    }
}
