package com.fabiodm.api.discord;

import com.fabiodm.api.discord.model.Token;
import com.fabiodm.api.discord.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Optional;

public class JDiscordOAuth2 {

    private static final String DISCORD_API_AUTHORIZATION_URL = "https://discord.com/api/v10/oauth2/authorize";
    private static final String DISCORD_API_TOKEN_URL = "https://discord.com/api/v10/oauth2/token";
    private static final String DISCORD_API_USERS_URL = "https://discord.com/api/v10/users/@me";

    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    protected JDiscordOAuth2(final String clientId, final String clientSecret, final String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public String generateAuthorizationUrl() {
        return this.generateAuthorizationUrl(null);
    }

    public String generateAuthorizationUrl(final String state) {
        return DISCORD_API_AUTHORIZATION_URL
                + "?client_id=" + this.clientId
                + "&redirect_uri=" + this.redirectUri
                + "&response_type=code"
                + "&scope=identify"
                + (state != null ? "&state=" + state : "");
    }

    public Optional<Token> getToken(final String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        final String basicAuth = Base64.getEncoder().encodeToString((this.clientId + ":" + this.clientSecret).getBytes());
        final String body = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + this.redirectUri;

        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(DISCORD_API_TOKEN_URL))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + basicAuth)
                .build();

        return Optional.ofNullable(this.sendRequest(httpRequest, Token.class));
    }

    public Optional<User> getUser(final String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return Optional.empty();
        }

        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(DISCORD_API_USERS_URL))
                .GET()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return Optional.ofNullable(this.sendRequest(httpRequest, User.class));
    }

    private <T> T sendRequest(final HttpRequest httpRequest, final Class<T> resultClass) {
        final HttpResponse<String> httpResponse = this.httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).join();
        if (httpResponse.statusCode() != 200) {
            return null;
        }

        return this.gson.fromJson(httpResponse.body(), resultClass);
    }
}
