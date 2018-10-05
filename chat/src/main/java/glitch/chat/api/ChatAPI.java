package glitch.chat.api;

import feign.Param;
import glitch.auth.Credential;
import glitch.chat.api.json.ChatRoomList;
import glitch.chat.api.json.Editors;
import glitch.chat.api.json.GlobalUserState;
import glitch.core.api.json.expanders.CredentialTokenExpander;
import io.reactivex.Single;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface ChatAPI {
    default Single<GlobalUserState> getGlobalUserState(Credential credential) {
        return getGlobalUserState(credential.getUserId());
    }

    @GET
    @Path("/users/{id}/chat")
    Single<GlobalUserState> getGlobalUserState(@PathParam("id") Long userId);

    default Single<ChatRoomList> getChatRooms(Credential credential) {
        return getChatRooms(credential.getUserId(), credential);
    }

    @GET
    @Path("/chat/{id}/rooms")
    @HeaderParam("Authorization: OAuth {token}")
    Single<ChatRoomList> getChatRooms(@Param("id") Long channelId, @Param(value = "token", expander = CredentialTokenExpander.class) Credential credential);

    default Single<Editors> getChannelEditors(Credential credential) {
        return getChannelEditors(credential.getUserId(), credential);
    }

    @GET
    @Path("/channels/{id}/editors")
    @HeaderParam("Authorization: OAuth {token}")
    Single<Editors> getChannelEditors(@Param("id") Long channelId, @Param(value = "token", expander = CredentialTokenExpander.class) Credential credential);
}
