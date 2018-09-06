package glitch.auth.store;

import glitch.auth.Credential;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CachedStorage implements Storage {
    private final Collection<Credential> credentials;

    @Override
    public void registerAsync(Credential credential) {
        if (!credentials.contains(credential)) {
            credentials.stream().filter(c -> c.getUserId().equals(credential.getUserId()))
                    .findFirst().ifPresent(credentials::remove);

            credentials.add(credential);
        }
    }

    @Override
    public void dropAsync(Credential credential) {
        credentials.removeIf(c -> c.getUserId().equals(credential.getUserId()));
    }

    @Override
    public Collection<Credential> fetchAll() {
        return credentials;
    }

    @Override
    public Optional<Credential> getAsync(Predicate<Credential> condition) {
        return credentials.stream().filter(condition).findFirst();
    }

    @Override
    public Credential getByIdAsync(Long id) throws IOException {
        return getAsync(c -> c.getUserId().equals(id)).orElseThrow(() -> new IOException("Credentials doesn't exist with this ID: " + id));
    }

    @Override
    public Optional<Credential> getByLoginAsync(String loginRegex) {
        return getAsync(c -> c.getLogin().matches(loginRegex));
    }
}
