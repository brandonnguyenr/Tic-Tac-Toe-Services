package io.github.coreutils.proj.messages;

import lombok.Getter;

@Getter
public class LoginResponseData {
    private final LoginData data;
    private final boolean loginSuccess;
    private final String info;

    public LoginResponseData(LoginData data, boolean loginSuccess, String info) {
        this.data = data;
        this.loginSuccess = loginSuccess;
        this.info = info;
    }
}
