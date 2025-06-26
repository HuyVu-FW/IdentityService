package huyvu.identityservice.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //cau hinh cho jackson bo qua null field
public class ApiResponse <T>{
  int code = 1000;
  String message;
  T result;


}
