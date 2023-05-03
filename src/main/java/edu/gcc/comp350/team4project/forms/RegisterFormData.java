package edu.gcc.comp350.team4project.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterFormData {
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 5, max = 250, message="username has to be at least 5 characters long")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message="password has to be at least 8 characters long")
    private String password;

    @NotEmpty(message = "Confirm password cannot be empty")
    @Size(min = 8, message="password has to be at least 8 characters long")
    private String confirm_password;

    private String year;

    public RegisterFormData(){
        this.username = "";
        this.password = "";
        this.confirm_password = "";
        this.year = "";
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
