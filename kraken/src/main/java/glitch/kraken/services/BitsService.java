package glitch.kraken.services;

import glitch.core.utils.http.ResponseException;
import glitch.kraken.json.lists.CheermoteList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public interface BitsService {
    @GET @Path("/bits/actions")
    CheermoteList getCheermotes() throws ResponseException;

    @GET @Path("/bits/actions")
    CheermoteList getCheermoteList(@QueryParam("channel_id") Long channelId) throws ResponseException;
}
