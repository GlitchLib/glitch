package glitch.auth;

import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.core.utils.http.HeaderValue;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface CredentialAPI {
    @GET("/validate")
    Single<Validate> validate(@Header("Authorization") @HeaderValue("OAuth") Credential credential);

    @POST("/token?grant_type=authorization_code")
    Single<AccessToken> getAccessToken(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("code") String authorizationCode,
            @Query("redirect_uri") String redirectUri
    );

    @POST("/token?grant_type=refresh_token")
    Single<AccessToken> refreshToken(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("refresh_token") Credential credential
    );

    @POST("/revoke")
    Single<Void> revoke(@Query("client_id") String clientId, @Query("token") Credential credential);
}
