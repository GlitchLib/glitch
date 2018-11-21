package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.GlobalUserState;
import glitch.kraken.object.json.User;
import glitch.kraken.object.json.UserFollow;
import glitch.kraken.object.json.list.UserList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;

public class UserService extends AbstractHttpService {
    public UserService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Flux<User> getUser(String... users) {
        return getUsers(Arrays.asList(users));
    }

    public Flux<User> getUsers(Collection<String> login) {
        return exchange(get("/users", UserList.class).queryParam("login", String.join(",", login))).toFlux(UserList::getData);
    }

    public Mono<UserFollow> getFollow(User user, Channel channel) {
        return null;
    }

    public Mono<GlobalUserState> getChatUserState(User user) {
        return getChatUserState(user.getId());
    }

    public Mono<GlobalUserState> getChatUserState(Long id) {
        return null;
    }
}
