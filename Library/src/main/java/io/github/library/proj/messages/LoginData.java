package io.github.library.proj.messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
