package huyvu.identityservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Entity
@NoArgsConstructor

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "identity_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    @ManyToMany
    Set<Role> roles;
}
