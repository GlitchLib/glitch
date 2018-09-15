# Glitch Core Package

This is a core package maintained for all modules.


# Modules

- [Auth](../auth)
- [Chat](../chat)
- [Kraken](../kraken)
- [Helix](../helix)
- [PubSub](../pubsub)

# TODO

- [ ] Twitch Credentials
  - [x] OAuth2 with Validation
  - [ ] Credential Storage
    - [ ] [JPA / Hibernate](http://hibernate.org/)
    - [ ] [JDBI](https://jdbi.org/)
    - [ ] File store
      - [ ] Multi-file credential
    - [x] Cached
- [ ] Event Manager
  - [ ] Reactive Events
  - [ ] Annotated Events
    - [ ] with custom arguments - using annotated parameters
  - [ ] Command API
- [x] Customized REST Client
- [x] Customized WebSocket Client
  - [ ] Multiple instance for [PubSub](../pubsub)

