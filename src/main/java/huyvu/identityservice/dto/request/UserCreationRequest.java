package huyvu.identityservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 6,message = "INVALID_USERNAME")
    String username;
    @Size(min =6,message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;


}
