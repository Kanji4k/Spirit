package client.confis.spirit;

import com.google.gson.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 4) The "C" badge in front of names of people who use the mod.
 * Vanilla/other servers cannot tell clients about each other, so the list of mod users comes from a
 * JSON file YOU host, e.g. ["Confis0oooooo","Friend1"]. Own name always gets the badge.
 */
public final class SpiritUsers {
    public static String LIST_URL = "https://YOUR-HOST/spirit-users.json";
    private static final Set<String> USERS = ConcurrentHashMap.newKeySet();

    public static void refresh() {
        HttpClient.newHttpClient().sendAsync(HttpRequest.newBuilder(URI.create(LIST_URL)).build(), HttpResponse.BodyHandlers.ofString())
            .thenAccept(r -> { if (r.statusCode() == 200)
                for (JsonElement e : JsonParser.parseString(r.body()).getAsJsonArray()) USERS.add(e.getAsString().toLowerCase()); });
    }
    public static boolean isUser(String name, String self) {
        return name.equalsIgnoreCase(self) || USERS.contains(name.toLowerCase());
    }
    private SpiritUsers() {}
}
