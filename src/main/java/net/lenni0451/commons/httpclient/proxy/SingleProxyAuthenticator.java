package net.lenni0451.commons.httpclient.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class SingleProxyAuthenticator extends Authenticator {

    private final PasswordAuthentication passwordAuthentication;

    public SingleProxyAuthenticator(final String username, final String password) {
        this.passwordAuthentication = new PasswordAuthentication(username, password.toCharArray());
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return this.passwordAuthentication;
    }

}
