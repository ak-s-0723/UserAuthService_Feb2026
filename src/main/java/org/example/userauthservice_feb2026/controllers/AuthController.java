package org.example.userauthservice_feb2026.controllers;

import org.example.userauthservice_feb2026.dtos.LoginRequestDto;
import org.example.userauthservice_feb2026.dtos.RoleDto;
import org.example.userauthservice_feb2026.dtos.SignupRequestDto;
import org.example.userauthservice_feb2026.dtos.UserDto;
import org.example.userauthservice_feb2026.models.Role;
import org.example.userauthservice_feb2026.models.User;
import org.example.userauthservice_feb2026.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    //Signup
    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        User user  = authService.signup(signupRequestDto.getName(), signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getPhoneNumber());
        return from(user);
    }

    //login
    //UserDto is going to change after jwt generation
    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto) {
       User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
       return from(user);
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        List<RoleDto> roleDtoList = new ArrayList<>();
        for(Role role : user.getRoles()) {
            RoleDto roleDto = new RoleDto();
            roleDto.setValue(role.getValue());
            roleDtoList.add(roleDto);
        }
        userDto.setRoles(roleDtoList);
        return userDto;
    }
}
