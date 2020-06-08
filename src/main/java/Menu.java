import java.util.Map;

import static java.lang.String.format;
import static java.lang.System.out;

public class Menu {
    static Map<String, String> commands = Map.of(
            "help", "call to show all suitable commands",
            "volumes", "call to show all volumes in a system",
            "find", "call to find a file by name",
            "read", "call to read a file",
            "write", "call to change a file"
    );
    private String command;

    public Menu(String userCommand) {
        if (userCommand.equals("-help"))
            printHelpMenu();
        else
            command = userCommand.replace("-","");
    }

    public String getCommand() {
        return command == null || command.isEmpty() ? "" : command;
    }

    public static boolean isCommandArgsValid(String[] args) {
        if (args == null || args.length == 0) {
            out.println("Bad command. Try -help");
            return false;
        }

        String userCommand = args[0];
        if (userCommand.isEmpty() || !userCommand.startsWith("-")
                || !commands.containsKey(userCommand.replace("-",""))) {
            printUnknownCommand(userCommand);
            return false;
        }

        return true;
    }



    private static void printUnknownCommand(String command) {
        out.println(format("Unknown command \"%s\"", command));
        out.println("Call \"-help\" to show a list of commands.");
    }

    private void printHelpMenu() {
        out.println("###\tHELP MENU\t###");
        out.println("Use next format to call a command: -[command] (e.g. -help)\n" +
                "Don't forget to put a dash before command.");
        out.println("Commands:");
        commands.forEach((key, value) -> out.println(format("-%s: %s", key, value)));


    }
}
