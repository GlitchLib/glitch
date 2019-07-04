!!! caution
    Some part of the events is not done yet. This documentations shows only example, they might be changed in time.
    
!!! todo
    This page needs improvements. We will do this as soon as possible.
    
# Initialization Client

For starting initialization `GlitchClient` we need a `client_id` and `client_secret` to this important creation process. How to get them, you will find out [over here](/wiki/advanced-tutorials/getting-client-id-and-secret/).

```java
GlitchClient client = GlitchClient.builder()
            .clientId("<client_id>")
            .clientSecret("<client_secret>")
            .credentialStorage(new MyCredentialStorage()) // a custom implementation of CredentialStorage
            .defaultScopes(Scope.USER_READ)
            .build(); // or .buildAsync() when returns Mono<GlitchClient>
```

# Authorize User

`GlitchClient` provides `OAuth` client ready for implementation to your server. Examples below shows how to using them propertly.

## Getting from Storage

Everytime when you executing [`create()`](/docs/latest/all/glitch/auth/CredentialManager.html#create(java.lang.String,java.lang.String)) or [`buildFromCredentials()`](/docs/latest/all/glitch/auth/CredentialManager.html#buildFromCredentials(glitch.auth.UserCredential)) methods from [`CredentialManager`](/docs/latest/all/glitch/auth/CredentialManager.html) will automatically add your [`Credential`](/docs/latest/all/glitch/auth/objects/json/Credential.html) to credential [`Storage`](/docs/latest/all/glitch/auth/store/Storage.html).

### Getting user by name

<blockquote class="alert alert-danger" role="alert">
For getting users with ignore case or soemthing else, please take a look more details about <a href="https://www.tutorialspoint.com/java/java_regular_expressions.htm">Regular Expressions</a>
</blockquote>

```java
client.getCredentialManager().getCredentialStorage().getByLogin("^(?i:twitch)");
```

## Register a new Credential

To creating credential you need a `access_token` and `refresh_token`.

```java
client.getCredentialManager().buildFromCredentials("<access_token>", "<refresh_token>");
```

## Authorizing user

<blockquote class="alert alert-danger" role="alert">
This library support only <a href="https://dev.twitch.tv/docs/authentication/getting-tokens-oauth/#oauth-authorization-code-flow">Authorization Code Flow</a>
</blockquote>

To authorizing user via OAuth2 we need provide `base_url` of your service and `code` (from your query parameter) when it is authorization code for creating your credentials.

```java
client.getCredentialManager().create("<code>", "<base_url>");
```