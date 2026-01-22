package adventurka;

public class Main {
    public static void main(String[] args) {
        try {
            Engine engine = Engine.load("src/assets/hra12.json");
            engine.play();
        } catch (Exception e) {
            System.out.println("Chyba pri spustení hry: " + e.getMessage());
            e.printStackTrace();
        }
    }
}