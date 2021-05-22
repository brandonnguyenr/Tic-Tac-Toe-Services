package io.github.coreutils.proj.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateData {
    private String  username;
    private String  password;
    private String  firstName;
    private String  lastName;
    private String  newUsername;
    private String  newPassword;
    private String  isDeleted;
}
