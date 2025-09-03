package com.daviddai.blog.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private Keycloak keycloakAdmin;
    private RealmResource realmResource;

    @PostConstruct
    public void init() {
        this.keycloakAdmin = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .build();
        this.realmResource = keycloakAdmin.realm(realm);
    }

    public UserRepresentation getUserById(String userId) {
        return realmResource.users().get(userId).toRepresentation();
    }

    public void updateUserEmail(String userId, String newEmail) {
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setEmail(newEmail);
        user.setEmailVerified(false);
        userResource.update(user);
    }

    public void updateUserUsername(String userId, String newUsername) {
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setUsername(newUsername);
        userResource.update(user);
    }

    public void triggerEmailVerification(String userId) {
        realmResource.users().get(userId).executeActionsEmail(List.of("VERIFY_EMAIL"));
    }

    public void enableUser(String userId) {
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(true);
        userResource.update(user);
    }

    public void disableUser(String userId) {
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(false);
        userResource.update(user);
    }

    public void deleteUser(String userId) {
        realmResource.users().delete(userId);
    }

    public List<UserRepresentation> searchUsers(String query, int first, int max) {
        return realmResource.users().search(query, first, max);
    }

    public boolean isUsernameEditAllowed() {
        return realmResource.toRepresentation().isEditUsernameAllowed();
    }

}
