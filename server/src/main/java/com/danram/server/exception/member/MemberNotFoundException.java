package com.danram.server.exception.member;

import com.danram.server.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class MemberNotFoundException extends RuntimeException {
    private String message;
    private ErrorCode code;

    public MemberNotFoundException(Long id) {
        super(id.toString());

        this.message = id.toString();
    }
}
