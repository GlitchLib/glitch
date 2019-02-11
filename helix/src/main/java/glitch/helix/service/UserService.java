package glitch.helix.service;

import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.*;
import glitch.helix.service.request.UserFollowRequest;
import glitch.service.AbstractHttpService;
import java.util.Arrays;
import javax.annotation.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserService extends AbstractHttpService {
    public UserService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public Flux<User> getUsersById(@Nullable Credential credential, Long... id) {
        HttpRequest users = Routes.get("/users").newRequest();

        if (credential != null) {
            if (checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ_EMAIL)) {
                users.header("Authorization", "Bearer " + credential.getAccessToken());
            }
        }

        Arrays.asList(Arrays.copyOf(id, 100))
                .forEach(uid -> users.queryParam("id", uid));

        return exchangeTo(users, Users.class).flatMapIterable(OrdinalList::getData);
    }

    public Flux<User> getUsersByLogin(@Nullable Credential credential, String... login) {
        HttpRequest users = Routes.get("/users").newRequest();

        if (credential != null) {
            if (checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ_EMAIL)) {
                users.header("Authorization", "Bearer " + credential.getAccessToken());
            }
        }

        Arrays.asList(Arrays.copyOf(login, 100))
                .forEach(l -> users.queryParam("login", l));

        return exchangeTo(users, Users.class).flatMapIterable(OrdinalList::getData);
    }

    public UserFollowRequest getUsersFollows(User user) {
        return new UserFollowRequest(http, user.getId(), null);
    }

    public UserFollowRequest getUsersFollowing(User user) {
        return new UserFollowRequest(http, null, user.getId());
    }

    public Mono<User> updateUser(Credential credential, String description) {
        if (checkRequiredScope(credential.getScopes(), GlitchScope.USER_EDIT)) {
            return exchangeTo(Routes.put("/users").newRequest()
                    .header("Authorization", "Bearer " + credential.getAccessToken())
                    .queryParam("description", description), Users.class)
                    .flatMapIterable(OrdinalList::getData).next();
        } else {
            return Mono.error(new ScopeIsMissingException(GlitchScope.USER_EDIT));
        }
    }

    public Flux<Extension> getUserExtensions(Credential credential) {
        if (checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ_BROADCAST)) {
            return exchangeTo(Routes.get("/users/extensions/list").newRequest()
                    .header("Authorization", "Bearer " + credential.getAccessToken()), Extensions.class)
                    .flatMapIterable(OrdinalList::getData);
        } else {
            return Flux.error(new ScopeIsMissingException(GlitchScope.USER_READ_BROADCAST));
        }
    }

    public Flux<InstalledExtension> getUserInstalledExtensions(Credential credential) {
        if (checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ_BROADCAST) || checkRequiredScope(credential.getScopes(), GlitchScope.USER_EDIT_BROADCAST)) {
            return exchangeTo(Routes.get("/users/extensions").newRequest()
                    .header("Authorization", "Bearer " + credential.getAccessToken()), InstalledExtensions.class)
                    .flatMapIterable(OrdinalList::getData);
        } else {
            return getUserInstalledExtensions(credential.getUserId());
        }
    }

    public Flux<InstalledExtension> getUserInstalledExtensions(Long id) {
        return exchangeTo(Routes.get("/users/extensions").newRequest()
                .queryParam("user_id", id), InstalledExtensions.class)
                .flatMapIterable(OrdinalList::getData);
    }

    // TODO
    public void updateUserExtension(Credential credential) {
        throw new UnsupportedOperationException("Updating User Extensions is not supported yet");
    }
}
