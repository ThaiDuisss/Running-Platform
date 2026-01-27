package com.laundry.exception;

import com.laundry.constant.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppException extends RuntimeException {

    private int code;
    private List<FieldValidateException> fieldValidateExceptions;

    public AppException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }
    public AppException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AppException(String message, int code, List<FieldValidateException> fieldValidateExceptions) {
        super(message);
        this.code = code;
        this.fieldValidateExceptions = fieldValidateExceptions;
    }

}

