package io.github.coreutils.proj.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LoginData {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
}
