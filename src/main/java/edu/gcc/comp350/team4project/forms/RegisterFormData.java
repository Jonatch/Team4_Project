package edu.gcc.comp350.team4project.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterFormData {
    @Size(min = 5, max = 250, message="Username must be at least 5 characters")
    private String username;

    @Size(min = 8, max = 250, message="Password must at least 8 characters")
    private String password;

    private String confirm_password;

    @NotEmpty(message = "Must select a year")
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
