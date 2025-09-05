package huyvu.identityservice.controller;

import huyvu.identityservice.dto.request.ApiResponse;
import huyvu.identityservice.dto.request.UserCreationRequest;
import huyvu.identityservice.dto.request.UserUpdateRequest;
import huyvu.identityservice.dto.response.UserResponse;
import huyvu.identityservice.model.User;
import huyvu.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
// tự động tạo contructor cho những biến có final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

     UserService userService;


    //convention of set name API
    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){


        return ApiResponse.<User>builder()
                .result(userService.createUser(request))
                .build();
    }

    //kiểm tra trước khi vào hàm
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<User>>  getUsers(){
        // spring sẽ tự map thong tin từ scop
        // lấy thông tin trong context
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("UsernameL {}", authentication.getName());
        authentication.getAuthorities().forEach(
                role -> log.info("RoleL {}", role.getAuthority())
        );




        return ApiResponse.<List<User>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable String userId){
        return userService.getUser(userId);

    }

    @PutMapping("/{userId}")
    public UserResponse updateUser( @PathVariable String userId ,@RequestBody UserUpdateRequest request ){
        return userService.updateUser(userId,request);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }


    @GetMapping("/myInfo")
    public ApiResponse<UserResponse>  myInfo(){
        var result = userService.getMyInfo();
        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();

    }
}
