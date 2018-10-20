package glitch.auth;

import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.core.utils.http.ResponseException;
import javax.ws.rs.*;

interface CredentialAPI {
    @GET @Path("/validate")
    Validate validate(@HeaderParam("Authorization") String accessToken) throws ResponseException;

    @POST @Path("/token?grant_type=authorization_code")
    AccessToken getAccessToken(
            @QueryParam("client_id") String clientId,
            @QueryParam("client_secret") String clientSecret,
            @QueryParam("code") String authorizationCode,
            @QueryParam("redirect_uri") String redirectUri
    ) throws ResponseException;

    @POST @Path("/token?grant_type=refresh_token")
    AccessToken refreshToken(
            @QueryParam("client_id") String clientId,
            @QueryParam("client_secret") String clientSecret,
            @QueryParam("refresh_token") String refreshToken
    ) throws ResponseException;

    @POST @QueryParam("/revoke")
    void revoke(@QueryParam("client_id") String clientId, @QueryParam("token") String accessToken) throws ResponseException;
}
