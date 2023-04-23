package com.alexon.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class AuthenticatorExt extends Authenticator{
    private String user = "";
    private String password = "";
    public AuthenticatorExt(String user, String password) {
        this.user = user;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password.toCharArray());
    }

}
