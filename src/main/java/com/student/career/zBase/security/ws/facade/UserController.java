package com.student.career.zBase.security.ws.facade;

import com.student.career.zBase.security.bean.User;
import com.student.career.zBase.security.dao.criteria.UserCriteria;
import com.student.career.zBase.security.service.facade.UserService;
import com.student.career.zBase.security.ws.dto.UserDto;
import com.student.career.zBase.security.ws.transformer.UserTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserTransformer userTransformer;

    public UserController(UserService userService, UserTransformer userTransformer) {
        this.userService = userService;
        this.userTransformer = userTransformer;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtos = userTransformer.toDto(userService.findAll());
        return ResponseEntity.ok(userDtos);
    }


    @PostMapping("/")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto)  {
        User user = userTransformer.toEntity(userDto);
        user = userService.saveWithAssociatedEmployee(user);
        return new ResponseEntity<>(userTransformer.toDto(user), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto) {
        User user = userTransformer.toEntity(userDto);
        user = userService.update(user);
        return new ResponseEntity<>(userTransformer.toDto(user), HttpStatus.OK);
    }

    @PatchMapping("/update-password/email/{email}/new-password/{newPassword}")
    public ResponseEntity<UserDto> updatePassword(@PathVariable String email, @PathVariable String newPassword) {
        User user = userService.updatePassword(email, newPassword);
        return new ResponseEntity<>(userTransformer.toDto(user), HttpStatus.ACCEPTED);
    }

    @PatchMapping("/update-password/email/{email}/current-password/{currentPassword}/new-password/{newPassword}")
    public ResponseEntity<UserDto> updatePasswordBasedOnCurrentPassword(@PathVariable String email, @PathVariable String currentPassword, @PathVariable String newPassword) {
        User user = userService.updatePasswordBasedOnCurrentPassword(email, currentPassword, newPassword);
        return new ResponseEntity<>(userTransformer.toDto(user), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable String id) {
        UserDto userDto = userTransformer.toDto(userService.findById(id));
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/authenticated-user")
    public ResponseEntity<UserDto> loadAuthenticatedUser() {
        UserDto userDto = userTransformer.toDto(userService.loadAuthenticatedUser());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/find-by-criteria")
    public ResponseEntity<List<UserDto>> findByCriteria(@RequestBody UserCriteria userCriteria) {
        List<User> users = userService.findByCriteria(userCriteria);
        List<UserDto> userDtos = userTransformer.toDto(users);
        return ResponseEntity.ok(userDtos);
    }
}
