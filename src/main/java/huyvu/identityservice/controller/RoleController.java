package huyvu.identityservice.controller;


import huyvu.identityservice.dto.request.ApiResponse;
import huyvu.identityservice.dto.request.PermissionRequest;
import huyvu.identityservice.dto.request.RoleRequest;
import huyvu.identityservice.dto.response.PermissionResponse;
import huyvu.identityservice.dto.response.RoleResponse;
import huyvu.identityservice.service.PermisstionService;
import huyvu.identityservice.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @Slf4j
    @RestController
    @RequestMapping("/role")
    @RequiredArgsConstructor
    // tự động tạo contructor cho những biến có final
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public class RoleController {
        RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create (@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(roleRequest))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> delete(@RequestParam String role) {
        roleService.delete(role);

        return ApiResponse.<Void>builder()
                .build();

    }
}
