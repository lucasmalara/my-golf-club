package com.mygolfclub.model.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserModel {

    @NotNull(message = "Username is required.")
    @Size(
            min = 1,
            max = 50,
            message = "Username must contain between 1 and 50 characters."
    )
    private String username;

    @NotNull(message = "Password is required.")
    @Length(
            min = 3,
            message = "Password must be longer than 3 characters."
    )
    private String password;

    private String role;
}
