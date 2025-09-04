package huyvu.identityservice.service;


import huyvu.identityservice.dto.request.RoleRequest;
import huyvu.identityservice.dto.response.RoleResponse;
import huyvu.identityservice.mapper.RoleMapper;
import huyvu.identityservice.repository.PermissionRepository;
import huyvu.identityservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
     RoleRepository roleRepository;
     PermissionRepository permissionRepository;
     RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);
        var permissionRepositoryAllB = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissionRepositoryAllB));

        return roleMapper.toRoleResponse(roleRepository.save(role));


    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();

    }

    public void delete(String name){
      roleRepository.deleteById(name);
    }


}
