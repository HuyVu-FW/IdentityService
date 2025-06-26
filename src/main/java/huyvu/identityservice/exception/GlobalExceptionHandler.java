package huyvu.identityservice.exception;

import huyvu.identityservice.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = ApiResponse.builder()
                .message(exception.getMessage())
                .code(1001)
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value =  MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        // lấy ra message, message đó chính là ennum key
        String enumkey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        // xử lí phần nếu nhập sai tên message
        try {
            errorCode = ErrorCode.valueOf(enumkey);
        } catch (IllegalArgumentException | NullPointerException e) {
            // Không cần làm gì, vì errorCode đã mặc định
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }



    ///  xử lí tất cả cac exception nếu không nằm trong mấy cái trên
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ErrorCode.UNCATEGORY_EXCEPTION.getMessage());
        apiResponse.setCode(ErrorCode.UNCATEGORY_EXCEPTION.getCode());
        return ResponseEntity.badRequest().body(apiResponse);
    }

}
