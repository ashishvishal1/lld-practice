package com.example;

import com.example.model.PaymentType;
import com.example.paymentfactory.PaymentFactory;

import com.example.paymentstrategy.Payment;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Starting Payment Strategy..." );


        Payment payment;
        try {
            payment= PaymentFactory.getInstance(PaymentType.CREDIT_CARD);
            payment.pay();
        } catch(Exception e) {
            System.out.println("Invalid Payment Type");
        }
        
        try {
            payment= PaymentFactory.getInstance(PaymentType.DEBIT_CARD);
            payment.pay();
        } catch(Exception e) {
            System.out.println("Invalid Payment Type");
        }
        try {
            payment= PaymentFactory.getInstance(PaymentType.PAYPAL);
            payment.pay();
        } catch(Exception e) {
            System.out.println("Invalid Payment Type");
        }
        System.out.println( "Ending Payment Strategy..." );
    }
}
