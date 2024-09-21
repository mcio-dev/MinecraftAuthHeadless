package net.lenni0451.commons.httpclient.content.impl;

import lombok.SneakyThrows;
import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.content.HttpContent;
import net.lenni0451.commons.httpclient.utils.URLCoder;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class URLEncodedFormContent extends HttpContent {

    private final List<FormEntry> entries;
    private final Charset charset;

    public URLEncodedFormContent() {
        this(StandardCharsets.UTF_8);
    }

    public URLEncodedFormContent(final Map<String, String> entries) {
        this(entries, StandardCharsets.UTF_8);
    }

    public URLEncodedFormContent(final Charset charset) {
        super(ContentTypes.APPLICATION_FORM_URLENCODED);
        this.entries = new ArrayList<>();
        this.charset = charset;
    }

    public URLEncodedFormContent(final Map<String, String> entries, final Charset charset) {
        super(ContentTypes.APPLICATION_FORM_URLENCODED);
        this.entries = entries.entrySet().stream().map(e -> new FormEntry(e.getKey(), e.getValue())).collect(Collectors.toList());
        this.charset = charset;
    }

    /**
     * Add a new entry to the form.
     *
     * @param key   The key
     * @param value The value
     * @return This instance for chaining
     */
    public URLEncodedFormContent put(final String key, final String value) {
        this.entries.add(new FormEntry(key, value));
        this.content = null;
        return this;
    }

    @Override
    @SneakyThrows
    public int getContentLength() {
        return this.getAsBytes().length;
    }

    @Override
    protected byte @NotNull [] compute() {
        StringBuilder builder = new StringBuilder();
        for (FormEntry entry : this.entries) {
            if (builder.length() != 0) builder.append("&");
            builder
                    .append(entry.encodeKey(this.charset))
                    .append("=")
                    .append(entry.encodeValue(this.charset));
        }
        return builder.toString().getBytes(this.charset);
    }

    private static class FormEntry {
        private final String key;
        private final String value;

        private FormEntry(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        private String getKey() {
            return this.key;
        }

        private String encodeKey(Charset charset) {
            return URLCoder.encode(this.key, charset);
        }

        private String getValue() {
            return this.value;
        }

        private String encodeValue(Charset charset) {
            return URLCoder.encode(this.value, charset);
        }
    }

}
