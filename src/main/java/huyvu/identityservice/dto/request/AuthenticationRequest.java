package huyvu.identityservice.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    String username;
    String password;
}
