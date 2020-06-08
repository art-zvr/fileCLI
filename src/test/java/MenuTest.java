import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void getCommand_shouldBeEmpty() {
        var menu = new Menu("-help");
        assertThat(menu.getCommand()).isEmpty();
    }

    @Test
    public void getCommand_shouldNotContainsDash() {
        var menu = new Menu("-volumes");
        assertThat(menu.getCommand()).isEqualTo("volumes");
    }

    @Test
    public void isCommandArgsValid() {
        String[] args = {"-volumes"};
        assertThat(Menu.isCommandArgsValid(args)).isTrue();
        assertThat(outContent.toString()).isEmpty();
    }

    @Test
    public void isCommandArgsValid_argsIsNull() {
        assertThat(Menu.isCommandArgsValid(null)).isFalse();
        assertThat(outContent.toString()).isEqualTo("Bad command. Try -help\n");
    }

    @Test
    public void isCommandArgsValid_argsIsEmpty() {
        String[] args = {};
        assertThat(Menu.isCommandArgsValid(args)).isFalse();
        assertThat(outContent.toString()).isEqualTo("Bad command. Try -help\n");
    }

    @Test
    public void isCommandArgsValid_argsIsNotEmptyCommandIsEmpty() {
        String[] args = {"", "hey", "lalaley"};
        assertThat(Menu.isCommandArgsValid(args)).isFalse();
        assertThat(outContent.toString())
                .isEqualTo("Unknown command \"\"\nCall \"-help\" to show a list of commands.\n");
    }

    @Test
    public void isCommandArgsValid_argsIsNotEmptyCommandStartsWithWrongChar() {
        String[] args = {"hop", "hey", "lalaley"};
        assertThat(Menu.isCommandArgsValid(args)).isFalse();
        assertThat(outContent.toString())
                .isEqualTo("Unknown command \"hop\"\nCall \"-help\" to show a list of commands.\n");
    }

    @Test
    public void isCommandArgsValid_argsIsNotEmptyCommandIsUnknown() {
        String[] args = {"-hop", "hey", "lalaley"};
        assertThat(Menu.isCommandArgsValid(args)).isFalse();
        assertThat(outContent.toString())
                .isEqualTo("Unknown command \"-hop\"\nCall \"-help\" to show a list of commands.\n");
    }
}