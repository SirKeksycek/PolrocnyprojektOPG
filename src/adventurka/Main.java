package adventurka;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        Engine e = new Engine();
        e.load("src/assets/hra12.json");
        e.play();
    }
}