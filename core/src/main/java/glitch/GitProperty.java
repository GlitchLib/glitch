package glitch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public enum GitProperty {

    APPLICATION_NAME("application.name"),
    APPLICATION_VERSION("application.version"),
    APPLICATION_DESCRIPTION("application.description"),
    APPLICATION_URL("application.url"),
    GIT_BRANCH("git.branch"),
    GIT_COMMIT_ID("git.commit.id"),
    GIT_COMMIT_ID_ABBREV("git.commit.id.abbrev"),
    GIT_COMMIT_ID_DESCRIBE("git.commit.id.describe");

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = GitProperty.class.getClassLoader().getResourceAsStream("git.properties")) {
            if (inputStream != null) {
                PROPERTIES.load(inputStream);
            }
        } catch (IOException ignore) {
        }
    }

    private final String value;

    GitProperty(String value) {
        this.value = value;
    }

    public static String get(GitProperty key) {
        return PROPERTIES.getProperty(key.value);
    }
}
