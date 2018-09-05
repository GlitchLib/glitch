package glitch;

import java.io.IOException;
import java.util.Properties;

public final class Property {

    private static final Properties prop = new Properties();

    static {
        try {
            prop.load(Property.class.getClassLoader().getResourceAsStream("git.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static final String VERSION = prop.getProperty("application.version");
    public static final String REVISION = "rev." + prop.getProperty("git.closest.tag.commit.count");
    public static final String COMMIT_ID = prop.getProperty("git.commit.id.abbrev");
}
