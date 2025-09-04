package huyvu.identityservice.service;


import huyvu.identityservice.dto.request.PermissionRequest;
import huyvu.identityservice.dto.response.PermissionResponse;
import huyvu.identityservice.mapper.PermissionMapper;
import huyvu.identityservice.model.Permission;
import huyvu.identityservice.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PermisstionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {

        List<Permission> permissions = permissionRepository.findAll();

        return permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void detele(String permissionName){
        permissionRepository.deleteById(permissionName);

    }
}
