package com.fabiodm.api.discord;

import com.fabiodm.api.discord.model.Token;
import com.fabiodm.api.discord.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

class JDiscordOAuth2Test {

    private static JDiscordOAuth2 jDiscordOAuth2;

    @BeforeAll
    static void beforeAll() {
        jDiscordOAuth2 = new JDiscordOAuth2Builder()
                .setClientId("client_id")
                .setClientSecret("client_secret")
                .setRedirectUri("redirect_uri")
                .build();
    }

    @Test
    void generateAuthorizationUrl() {
        Assertions.assertNotNull(jDiscordOAuth2.generateAuthorizationUrl());
    }

    @Test
    void generateAuthorizationUrlWithState() {
        Assertions.assertNotNull(jDiscordOAuth2.generateAuthorizationUrl(UUID.randomUUID().toString()));
    }

    @Test
    void getTokenWithNullCode() {
        final Optional<Token> token = jDiscordOAuth2.getToken(null);
        Assertions.assertTrue(token.isEmpty());
    }

    @Test
    void getTokenWithEmptyCode() {
        final Optional<Token> token = jDiscordOAuth2.getToken("");
        Assertions.assertTrue(token.isEmpty());
    }

    @Test
    void getTokenWithInvalidCode() {
        final Optional<Token> token = jDiscordOAuth2.getToken("invalid");
        Assertions.assertTrue(token.isEmpty());
    }

    @Test
    void getUserWithNullToken() {
        final Optional<User> user = jDiscordOAuth2.getUser(null);
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void getUserWithEmptyToken() {
        final Optional<User> user = jDiscordOAuth2.getUser("");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void getUserWithInvalidToken() {
        final Optional<User> user = jDiscordOAuth2.getUser("invalid");
        Assertions.assertTrue(user.isEmpty());
    }
}