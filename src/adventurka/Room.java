package adventurka;

import java.util.HashMap;
import java.util.Map;

class Room {
    final String id;
    String label;
    String description;
    String gameOver;

    final Map<String, Exit> exits = new HashMap<>();
    final Map<String, Item> items = new HashMap<>();
    final Map<String, UseAction> uses = new HashMap<>();

    Room(String id) {
        this.id = id;
    }
}
