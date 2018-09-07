package glitch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Versions {

    public static final String APPLICATION_NAME = "application.name";
    public static final String APPLICATION_VERSION = "application.version";
    public static final String APPLICATION_DESCRIPTION = "application.description";
    public static final String APPLICATION_URL = "application.url";
    public static final String GIT_BRANCH = "git.branch";
    public static final String GIT_COMMIT_ID = "git.commit.id";
    public static final String GIT_COMMIT_ID_ABBREV = "git.commit.id.abbrev";
    public static final String GIT_COMMIT_ID_DESCRIBE = "git.commit.id.describe";

    public static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = Versions.class.getClassLoader().getResourceAsStream("git.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ignore) {}
        return properties;
    }
}
