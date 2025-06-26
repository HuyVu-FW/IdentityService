package huyvu.identityservice.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
<<<<<<< HEAD
import lombok.*;
=======
import lombok.Getter;
import lombok.Setter;
>>>>>>> b3d575331accd42e003f9c1f8c8727032197deca


@Getter
@Setter
<<<<<<< HEAD
@Builder
@NoArgsConstructor
@AllArgsConstructor
=======
>>>>>>> b3d575331accd42e003f9c1f8c8727032197deca
@JsonInclude(JsonInclude.Include.NON_NULL) //cau hinh cho jackson bo qua null field
public class ApiResponse <T>{
  int code = 1000;
  String message;
  T result;


}
