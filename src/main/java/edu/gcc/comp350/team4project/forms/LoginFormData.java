package edu.gcc.comp350.team4project.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginFormData {
    @NotEmpty(message = "Username cannot be empty")
    @Size(max = 250, message="Attempted username too long")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 250, message="Attempted password too long")
    private String password;

    public LoginFormData(){
        this.username = "";
        this.password = "";
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
