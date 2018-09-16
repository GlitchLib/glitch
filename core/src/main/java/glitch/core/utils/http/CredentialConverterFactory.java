package glitch.core.utils.http;

import glitch.auth.Credential;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public class CredentialConverterFactory extends Converter.Factory {
    @Nullable
    @Override
    public Converter<Credential, String> stringConverter(final Type type, final Annotation[] annotations, Retrofit retrofit) {
        return new Converter<Credential, String>() {
            @Override
            public String convert(Credential value) throws IOException {
                if (getRawType(type).isAssignableFrom(value.getClass())) {
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof HeaderValue) {
                            return ((HeaderValue) annotation).value() + " " + value.getAccessToken();
                        } else if (annotation instanceof Query) {
                            switch (((Query) annotation).value()) {
                                case "token":
                                    return value.getAccessToken();
                                case "refresh_token":
                                    return value.getRefreshToken();
                                case "client_id":
                                    return value.getClientId();
                                default:
                                    return null;
                            }
                        } else if (annotation instanceof QueryName) {
                            return value.getAccessToken();
                        }
                    }
                }
                return null;
            }
        };
    }
}
