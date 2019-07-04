!!! caution
    Some part of the events is not done yet. This documentations shows only example, they might be changed in time.
    
!!! todo
    This page needs improvements. We will do this as soon as possible.
    

# Getting Started

Before start we need initialize [`client`](../client) first at all. After initialization we can now getting into the [next step](#initialize-api)

## Initialize API

```java
GlitchKraken kraken = GlitchKraken.create(client);
// same way with Helix
GlitchHelix helix = GlitchHelix.create(client);
```

## What's next?

All services are be separated to specific API type, so **better watch out** which object you are importing.
Each services are be called using example below:

```java
kraken.use(ChatService.class);
// same way with Helix
helix.use(UserService.class);
```

All endpoints like this will returns `Mono` object to handling into reactive response.

### Get user

To getting Twitch user we need use `UserService` (each one are be separated to specific API)

> For e.g. We wana find `twitch` user.

#### Poseidon's way (Kraken) 

```java
kraken.use(UserService.class).flatMapMany(service -> service.getUsers("twitch")).next() // gets first element
```

we can use multiple usernames like this:

```java
kraken.use(UserService.class).flatMapMany(service -> service.getUsers("twitch", "summit1g", "izakooo", "ninja"))
```

#### Way with Helix

First parameter `#getUserByLogin()` is always a `Credential`.
If we want expose email address we need a Authorize user with `Scope.USER_READ_EMAIL`.
Otherwise `User#getEmail()` will always returns `null` if we not provide `Credential` with specific mentioned `Scope`.

```java
helix.use(UserService.class).flatMapMany(service -> service.getUsersByLogin(credential, "twitch")).next() // gets first element
```

we can use multiple usernames like this:

```java
helix.use(UserService.class).flatMapMany(service -> service
          .getUsersByLogin(null, "twitch", "summit1g", "izakooo", "ninja")) // if we don't needs their e-mails just add null on the first parameter
```


### Getting User using his ID

Same rules like aboved, but you provides `Long` id's not `String`. To formatting `Long` data use [`Long#parseLong(String)`](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html#parseLong-java.lang.String-)

```java fct_label="Kraken"
kraken.use(UserService.class).flatMap(service -> service.getUserById(12826))
```

```java fct_label="Helix"
helix.use(UserService.class).flatMapMany(service -> service
          .getUsersByLogin(null, 12826, 26490481, 36717908, 19571641))
```