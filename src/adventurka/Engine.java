package adventurka;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Engine {

    private final Hra hra;
    private final String intro;
    private final String outro;

    private Engine(Hra hra, String intro, String outro) {
        this.hra = hra;
        this.intro = intro;
        this.outro = outro;
    }

    public static Engine load(String filePath) throws IOException {
        String json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        json = json.replaceAll("\\s+", " ").trim();

        String intro = extractQuoted(json, "\"intro\": \"");
        String outro = extractQuoted(json, "\"outro\": \"");
        String startRoomId = extractQuoted(json, "\"startRoomId\": \"");
        if (startRoomId.isEmpty()) startRoomId = extractQuoted(json, "\"player\": { \"startRoomId\": \"");

        int roomsStartIdx = json.indexOf("\"rooms\": [");
        if (roomsStartIdx < 0) throw new IOException("Nenašiel sa začiatok rooms");
        roomsStartIdx += "\"rooms\": [".length();
        int roomsEndIdx = json.lastIndexOf("]");
        String roomsBlock = json.substring(roomsStartIdx, roomsEndIdx).trim();

        Map<String, Room> rooms = new HashMap<>();
        String[] roomBlocks = roomsBlock.split("\\}\\s*,\\s*\\{");
        for (int i = 0; i < roomBlocks.length; i++) {
            String block = roomBlocks[i].trim();
            if (i == 0 && block.startsWith("{")) block = block.substring(1);
            if (i == roomBlocks.length - 1 && block.endsWith("}")) block = block.substring(0, block.length() - 1);
            Room room = parseRoom(block);
            rooms.put(room.id, room);
        }

        Player player = new Player(startRoomId);
        Hra hra = new Hra(player, rooms);
        return new Engine(hra, intro, outro);
    }

    private static String extractQuoted(String text, String key) {
        int start = text.indexOf(key);
        if (start < 0) return "";
        start += key.length();
        int end = text.indexOf("\"", start);
        if (end < 0) return "";
        return text.substring(start, end);
    }

    private static String extractArray(String text, String startMarker, String endMarker) {
        int start = text.indexOf(startMarker);
        if (start < 0) return "";
        start += startMarker.length();
        int end = text.indexOf(endMarker, start);
        if (end < 0) return "";
        return text.substring(start, end).trim();
    }

    private static Room parseRoom(String roomText) {
        Room room = new Room(extractQuoted(roomText, "\"roomId\": \""));
        room.label = extractQuoted(roomText, "\"label\": \"");
        room.description = extractQuoted(roomText, "\"desc\": \"");
        if (room.description.isEmpty()) room.description = extractQuoted(roomText, "\"description\": \"");
        room.gameOver = extractQuoted(roomText, "\"game_over\": \"");

        String exitsPart = extractArray(roomText, "\"exits\": [", "]");
        if (!exitsPart.isEmpty()) {
            String[] exits = exitsPart.split("\\}\\s*,\\s*\\{");
            for (String ex : exits) {
                String dir = extractQuoted(ex, "\"label\": \"");
                String target = extractQuoted(ex, "\"roomId\": \"");
                if (!dir.isEmpty() && !target.isEmpty()) {
                    room.exits.put(dir.toLowerCase(), new Exit(dir, target));
                }
            }
        }

        String itemsPart = extractArray(roomText, "\"items\": [", "]");
        if (!itemsPart.isEmpty()) {
            String[] items = itemsPart.split("\\}\\s*,\\s*\\{");
            for (String it : items) {
                String lbl = extractQuoted(it, "\"label\": \"");
                String desc = extractQuoted(it, "\"desc\": \"");
                if (!lbl.isEmpty()) {
                    room.items.put(lbl.toLowerCase(), new Item(lbl, desc));
                }
            }
        }

        String usesPart = extractArray(roomText, "\"uses\": [", "]");
        if (!usesPart.isEmpty()) {
            String[] usesArr = usesPart.split("\\}\\s*,\\s*\\{");
            for (String u : usesArr) {
                String itemId = extractQuoted(u, "\"itemId\": \"");
                String message = extractQuoted(u, "\"desc\": \"");
                UseAction ua = new UseAction(itemId, message);

                String replacePart = extractArray(u, "\"replace\": {", "}");
                if (!replacePart.isEmpty()) {
                    Replace rep = new Replace();
                    rep.newDesc = extractQuoted(replacePart, "\"desc\": \"");

                    String exitsReplace = extractArray(replacePart, "\"exits\": [", "]");
                    if (!exitsReplace.isEmpty()) {
                        String[] exArr = exitsReplace.split("\\}\\s*,\\s*\\{");
                        List<Exit> newExits = new ArrayList<>();
                        for (String ex : exArr) {
                            String dir = extractQuoted(ex, "\"label\": \"");
                            String target = extractQuoted(ex, "\"roomId\": \"");
                            if (!dir.isEmpty() && !target.isEmpty()) {
                                newExits.add(new Exit(dir, target));
                            }
                        }
                        rep.newExits = newExits;
                    }
                    ua.replace = rep;
                }

                room.uses.put(itemId.toLowerCase(), ua);
            }
        }

        return room;
    }

    public void play() {
        Scanner sc = new Scanner(System.in);
        System.out.println(intro);

        while (true) {
            Room current = hra.getCurrentRoom();

            // --- Popis miestnosti (len description) ---
            System.out.println("\n" + current.description);

            // vypíš exits (cesty)
            if (!current.exits.isEmpty()) {
                System.out.print("Cesty: ");
                System.out.println(String.join(", ", current.exits.keySet()));
            }

            // vypíš items (predmety)
            if (!current.items.isEmpty()) {
                System.out.print("Predmety: ");
                System.out.println(String.join(", ", current.items.keySet()));
            }

            // --- Skontroluj game over ---
            String gameOver = hra.checkGameOver();
            if (gameOver != null && !gameOver.isEmpty()) {
                System.out.println("\n" + gameOver);
                System.out.println(outro);
                break;
            }

            // --- Zadanie príkazu ---
            System.out.print("\n> ");
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();
            String arg = parts.length > 1 ? parts[1] : null;

            switch (cmd) {
                case "go" -> hra.go(arg);
                case "take" -> hra.take(arg);
                case "use" -> hra.use(arg);
                case "inventory" -> {
                    if (hra.player.inventory.isEmpty()) {
                        System.out.println("Inventár je prázdny.");
                    } else {
                        System.out.println("Inventár: " + String.join(", ", hra.player.inventory.keySet()));
                    }
                }
                case "exit", "quit" -> {
                    System.out.println("Koniec hry.");
                    return;
                }
                default -> System.out.println("Neznámy príkaz. Použi: go <smer>, take <predmet>, use <predmet>, inventory, exit");
            }
        }
    }

}