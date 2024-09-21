package net.lenni0451.commons.httpclient.proxy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class ProxyHandler {

    private ProxyType proxyType;
    private SocketAddress address;
    private String username;
    private String password;

    public ProxyHandler() {
    }

    public ProxyHandler(final ProxyType proxyType, final String host, final int port) {
        this(proxyType, host, port, null, null);
    }

    public ProxyHandler(final ProxyType proxyType, final String host, final int port, @Nullable final String username, @Nullable final String password) {
        this(proxyType, new InetSocketAddress(host, port), username, password);
    }

    public ProxyHandler(final ProxyType proxyType, final SocketAddress address) {
        this(proxyType, address, null, null);
    }

    public ProxyHandler(final ProxyType proxyType, final SocketAddress address, @Nullable final String username, @Nullable final String password) {
        this.proxyType = proxyType;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    /**
     * Set the proxy to use.
     *
     * @param type The type of the proxy
     * @param host The host of the proxy
     * @param port The port of the proxy
     * @return This instance for chaining
     */
    public ProxyHandler setProxy(final ProxyType type, final String host, final int port) {
        return this.setProxy(type, new InetSocketAddress(host, port));
    }

    /**
     * Set the proxy to use.
     *
     * @param type    The type of the proxy
     * @param address The address of the proxy
     * @return This instance for chaining
     */
    public ProxyHandler setProxy(final ProxyType type, final SocketAddress address) {
        this.proxyType = type;
        this.address = address;
        return this;
    }

    /**
     * Unset the proxy.
     *
     * @return This instance for chaining
     */
    public ProxyHandler unsetProxy() {
        this.proxyType = null;
        this.address = null;
        return this;
    }

    /**
     * @return If the proxy is set
     */
    public boolean isProxySet() {
        return this.proxyType != null && this.address != null;
    }

    /**
     * @return The type of the proxy
     */
    @Nullable
    public ProxyType getProxyType() {
        return this.proxyType;
    }

    /**
     * Set the type of the proxy.
     *
     * @param type The type of the proxy
     * @return This instance for chaining
     */
    public ProxyHandler setProxyType(@NotNull final ProxyType type) {
        this.proxyType = type;
        return this;
    }

    /**
     * @return The proxy address
     */
    @Nullable
    public SocketAddress getAddress() {
        return this.address;
    }

    /**
     * Set the proxy address.
     *
     * @param address The proxy address
     * @return This instance for chaining
     */
    public ProxyHandler setAddress(@NotNull final SocketAddress address) {
        this.address = address;
        return this;
    }

    /**
     * @return If the authentication is set
     */
    public boolean isAuthenticationSet() {
        return this.username != null && this.password != null;
    }

    /**
     * @return The username for the proxy
     */
    @Nullable
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the username for the proxy.
     *
     * @param username The username for the proxy
     * @return This instance for chaining
     */
    public ProxyHandler setUsername(@Nullable final String username) {
        this.username = username;
        return this;
    }

    /**
     * @return The password for the proxy
     */
    @Nullable
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the password for the proxy.
     *
     * @param password The password for the proxy
     * @return This instance for chaining
     */
    public ProxyHandler setPassword(@Nullable final String password) {
        this.password = password;
        return this;
    }

    /**
     * @return The SingleProxySelector for this proxy
     * @throws IllegalStateException If the proxy is not set
     */
    public SingleProxySelector getProxySelector() {
        if (!this.isProxySet()) throw new IllegalStateException("Proxy is not set");
        return new SingleProxySelector(this.toJavaProxy(), this.username, this.password);
    }

    /**
     * @return The SingleProxyAuthenticator for this proxy
     * @throws IllegalStateException If the proxy or the username/password is not set
     */
    public SingleProxyAuthenticator getProxyAuthenticator() {
        if (!this.isProxySet()) throw new IllegalStateException("Proxy is not set");
        if (!this.isAuthenticationSet()) throw new IllegalStateException("Username or password is not set");
        return new SingleProxyAuthenticator(this.username, this.password);
    }

    /**
     * Create a {@link Proxy} object from this proxy.<br>
     * The {@link Proxy} might not support all proxy types.
     *
     * @return The created {@link Proxy} object
     */
    public Proxy toJavaProxy() {
        switch (this.proxyType) {
            case HTTP:
                return new Proxy(Proxy.Type.HTTP, this.address);
            case SOCKS4:
                try {
                    Class<?> clazz = Class.forName("sun.net.SocksProxy");
                    Method createMethod = clazz.getDeclaredMethod("create", SocketAddress.class, int.class);
                    return (Proxy) createMethod.invoke(null, this.address, 4);
                } catch (Throwable t) {
                    throw new UnsupportedOperationException("SOCKS4 proxy type is not supported", t);
                }
            case SOCKS5:
                return new Proxy(Proxy.Type.SOCKS, this.address);
            default:
                throw new IllegalStateException("Unknown proxy type: " + this.proxyType.name());
        }
    }

}
