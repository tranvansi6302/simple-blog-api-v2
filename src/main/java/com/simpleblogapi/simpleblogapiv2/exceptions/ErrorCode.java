package com.simpleblogapi.simpleblogapiv2.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    UNCAUGHT_EXCEPTION(9999, "An error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTS(1002, "User already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1003, "Email already exists", HttpStatus.BAD_REQUEST),

    INVALID_FULLNAME_REQUIRED(1004, "Fullname is required", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_REQUIRED(1005, "Email is required", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_REQUIRED(1006, "Password is required", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(1007, "Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_EXISTS(1008, "Email already exists", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1009, "Invalid key", HttpStatus.BAD_REQUEST),

    FORBIDDEN(1010, "Forbidden", HttpStatus.FORBIDDEN),
    UNAUTHORIZED(1011, "Unauthorized", HttpStatus.UNAUTHORIZED),

    EMAIL_OR_PASSWORD_INCORRECT(1012, "Email or password is incorrect", HttpStatus.BAD_REQUEST),
    TOKEN_REQUIRED(1013, "Token is required", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY_NAME_REQUIRED(1014, "Category name is required", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTS(1015, "Category already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1016, "Category not found", HttpStatus.NOT_FOUND),

    INVALID_POST_TITLE_REQUIRED(1017, "Post title is required", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND(1018, "Post not found", HttpStatus.NOT_FOUND),

    FILE_ERROR(1019, "File error", HttpStatus.BAD_REQUEST),

    FILE_IS_REQUIRED(1020, "File is required", HttpStatus.BAD_REQUEST),
    FILE_FORMAT_NOT_SUPPORTED(1021, "File format is not supported", HttpStatus.BAD_REQUEST),
    FILE_SIZE_TOO_LARGE(1022, "File size is too large", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1023, "Access denied", HttpStatus.FORBIDDEN),
    CATEGORY_ID_REQUIRED(1024, "Category id is required", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(1025, "Token not found", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1026, "Token is invalid", HttpStatus.UNAUTHORIZED),
    INVALID_POST_STATUS(1027, "Invalid post status", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_ADMIN(1028, "Cannot delete admin", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_POST(1029, "Category has post", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1030, "Role not found", HttpStatus.NOT_FOUND);
    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
