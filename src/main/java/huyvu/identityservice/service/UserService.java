package huyvu.identityservice.service;


import huyvu.identityservice.dto.request.UserCreationRequest;
import huyvu.identityservice.dto.request.UserUpdateRequest;
import huyvu.identityservice.dto.response.UserResponse;
import huyvu.identityservice.enums.Role;
import huyvu.identityservice.exception.AppException;
import huyvu.identityservice.exception.ErrorCode;
import huyvu.identityservice.mapper.UserMapper;
import huyvu.identityservice.model.User;
import huyvu.identityservice.repository.RoleRepository;
import huyvu.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
// tự động tạo contructor cho những biến có final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {


    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder  passwordEncoder;
    RoleRepository roleRepository;

    public User createUser(UserCreationRequest request) {


        if (userRepository.existsUserByUsername(request.getUsername()))
        {
            throw new AppException(ErrorCode.USER_EXIST);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // set default Role
//        user.setRoles(Set.of(Role.USER.name()));
        return userRepository.save(user);
    }

    // thường sẽ chặn ở đây

    // hasRole thì nó tự động config thêm ROLE ở trong scoe
    // còn hasAuthority thì nó so sánh trực tiếp, không thêm gì
    @PreAuthorize("hasAuthority('CREATE_DATA')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }



    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id) {

        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow( () -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));

    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }


    public UserResponse getMyInfo(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        return userMapper.toUserResponse(user);
    }
}
