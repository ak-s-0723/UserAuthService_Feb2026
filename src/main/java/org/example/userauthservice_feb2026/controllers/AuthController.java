package org.example.userauthservice_feb2026.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_feb2026.dtos.LoginRequestDto;
import org.example.userauthservice_feb2026.dtos.RoleDto;
import org.example.userauthservice_feb2026.dtos.SignupRequestDto;
import org.example.userauthservice_feb2026.dtos.UserDto;
import org.example.userauthservice_feb2026.models.Role;
import org.example.userauthservice_feb2026.models.User;
import org.example.userauthservice_feb2026.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Pair<User,String> response = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        User user = response.a;
        String token = response.b;

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);
        headers.add("Generated-By","Anurag Khanna");

       return new ResponseEntity<>(from(user),headers, HttpStatus.OK);
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
       // userDto.setRoles(roleDtoList);
        return userDto;
    }
}
