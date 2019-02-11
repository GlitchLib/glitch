package glitch.kraken.services;

import glitch.api.http.Routes;
import glitch.api.http.Unofficial;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.*;
import glitch.kraken.object.json.collections.ChatRooms;
import glitch.kraken.object.json.collections.EmoteSets;
import glitch.kraken.object.json.collections.Emotes;
import glitch.service.AbstractHttpService;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ChatService extends AbstractHttpService {
    public ChatService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<ChatBadges> getChatBadges(Long id) {
        return exchangeTo(Routes.get("/chat/%s/badges").newRequest(id), ChatBadges.class);
    }

    public Mono<EmoteSets> getChatEmoteSets(Integer... ids) {
        return getChatEmoteSets(Arrays.asList(ids));
    }

    public Mono<EmoteSets> getChatEmoteSets(Collection<Integer> ids) {
        return exchangeTo(Routes.get("/chat/emoticon_images").newRequest()
                .queryParam("emotesets", ids.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","))), EmoteSets.class);
    }

    public Flux<Emote> getAllEmoticons() {
        return exchangeTo(Routes.get("/chat/emoticons").newRequest(), Emotes.class)
                .flatMapIterable(OrdinalList::getData);
    }

    public Flux<ChatRoom> getChatRooms(Long id) {
        return exchangeTo(Routes.get("/chat/%s/rooms").newRequest(id), ChatRooms.class)
                .flatMapIterable(OrdinalList::getData);
    }

    @Unofficial
    public Mono<GlobalUserState> getChatUserState(User user) {
        return getChatUserState(user.getId());
    }

    @Unofficial
    public Mono<GlobalUserState> getChatUserState(Long id) {
        return exchangeTo(Routes.get("/users/%s/chat").newRequest(id), GlobalUserState.class);
    }
}
