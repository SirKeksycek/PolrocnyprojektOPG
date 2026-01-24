package adventurka;

import org.json.*;
import java.nio.file.*;
import java.util.*;
import java.text.Normalizer;

public class Engine {

    private Map<String, Room> rooms = new HashMap<>();
    private Room current;
    private Set<String> inventory = new LinkedHashSet<>();
    private boolean running = true;
    private String outro = "";

    // =========================================================
    // Načítanie hry z JSON
    // =========================================================
    public void load(String path) throws Exception {
        String json = Files.readString(Path.of(path));
        JSONObject root = new JSONObject(json);

        System.out.println(stripDiacritics(root.getString("intro")));
        System.out.println();

        outro = root.optString("outro", "");

        String startId = root.getJSONObject("player").getString("startRoomId");

        JSONArray arr = root.getJSONArray("rooms");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject r = arr.getJSONObject(i);
            Room room = new Room();

            room.id = r.getString("roomId");
            room.label = r.getString("label");
            room.desc = r.getString("desc");
            room.gameOver = r.optString("game_over", null);

            // items
            JSONArray items = r.optJSONArray("items");
            if (items != null) {
                for (int j = 0; j < items.length(); j++) {
                    JSONObject it = items.getJSONObject(j);
                    room.items.put(it.getString("label"), it.getString("desc"));
                }
            }

            // exits
            JSONArray exits = r.optJSONArray("exits");
            if (exits != null) {
                for (int j = 0; j < exits.length(); j++) {
                    JSONObject e = exits.getJSONObject(j);
                    room.exits.put(e.getString("label"), e.getString("roomId"));
                }
            }

            // uses
            JSONArray uses = r.optJSONArray("uses");
            if (uses != null) {
                for (int j = 0; j < uses.length(); j++) {
                    JSONObject u = uses.getJSONObject(j);
                    Use use = new Use();
                    use.itemId = u.getString("itemId");
                    use.desc = u.getString("desc");

                    JSONObject rep = u.getJSONObject("replace");
                    use.newDesc = rep.getString("desc");

                    JSONArray repExits = rep.getJSONArray("exits");
                    for (int k = 0; k < repExits.length(); k++) {
                        JSONObject e = repExits.getJSONObject(k);
                        use.newExits.put(e.getString("label"), e.getString("roomId"));
                    }
                    room.uses.add(use);
                }
            }

            rooms.put(room.id, room);
        }

        current = rooms.get(startId);
    }

    // =========================================================
    // Hlavny loop hry
    // =========================================================
    public void play() {
        Scanner sc = new Scanner(System.in);

        while (running) {
            printRoom();

            if (current.gameOver != null) {
                System.out.println(stripDiacritics(current.gameOver));
                if (!outro.isEmpty()) System.out.println(stripDiacritics(outro));
                break;
            }

            System.out.print("> ");
            String cmd = sc.nextLine().trim().toLowerCase();
            handle(cmd);
        }
    }

    // =========================================================
    // Vypis miestnosti + inventar + pouzitelne predmety
    // =========================================================
    private void printRoom() {
        System.out.println(stripDiacritics(current.desc));

        if (!current.exits.isEmpty()) {
            System.out.println("Cesty: " + stripDiacritics(String.join(", ", current.exits.keySet())));
        }

        if (!current.items.isEmpty()) {
            System.out.println("Predmety: " + stripDiacritics(String.join(", ", current.items.keySet())));
        }

        if (!inventory.isEmpty()) {
            System.out.println("Inventar: " + stripDiacritics(String.join(", ", inventory)));
        } else {
            System.out.println("Inventar je prazdny.");
        }

        // predmety z inventara, ktore sa daju pouzit v danej miestnosti
        List<String> usable = new ArrayList<>();
        for (Use u : current.uses) {
            if (inventory.contains(u.itemId)) {
                usable.add(u.itemId);
            }
        }
        if (!usable.isEmpty()) {
            System.out.println("Mozes pouzit: " + stripDiacritics(String.join(", ", usable)));
        }
        System.out.println();
    }

    // =========================================================
    // Spracovanie prikazov
    // =========================================================
    private void handle(String cmd) {
        if (cmd.startsWith("go ")) {
            go(cmd.substring(3));
        } else if (cmd.startsWith("take ")) {
            take(cmd.substring(5));
        } else if (cmd.startsWith("use ")) {
            use(cmd.substring(4));
        } else if (cmd.equals("inventory")) {
            System.out.println("Inventar: " + stripDiacritics(String.join(", ", inventory)));
        } else {
            System.out.println("Neznámy prikaz.");
        }
    }

    private void go(String exit) {
        if (!current.exits.containsKey(exit)) {
            System.out.println("Tym smerom sa neda ist.");
            return;
        }
        current = rooms.get(current.exits.get(exit));
    }

    private void take(String item) {
        if (!current.items.containsKey(item)) {
            System.out.println("Tento predmet tu nie je.");
            return;
        }
        inventory.add(item);
        current.items.remove(item);
        System.out.println("Vzal si " + stripDiacritics(item) + ".");
    }

    private void use(String item) {
        if (!inventory.contains(item)) {
            System.out.println("Nemáš tento predmet.");
            return;
        }

        boolean used = false;
        for (Use u : current.uses) {
            if (u.itemId.equals(item)) {
                System.out.println(stripDiacritics(u.desc));
                current.desc = u.newDesc;
                current.exits = u.newExits;
                used = true;
                break;
            }
        }

        if (used) {
            inventory.remove(item);
            System.out.println("Predmet bol pouzity a odstranený z inventara.");
        } else {
            System.out.println("Tu sa to neda pouzit.");
        }
    }

    // =========================================================
    // Odstranenie dlznych a makcien
    // =========================================================
    public static String stripDiacritics(String s) {
        if (s == null) return null;
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    // =========================================================
    // VNUTORNE TRIEDY
    // =========================================================
    static class Room {
        String id;
        String label;
        String desc;
        String gameOver;
        Map<String, String> exits = new LinkedHashMap<>();
        Map<String, String> items = new LinkedHashMap<>();
        List<Use> uses = new ArrayList<>();
    }

    static class Use {
        String itemId;
        String desc;
        String newDesc;
        Map<String, String> newExits = new LinkedHashMap<>();
    }
}
