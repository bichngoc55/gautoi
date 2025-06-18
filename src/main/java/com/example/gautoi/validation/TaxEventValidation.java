package com.example.gautoi.validation;

import com.example.gautoi.entity.Person;
import com.example.gautoi.exception.TaxValidationException;

public final class TaxEventValidation {
    public static void TaxAmountValidation(Double amount) {
        if(amount == null){
            throw new TaxValidationException("Tax amount cant be null");
        }
        if (amount < 0){
            throw new TaxValidationException("Amount cant be negative");
        }
//        else {
//// cant input 1tr hooc not double value
//        }

    }
}
