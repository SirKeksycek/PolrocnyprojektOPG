package adventurka;

import org.json.*;
import java.nio.file.*;
import java.util.*;

public class Engine {

    private Map<String, Room> rooms = new HashMap<>();
    private Room current;
    private Set<String> inventory = new LinkedHashSet<>();
    private boolean running = true;
    private String outro = "";
    private int steps = 0;

    // nove flagy
    private boolean vezaOsvetlena = false;
    private boolean jadroStabilne = false;
    private boolean kartaPouzitaVCentrum = false;

    public void load(String path) throws Exception {
        String json = Files.readString(Path.of(path));
        JSONObject root = new JSONObject(json);

        System.out.println("========================================");
        System.out.println("     WELCOME TO RAVENROCK ADVENTURE     ");
        System.out.println("========================================\n");
        System.out.println(root.getString("intro"));
        System.out.println("\n========================================\n");

        System.out.println("Prikazy:");
        System.out.println(" go <smer>");
        System.out.println(" take <predmet>");
        System.out.println(" use <predmet>");
        System.out.println(" inventory\n");

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

            JSONArray items = r.optJSONArray("items");
            if (items != null) {
                for (int j = 0; j < items.length(); j++) {
                    JSONObject it = items.getJSONObject(j);
                    room.items.put(it.getString("label"), it.getString("desc"));
                }
            }

            JSONArray exits = r.optJSONArray("exits");
            if (exits != null) {
                for (int j = 0; j < exits.length(); j++) {
                    JSONObject e = exits.getJSONObject(j);
                    room.exits.put(e.getString("label"), e.getString("roomId"));
                }
            }

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

    public void play() {
        Scanner sc = new Scanner(System.in);

        while (running) {
            printRoom();

            if (current.gameOver != null) {
                System.out.println(current.gameOver);
                if (!outro.isEmpty()) System.out.println("\n" + outro);
                System.out.println("Pocet tahov: " + steps);
                break;
            }

            System.out.print("> ");
            String cmd = sc.nextLine().trim().toLowerCase();
            handle(cmd);
        }
    }

    private void printRoom() {
        System.out.println("****************************************");
        System.out.println("=== " + current.label + " ===");
        System.out.println("****************************************\n");
        System.out.println(current.desc + "\n");

        if (!current.exits.isEmpty()) {
            System.out.println("Cesty:");
            for (String e : current.exits.keySet()) {
                System.out.println(" ↳ " + e);
            }
            System.out.println();
        }

        if (!current.items.isEmpty()) {
            System.out.println("Predmety v miestnosti:");
            for (String i : current.items.keySet()) {
                System.out.println(" • " + i);
            }
            System.out.println();
        }

        List<String> usable = new ArrayList<>();
        for (Use u : current.uses) {
            if (inventory.contains(u.itemId)) usable.add(u.itemId);
        }
        if (!usable.isEmpty()) {
            System.out.println("Predmety, ktore mozes pouzit:");
            for (String u : usable) System.out.println(" • " + u);
            System.out.println();
        }

        printInventory();
        System.out.println("****************************************\n");
    }

    private void handle(String cmd) {
        if (cmd.startsWith("go ")) {
            go(cmd.substring(3));
        } else if (cmd.startsWith("take ")) {
            take(cmd.substring(5));
        } else if (cmd.startsWith("use ")) {
            use(cmd.substring(4));
        } else if (cmd.equals("inventory")) {
            printInventory();
        } else if (current.exits.containsKey(cmd)) {
            go(cmd);
        } else {
            System.out.println("Neznamy prikaz.");
        }
    }

    private void go(String exit) {
        if (!current.exits.containsKey(exit)) {
            System.out.println("Tym smerom sa neda ist.");
            return;
        }

        // Secret: do centra sa da ist iba ak je baterka pouzita
        if (current.id.equals("3") && exit.equals("centrum") && !vezaOsvetlena) {
            System.out.println("Je tma a nevidis cestu! Bez baterky zomieras...");
            System.out.println("Zly ending.");
            running = false;
            return;
        }

        // Centrum: ak bola karta pouzita, mozes ist aj do zamknute
        if (current.id.equals("7") && exit.equals("zamknute") && !kartaPouzitaVCentrum) {
            System.out.println("Brana je zamknuta. Nemozes prejst bez karty!");
            System.out.println("Zly ending.");
            running = false;
            return;
        }

        // Hlbka z jadra - povolena iba ak bola pouzita paka
        if (current.id.equals("6") && exit.equals("hlbka") && !jadroStabilne) {
            System.out.println("Jadro je nestabilne! Nemozes ist do hlbky bez pouzitej paky! zabil ta vybuch.");
            System.out.println("Zly ending.");
            running = false;
            return;
        }

        current = rooms.get(current.exits.get(exit));
        steps++;

        // Specialny ending: naradie + paka + hlbka
        if (current.id.equals("9") && jadroStabilne && inventory.contains("naradie") == false) {
            current.gameOver = "Mesto je zachranene a nasiel si poklad!";
            running = false;
        }

        printRoom();
    }


    private void take(String item) {
        if (!current.items.containsKey(item)) {
            System.out.println("Tento predmet tu nie je.");
            return;
        }
        inventory.add(item);
        current.items.remove(item);
        System.out.println("Vzal si " + item + ".");
        printRoom();
    }

    private void use(String item) {
        if (!inventory.contains(item)) {
            System.out.println("Nemas tento predmet.");
            return;
        }

        boolean used = false;
        for (Use u : current.uses) {
            if (u.itemId.equals(item)) {
                System.out.println(u.desc);
                current.desc = u.newDesc;

                for (Map.Entry<String, String> entry : u.newExits.entrySet()) {
                    current.exits.put(entry.getKey(), entry.getValue());
                }

                inventory.remove(item);

                // nove flagy
                if (item.equals("baterka") && current.id.equals("3")) vezaOsvetlena = true;
                if (item.equals("paka") && current.id.equals("6")) jadroStabilne = true;
                if (item.equals("karta") && current.id.equals("7")) kartaPouzitaVCentrum = true;

                used = true;
                break;
            }
        }

        if (!used) {
            System.out.println("Tento predmet sa tu neda pouzit.");
        }
        printRoom();
    }

    private void printInventory() {
        System.out.println("Inventar:");
        if (inventory.isEmpty()) {
            System.out.println(" • prazdny");
        } else {
            for (String i : inventory) {
                System.out.println(" • " + i);
            }
        }
        System.out.println();
    }

    // VNUTORNE TRIEDY
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
