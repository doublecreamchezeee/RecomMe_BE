package com.example.recomme_be.controller;


import com.example.recomme_be.dto.request.auth.UserUpdateRequest;
import com.example.recomme_be.dto.response.auth.UserInfoResponse;
import com.example.recomme_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /* Not allow anyone to get other user's info */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "email", required = false) String email) {
        if (userId == null && email == null) {
            return ResponseEntity.badRequest().build();
        }
        UserInfoResponse userInfo = userService.getUserInfo(userId, email);
        return ResponseEntity.ok(userInfo);
    }
    /* Not allow anyone to update other user's email */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateUserByEmail(@RequestParam("email") String email, @Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateByEmail(email, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyProfile(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getUserInfo(uid,null));
    }

    @PutMapping("/me")
    public ResponseEntity<HttpStatus> updateMyProfile(Authentication authentication, @Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        String uid = (String) authentication.getPrincipal();
        userService.updateByUid(uid, userUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
