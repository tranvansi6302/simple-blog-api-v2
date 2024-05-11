package com.simpleblogapi.simpleblogapiv2.exceptions;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
}
