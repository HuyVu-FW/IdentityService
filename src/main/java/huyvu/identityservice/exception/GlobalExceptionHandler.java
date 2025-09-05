package huyvu.identityservice.exception;

import huyvu.identityservice.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE= "min";

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {

        ApiResponse apiResponse = ApiResponse.builder().message(exception.getMessage()).code(1001).build();


        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        // lấy ra message, message đó chính là ennum key
        String enumkey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        Map<String,Object> attributes = null ;

        // xử lí phần nếu nhập sai tên message
        try {
            errorCode = ErrorCode.valueOf(enumkey);

            var constrainViolation = exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes =constrainViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            // Không cần làm gì, vì errorCode đã mặc định
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(),attributes)

    :errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    private String mapAttribute(String message, Map<String,Object> attributes) {
         String minvalue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
         return message.replace("{"+MIN_ATTRIBUTE+"}",minvalue);
    }


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDemiedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .message(exception.getMessage())
                        .code(errorCode.getCode())
                        .build());
    }


    ///  xử lí tất cả cac exception nếu không nằm trong mấy cái trên
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ErrorCode.UNCATEGORY_EXCEPTION.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORY_EXCEPTION.getCode());
        return ResponseEntity
                .status(ErrorCode.UNCATEGORY_EXCEPTION.getStatusCode())
                .body(apiResponse);
    }

}
