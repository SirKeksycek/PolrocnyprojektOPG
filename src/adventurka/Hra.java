package adventurka;

import java.util.Map;

class Hra {
    final Player player;
    private final Map<String, Room> rooms;

    Hra(Player player, Map<String, Room> rooms) {
        this.player = player;
        this.rooms = rooms;
    }

    Room getCurrentRoom() {
        return rooms.get(player.currentRoomId);
    }

    String checkGameOver() {
        return getCurrentRoom().gameOver;
    }

    void go(String direction) {
        if (direction == null) {
            System.out.println("Kam chceš ísť?");
            return;
        }
        direction = direction.toLowerCase();
        Room room = getCurrentRoom();
        Exit exit = room.exits.get(direction);
        if (exit != null) {
            player.currentRoomId = exit.target;
            System.out.println("Ideš " + direction + "...");
        } else {
            System.out.println("Tým smerom cesta nevedie.");
        }
    }

    void take(String itemName) {
        if (itemName == null) {
            System.out.println("Čo chceš zobrať?");
            return;
        }
        String key = itemName.toLowerCase();
        Room room = getCurrentRoom();
        Item item = room.items.remove(key);
        if (item != null) {
            player.inventory.put(key, item);
            System.out.println("Vzal si: " + itemName);
        } else {
            System.out.println("Tu nič také nevidíš...");
        }
    }

    void use(String itemName) {
        if (itemName == null) {
            System.out.println("Čo chceš použiť?");
            return;
        }
        String key = itemName.toLowerCase();
        if (!player.inventory.containsKey(key)) {
            System.out.println("Nemáš to pri sebe...");
            return;
        }

        Room room = getCurrentRoom();
        UseAction action = room.uses.get(key);
        if (action == null) {
            System.out.println("Tu to nepoužiješ...");
            return;
        }

        System.out.println(action.message);

        if (action.replace != null) {
            if (action.replace.newDesc != null) {
                room.description = action.replace.newDesc;
            }
            if (action.replace.newExits != null) {
                room.exits.clear();
                for (Exit e : action.replace.newExits) {
                    room.exits.put(e.direction.toLowerCase(), e);
                }
            }
        }

        player.inventory.remove(key);
        System.out.println("Predmet použitý a zmizol z inventára.");
    }
}
