package com.example.paymentfactory;

import com.example.model.PaymentType;
import com.example.paymentstrategy.CreditCardPayment;
import com.example.paymentstrategy.DebitCardPayment;
import com.example.paymentstrategy.Payment;
import com.example.paymentstrategy.PaypalPayment;

public class PaymentFactory {

    private PaymentFactory(){}

    public static Payment getInstance(PaymentType paymentType) throws Exception{
        switch (paymentType) {
            case DEBIT_CARD:
                return new DebitCardPayment();
            case PAYPAL:
                return new PaypalPayment();
            case CREDIT_CARD:
                return new CreditCardPayment();
            default:
                throw new Exception("Unknown payment type");
        }
    }
    
}
