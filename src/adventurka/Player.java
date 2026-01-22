package adventurka;

import java.util.HashMap;
import java.util.Map;

class Player {
    String currentRoomId;
    final Map<String, Item> inventory = new HashMap<>();

    Player(String startRoomId) {
        this.currentRoomId = startRoomId;
    }
}
