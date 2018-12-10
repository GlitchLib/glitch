package glitch.helix.service;

import com.google.common.collect.ImmutableList;
import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.Extension;
import glitch.helix.object.json.InstalledExtension;
import glitch.helix.object.json.User;
import glitch.helix.object.json.list.Extensions;
import glitch.helix.object.json.list.InstalledExtensions;
import glitch.helix.service.request.UserFollowRequest;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class UserService extends AbstractHttpService {
    public UserService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public Flux<User> getUsersById(@Nullable Credential credential, Long... id) {
        HttpRequest<UserList> users = get("/users", UserList.class);

        if (credential != null) {
            if (checkRequiredScope(credential.getScopes(), Scope.USER_READ_EMAIL)) {
                users.header("Authorization", "Bearer " + credential.getAccessToken());
            }
        }

        Arrays.asList(Arrays.copyOf(id, 100))
                .forEach(uid -> users.queryParam("id", uid));

        return exchange(users).toFlux(OrdinalList::getData);
    }

    public Flux<User> getUsersByLogin(@Nullable Credential credential, String... login) {
        List<String> logins = Arrays.asList(Arrays.copyOf(login, 100));

        HttpRequest<UserList> users = get("/users", UserList.class);

        if (credential != null) {
            if (checkRequiredScope(credential.getScopes(), Scope.USER_READ_EMAIL)) {
                users.header("Authorization", "Bearer " + credential.getAccessToken());
            }
        }

        Arrays.asList(Arrays.copyOf(login, 100))
                .forEach(l -> users.queryParam("login", l));

        return exchange(users).toFlux(OrdinalList::getData);
    }

    public UserFollowRequest getUsersFollows(User user) {
        return new UserFollowRequest(http, user.getId(), null);
    }

    public UserFollowRequest getUsersFollowing(User user) {
        return new UserFollowRequest(http, null, user.getId());
    }

    public Mono<User> updateUser(Credential credential, String description) {
        if (checkRequiredScope(credential.getScopes(), Scope.USER_EDIT)) {
            return exchange(put("/users", UserList.class)
                    .header("Authorization", "Bearer " + credential.getAccessToken())
                    .queryParam("description", description))
                    .toFlux(OrdinalList::getData).next();
        } else {
            return Mono.error(new ScopeIsMissingException(Scope.USER_EDIT));
        }
    }

    public Flux<Extension> getUserExtensions(Credential credential) {
        if (checkRequiredScope(credential.getScopes(), Scope.USER_READ_BROADCAST)) {
            return exchange(get("/users/extensions/list", Extensions.class)
                    .header("Authorization", "Bearer " + credential.getAccessToken()))
                    .toFlux(OrdinalList::getData);
        } else {
            return Flux.error(new ScopeIsMissingException(Scope.USER_READ_BROADCAST));
        }
    }

    public Flux<InstalledExtension> getUserInstalledExtensions(Credential credential) {
        if (checkRequiredScope(credential.getScopes(), Scope.USER_READ_BROADCAST) || checkRequiredScope(credential.getScopes(), Scope.USER_EDIT_BROADCAST)) {
            return exchange(get("/users/extensions", InstalledExtensions.class)
                    .header("Authorization", "Bearer " + credential.getAccessToken()))
                    .toFlux(OrdinalList::getData);
        } else {
            return getUserInstalledExtensions(credential.getUserId());
        }
    }

    public Flux<InstalledExtension> getUserInstalledExtensions(Long id) {
        return exchange(get("/users/extensions", InstalledExtensions.class)
                .queryParam("user_id", id))
                .toFlux(OrdinalList::getData);
    }

    // TODO
    public void updateUserExtension(Credential credential) {
        throw new UnsupportedOperationException("Updating User Extensions is not supported yet");
    }

    @Data
    private static class UserList implements OrdinalList<User> {
        private final ImmutableList<User> data;
    }
}
