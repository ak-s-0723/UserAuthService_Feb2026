package org.example.userauthservice_feb2026.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_feb2026.exceptions.PasswordMismatchException;
import org.example.userauthservice_feb2026.exceptions.UserAlreadyExistsException;
import org.example.userauthservice_feb2026.exceptions.UserNotSignedUpException;
import org.example.userauthservice_feb2026.models.Role;
import org.example.userauthservice_feb2026.models.Status;
import org.example.userauthservice_feb2026.models.User;
import org.example.userauthservice_feb2026.models.UserSession;
import org.example.userauthservice_feb2026.repos.RoleRepo;
import org.example.userauthservice_feb2026.repos.UserRepo;
import org.example.userauthservice_feb2026.repos.UserSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private UserSessionRepo userSessionRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String name, String email, String password, String phoneNumber) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent()) {
           throw new UserAlreadyExistsException("Please use different emailId");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
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
    public Pair<User,String> login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isEmpty()) {
           throw new UserNotSignedUpException("Please signup first");
        }

        User user = optionalUser.get();
        //if(!password.equals(user.getPassword())) {
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())) {
            throw new PasswordMismatchException("Please check your password again");
        }

        //ToDo : Generate JWT by Anurag
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        claims.put("issuer","scaler");
        Long currentTime = System.currentTimeMillis();
        claims.put("iat",currentTime);
        claims.put("exp",currentTime+100000);

        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setToken(token);
        userSession.setStatus(Status.ACTIVE);
        userSession.setCreatedAt(new Date());
        userSession.setLastUpdatedAt(new Date());
        userSessionRepo.save(userSession);

        return new Pair<>(user,token);
    }
}
