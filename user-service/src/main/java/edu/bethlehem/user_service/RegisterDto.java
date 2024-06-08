package edu.bethlehem.user_service;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String password;
    private String role;
}