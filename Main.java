package banking;

public class Main {
    static String url;

    public static void main(String[] args) {
        String fileName;
        if (args.length >= 2 && args[0].equals("-fileName")) {
            fileName = args[1];
            url = "jdbc:sqlite:" + fileName;
        }
        Menu programMenu = new Menu();
        programMenu.startProgram();
    }
}