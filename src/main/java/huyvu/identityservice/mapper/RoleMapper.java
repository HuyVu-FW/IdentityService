package huyvu.identityservice.mapper;

import huyvu.identityservice.dto.request.PermissionRequest;
import huyvu.identityservice.dto.request.RoleRequest;
import huyvu.identityservice.dto.response.PermissionResponse;
import huyvu.identityservice.dto.response.RoleResponse;
import huyvu.identityservice.model.Permission;
import huyvu.identityservice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

// do khai báo là spring nên có thể autowire
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions",ignore = true)
     Role toRole(RoleRequest roleRequest);

     RoleResponse toRoleResponse(Role role);
}

