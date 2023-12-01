package com.fabiodm.api.discord;

public class JDiscordOAuth2Builder {

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public JDiscordOAuth2Builder setClientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    public JDiscordOAuth2Builder setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public JDiscordOAuth2Builder setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public JDiscordOAuth2 build() {
        if (this.clientId == null || this.clientId.isBlank()) {
            throw new IllegalArgumentException("[!] CLIENT_ID cannot be null or blank");
        }

        if (this.clientSecret == null || this.clientSecret.isBlank()) {
            throw new IllegalArgumentException("[!] CLIENT_SECRET cannot be null or blank");
        }

        if (this.redirectUri == null || this.redirectUri.isBlank()) {
            throw new IllegalArgumentException("[!] REDIRECT_URI cannot be null or blank");
        }

        return new JDiscordOAuth2(this.clientId, this.clientSecret, this.redirectUri);
    }
}
