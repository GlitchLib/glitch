package glitch.helix.service;

import glitch.helix.GlitchHelix;
import glitch.helix.object.json.Codes;
import glitch.helix.object.json.UploadEntitlement;
import glitch.service.AbstractHttpService;
import reactor.core.publisher.Mono;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class EntitlementService extends AbstractHttpService {
    public EntitlementService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public Mono<UploadEntitlement> createUploadUrl() {
        return Mono.error(new UnsupportedOperationException("Uploading data for drops is not supported YET!"));
    }

    public Mono<Codes> getCodeStatus() {
        return Mono.error(new UnsupportedOperationException("Codes is not supported YET!"));
    }

    public Mono<Codes> redeemCode() {
        return Mono.error(new UnsupportedOperationException("Codes is not supported YET!"));
    }
}
