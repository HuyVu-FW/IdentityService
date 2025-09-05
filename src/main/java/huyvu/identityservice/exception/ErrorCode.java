package huyvu.identityservice.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNCATEGORY_EXCEPTION(9999,"Uncategorized Exception",HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_KEY(1001,"Invalid message key",HttpStatus.UNPROCESSABLE_ENTITY),

    USER_EXIST(1002,"User already exists",HttpStatus.BAD_REQUEST),

    //validation user
    INVALID_USERNAME(1003,"Username must be at least 6 characters",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004,"Password must be at least 6 characters",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005,"User not found",HttpStatus.NOT_FOUND),

    USER_NOT_EXIST(1006,"User not exists",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007,"Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008,"Unauthorized",HttpStatus.FORBIDDEN),
    INVALID_DOB(1008,"Invalid Date of Birth {min}",HttpStatus.BAD_REQUEST),




    ;
    private int code;
    private String message;
    private HttpStatus statusCode;

}
