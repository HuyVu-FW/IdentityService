package huyvu.identityservice.controller;


import huyvu.identityservice.dto.request.ApiResponse;
import huyvu.identityservice.dto.request.PermissionRequest;
import huyvu.identityservice.dto.response.PermissionResponse;
import huyvu.identityservice.service.PermisstionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
// tự động tạo contructor cho những biến có final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermisstionController {
    PermisstionService permisstionService;

    @PostMapping
    ApiResponse<PermissionResponse> create (@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permisstionService.create(permissionRequest))
                .build();
    }
    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permisstionService.getAll())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> delete(@RequestParam String permissionId) {
        permisstionService.detele(permissionId);

        return ApiResponse.<Void>builder()
                .build();

    }
}
