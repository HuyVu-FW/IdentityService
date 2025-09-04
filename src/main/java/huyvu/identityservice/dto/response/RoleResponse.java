package huyvu.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import huyvu.identityservice.model.Permission;
import huyvu.identityservice.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse {
    String name;
    String description;
    Set<Permission  > permissions;
}


