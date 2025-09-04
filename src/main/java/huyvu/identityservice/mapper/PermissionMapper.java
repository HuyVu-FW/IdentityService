package huyvu.identityservice.mapper;

import huyvu.identityservice.dto.request.PermissionRequest;
import huyvu.identityservice.dto.response.PermissionResponse;
import huyvu.identityservice.model.Permission;
import org.mapstruct.Mapper;

// do khai báo là spring nên có thể autowire
@Mapper(componentModel = "spring")
public interface PermissionMapper {


     Permission toPermission(PermissionRequest permissionRequest);
     PermissionResponse toPermissionResponse(Permission permission);
}
