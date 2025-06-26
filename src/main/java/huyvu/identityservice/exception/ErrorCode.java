package huyvu.identityservice.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNCATEGORY_EXCEPTION(9999,"Uncategorized Exception"),
    INVALID_KEY(1001,"Invalid message key"),

    USER_EXIST(1002,"User already exists"),

    //validation user
    INVALID_USERNAME(1003,"Username must be at least 6 characters"),
    INVALID_PASSWORD(1004,"Password must be at least 6 characters"),
    USER_NOT_FOUND(1005,"User not found"),
<<<<<<< HEAD
    USER_NOT_EXIST(1006,"User not exists"),
    UNAUTHENTICATED(1007,"Unauthenticated"),

=======
>>>>>>> b3d575331accd42e003f9c1f8c8727032197deca

    ;
    private int code;
    private String message;

}
