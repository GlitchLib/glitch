package glitch.chat.object.entities;

import glitch.chat.GlitchChat;
import glitch.kraken.object.json.GlobalUserState;
import glitch.kraken.services.UserService;
import lombok.Getter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Getter
public class UserEntity implements AbstractEntity<GlobalUserState> {
    private final GlitchChat client;
    private final String username;
    private final Mono<GlobalUserState> data;

    public UserEntity(GlitchChat client, String username) {
        this.client = client;
        this.username = username;
        this.data = (getClient().getApi() != null) ? getClient().getApi()
                .use(UserService.class).zipWhen(service -> service.getUser(username).next())
                .flatMap(tuple -> tuple.getT1().getChatUserState(tuple.getT2())) :
                Mono.empty();
    }

    public Mono<Void> send(Publisher<String> message) {
        return client.whisper(this, message);
    }
}
