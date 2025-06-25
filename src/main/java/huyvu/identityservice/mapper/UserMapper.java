package huyvu.identityservice.mapper;

import huyvu.identityservice.dto.request.UserCreationRequest;
import huyvu.identityservice.dto.request.UserUpdateRequest;
import huyvu.identityservice.dto.response.UserResponse;
import huyvu.identityservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// do khai báo là spring nên có thể autowire
@Mapper(componentModel = "spring")
public interface UserMapper {

     // lấy thông tin cua thằng rquest tạo User
     User toUser(UserCreationRequest request);

     // lấy thông tin từ thằng request gán cho thằng user
     void updateUser(@MappingTarget User user, UserUpdateRequest request);
    // source là cái đi map, target là cái mình sẽ lấy về
//    @Mapping(source = "",target = "")
//     @Mapping(target = "lastName",ignore = true) // bỏ field trong target
     UserResponse toUserResponse(User user);
}
