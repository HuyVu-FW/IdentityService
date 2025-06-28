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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
// tự động tạo contructor cho những biến có final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

     UserService userService;


    //convention of set name API
    @PostMapping
    ApiResponse<User> crateUser(@RequestBody @Valid UserCreationRequest request){


        return ApiResponse.<User>builder()
                .result(userService.createUser(request))
                .build();




    }
    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
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
}
