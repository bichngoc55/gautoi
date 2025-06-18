package com.example.gautoi.validation;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.exception.PersonValidationException;
import java.time.LocalDate;

public final class PersonEventValidation {

    public static void validatePersonDTO(PersonRequestDTO personDTO, boolean checkAllFields) {
        if (personDTO == null) {
            throw new PersonValidationException("Person data cant be null");
        }
        String taxNumber = personDTO.taxNumber();
        if (taxNumber == null || taxNumber.length() != 8 ) {
            throw new PersonValidationException("Tax number must be an 8 length string");
        }
        if (checkAllFields) {
            if (personDTO.firstName() == null || personDTO.lastName() == null) {
                throw new PersonValidationException("Name cant be null");
            }
            if (personDTO.dateOfBirth() == null) {
                throw new PersonValidationException("Date of birth cant be null");
            }
            if (!personDTO.dateOfBirth().isBefore(LocalDate.now())) {
                throw new PersonValidationException( "Date of birth must be before the current date");
            }
        }
    }

}
