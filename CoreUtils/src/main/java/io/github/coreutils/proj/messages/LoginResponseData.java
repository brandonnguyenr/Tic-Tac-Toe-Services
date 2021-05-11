package io.github.coreutils.proj.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseData {
    private final LoginData data;
    private final boolean loginSuccess;
    private final String info;
}
