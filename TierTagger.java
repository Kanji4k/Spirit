package client.confis.spirit;

import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 9) Tier tagger. Fetches a player's PvP tier from a tier API and caches it.
 * !! Set API_URL to the tier service you use (it must return JSON). The parser below
 * expects {"tier":"HT3"}; change parseTier() if your API returns another shape.
 */
public final class TierTagger {
    public static String API_URL = "https://YOUR-TIER-API/profile/";   // + player name
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();
    private static final Set<String> PENDING = ConcurrentHashMap.newKeySet();
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    /** Returns cached tier like "HT3", or "" while loading / unknown. */
    public static String tierOf(String playerName) {
        String c = CACHE.get(playerName);
        if (c != null) return c;
        if (PENDING.add(playerName)) {
            HTTP.sendAsync(HttpRequest.newBuilder(URI.create(API_URL + playerName)).build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(r -> CACHE.put(playerName, r.statusCode() == 200 ? parseTier(r.body()) : "-"))
                .exceptionally(ex -> { CACHE.put(playerName, "-"); return null; });
        }
        return "";
    }
    static String parseTier(String json) {
        try { JsonObject o = JsonParser.parseString(json).getAsJsonObject(); return o.get("tier").getAsString(); }
        catch (Exception e) { return "-"; }
    }
    private TierTagger() {}
}
