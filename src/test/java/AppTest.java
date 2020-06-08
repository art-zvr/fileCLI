import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AppTest {
    private final App app = spy(new App());
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final File fileOne = mock(File.class);
    private final File fileTwo = mock(File.class);
    private final File[] files = {fileOne, fileTwo};

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));

        when(fileOne.getAbsolutePath()).thenReturn("driveA");
        when(fileOne.canRead()).thenReturn(true);
        when(fileOne.canWrite()).thenReturn(false);

        when(fileTwo.getAbsolutePath()).thenReturn("driveB");
        when(fileTwo.canRead()).thenReturn(true);
        when(fileTwo.canWrite()).thenReturn(true);
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void printVolumesList() {
        when(app.rootListBasedOnOsName()).thenReturn(files);
        app.printVolumesList();
        assertThat(outContent.toString()).isEqualTo("driveA: access to read - true, write - false\n" +
                "driveB: access to read - true, write - true\n");
    }

    @Test
    public void printVolumesList_canNotReadInformation() {
        when(app.rootListBasedOnOsName()).thenReturn(new File[0]);
        app.printVolumesList();
        assertThat(outContent.toString()).isEqualTo("Can't read volumes information\n");
    }

    @Test
    public void nameForCopy() {
        assertThat(app.nameForCopy("\\users\\test\\file.txt")).isEqualTo("\\users\\test\\file_copy.txt");
    }

    @Test
    public void findAndPrintFiles() {
        when((app.findFiles("url", "test"))).thenReturn(List.of("test"));
        app.findAndPrintFiles("url", "test");
        assertThat(outContent.toString()).isEqualTo("test\n");
    }

    @Test
    public void findAndPrintFiles_noDataFound() {
        when((app.findFiles("url", "test"))).thenReturn(emptyList());
        app.findAndPrintFiles("url", "test");
        assertThat(outContent.toString()).isEqualTo("No data found\n");
    }

    @Test
    public void readFile() {
        app.readFile("src/test/java/readWriteTest.txt");
        assertThat(outContent.toString()).isEqualTo("Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n");
    }

    @Test
    public void readFile_noFile() {
        app.readFile("test");
        assertThat(outContent.toString()).isEqualTo("Can't read file \ntest (No such file or directory)\n");
    }

    @Test
    public void writeFile() {
        var newFileUrl = app.writeFile("src/test/java/readWriteTest.txt", "test");

        assertThat(outContent.toString()).isEqualTo("test\nLorem ipsum dolor sit amet, consectetur adipiscing elit.\n");
        assertThat(newFileUrl.endsWith("src/test/java/readWriteTest_copy.txt")).isTrue();

        new File(newFileUrl).deleteOnExit();
    }

    @Test
    public void writeFile_canNotFindFileToWrite() {
        var newFileUrl = app.writeFile("test", "test");

        assertThat(outContent.toString()).isEqualTo("Can't find file\ntest\n");
        assertThat(newFileUrl).isEmpty();
    }
}