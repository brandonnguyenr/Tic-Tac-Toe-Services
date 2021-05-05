package io.github.library.proj.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginData {
    private String username;
    private String firstName;
    private String lastName;

    public LoginData(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
