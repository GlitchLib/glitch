package glitch.kraken.services;

import glitch.service.AbstractHttpService;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.ChatBadges;
import glitch.kraken.object.json.ChatRoom;
import glitch.kraken.object.json.Emote;
import glitch.kraken.object.json.list.ChatRooms;
import glitch.kraken.object.json.list.EmoteList;
import glitch.kraken.object.json.list.EmoteSets;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ChatService extends AbstractHttpService {
    public ChatService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<ChatBadges> getChatBadges(Long id) {
        return exchange(get(String.format("/chat/%s/badges", id), ChatBadges.class)).toMono();
    }

    public Mono<EmoteSets> getChatEmoteSets(Integer... ids) {
        return getChatEmoteSets(Arrays.asList(ids));
    }

    public Mono<EmoteSets> getChatEmoteSets(Collection<Integer> ids) {
        return exchange(get("/chat/emoticon_images", EmoteSets.class)
                .queryParam("emotesets", ids.stream().map(String::valueOf).collect(Collectors.joining(",")))).toMono();
    }

    public Flux<Emote> getAllEmoticons() {
        return exchange(get("/chat/emoticons", EmoteList.class)).toFlux(OrdinalList::getData);
    }

    public Flux<ChatRoom> getChatRooms(Long id) {
        return exchange(get(String.format("/chat/%s/rooms", id), ChatRooms.class)).toFlux(OrdinalList::getData);
    }
}
