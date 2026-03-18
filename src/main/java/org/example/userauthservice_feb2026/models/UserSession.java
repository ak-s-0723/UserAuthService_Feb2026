package org.example.userauthservice_feb2026.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UserSession extends BaseModel {
    private String token;

    @ManyToOne
    private User user;
}

/*1                 1
Session          User
M                  1

M          :       1*/
