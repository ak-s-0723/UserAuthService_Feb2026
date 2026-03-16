package org.example.userauthservice_feb2026.services;

import org.example.userauthservice_feb2026.models.User;

public interface IAuthService {

    User signup(String name,String email,String password,String phoneNumber);

    User login(String email,String password);
}
