package net.lenni0451.commons.httpclient.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HttpHeader {

    @NotNull
    private final String name;
    @NotNull
    private final String value;

    public HttpHeader(@NotNull final String name, @NotNull final String value) {
        this.name = name;
        this.value = value;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "HttpHeader{" +
                "name='" + this.name + '\'' +
                ", value='" + this.value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeader that = (HttpHeader) o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

}
