package org.example.userauthservice_feb2026.services;

import org.example.userauthservice_feb2026.exceptions.PasswordMismatchException;
import org.example.userauthservice_feb2026.exceptions.UserAlreadyExistsException;
import org.example.userauthservice_feb2026.exceptions.UserNotSignedUpException;
import org.example.userauthservice_feb2026.models.Role;
import org.example.userauthservice_feb2026.models.Status;
import org.example.userauthservice_feb2026.models.User;
import org.example.userauthservice_feb2026.repos.RoleRepo;
import org.example.userauthservice_feb2026.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public User signup(String name, String email, String password, String phoneNumber) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent()) {
           throw new UserAlreadyExistsException("Please use different emailId");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);  //This is a problem - ToDo on Anurag to encode it
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setStatus(Status.ACTIVE);
        user.setCreatedAt(new Date());

        Role role;
        String non_admin = "NON_ADMIN";

        Optional<Role> roleOptional = roleRepo.findByValue(non_admin);
        if(roleOptional.isEmpty()) {
            role = new Role();
            role.setValue(non_admin);
            role.setStatus(Status.ACTIVE);
            role.setCreatedAt(new Date());
            roleRepo.save(role);
        } else {
            role = roleOptional.get();
        }

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepo.save(user);
        return user;
    }

    @Override
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isEmpty()) {
           throw new UserNotSignedUpException("Please signup first");
        }

        User user = optionalUser.get();
        if(!password.equals(user.getPassword())) {
            throw new PasswordMismatchException("Please check your password again");
        }

        //ToDo : Generate JWT by Anurag

        return user;
    }
}
