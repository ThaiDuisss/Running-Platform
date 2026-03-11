package com.running_platform.dto.response;

import com.running_platform.enums.ResponseStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ApiResponse<T> {
    private  String status ;
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(ResponseStatus.SUCCESS.toString())
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(String message,T data) {
        return ApiResponse.<T>builder()
                .status(ResponseStatus.SUCCESS.toString())
                .code(201)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        return ApiResponse.<T>builder()
                .status(ResponseStatus.ERROR.toString())
                .code(code)
                .message(message)
                .build();
    }
}
