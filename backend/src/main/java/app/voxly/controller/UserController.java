package app.voxly.controller;

import app.voxly.model.dto.request.UpdateProfileRequest;
import app.voxly.model.dto.response.UserResponse;
import app.voxly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                               @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<UserResponse> getStats(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}
