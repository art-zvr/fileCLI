public class Main {
    public static void main(String[] args) {
        if (Menu.isCommandArgsValid(args)) {
            var menu = new Menu(args[0]);
            var app = new App();
            switch (menu.getCommand()) {
                case "volumes" -> app.printVolumesList();
                case "find" -> app.findAndPrintFiles(args[1], args[2]);
                case "read" -> app.readFile(args[1]);
                case "write" -> app.writeFile(args[1], args[2]);
                default -> System.out.println("Fin");
            }
        }
    }
}