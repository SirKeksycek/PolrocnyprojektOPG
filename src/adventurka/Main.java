package adventurka;

public class Main {
    public static void main(String[] args) throws Exception {
        Engine e = new Engine();
        e.load("src/assets/hra12.json");
        e.play();
    }
}
