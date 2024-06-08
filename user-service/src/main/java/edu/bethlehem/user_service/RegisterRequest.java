package edu.bethlehem.user_service;



import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterRequest {


    private String firstName;


    private String lastName;

    @Column(unique = true)

    private String username;


    private String email;




    private String password;

    private String bio;



    private String phoneNumber;

    private String fieldOfWork;


}
