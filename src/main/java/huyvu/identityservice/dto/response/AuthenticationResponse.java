package huyvu.identityservice.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {

    String token;
    boolean authenticated;
}
