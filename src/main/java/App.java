import java.io.*;
import java.util.List;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class App {
    public void printVolumesList() {
        File[] volumes = rootListBasedOnOsName();
        if (volumes == null || volumes.length == 0)
            out.println("Can't read volumes information");
        else
            stream(volumes).forEach(root ->
                    out.println(format("%s: access to read - %s, write - %s",
                            root.getAbsolutePath(), root.canRead(), root.canWrite()))
            );
    }

    public void findAndPrintFiles(String url, String name) {
        List<String> files = findFiles(url, name);
        if (files.isEmpty()) {
            out.println("No data found");
        } else {
            files.forEach(out::println);
        }
    }

    public void readFile(String fileUrl) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileUrl))) {
            reader.lines().forEach(out::println);
        } catch (IOException e) {
            out.println(format("Can't read file \n%s", e.getMessage()));
        }
    }

    public String writeFile(String fileUrl, String text) {
        var file = new File(fileUrl);
        String copyFileUrl = "";

        if (!file.exists() || !file.isFile()) {
            out.println(format("Can't find file\n%s", fileUrl));
        } else {
            copyFileUrl = nameForCopy(file.getAbsolutePath());
            try (var writer = new BufferedWriter(new FileWriter(copyFileUrl));
                 var reader = new BufferedReader((new FileReader(file)))) {
                writer.write(text);

                reader.lines().forEach(line -> {
                    try {
                        writer.newLine();
                        writer.append(line);
                    } catch (IOException e) {
                        out.println(format("Can't perform writing to file\n%s", e.getMessage()));
                    }
                });

                writer.close();
                readFile(copyFileUrl);
            } catch (IOException e) {
                out.println(format("Can't write to file\n%s", e.getMessage()));
            }
        }
        return copyFileUrl;
    }

    protected String nameForCopy(String origin) {
        return format("%s_copy.%s",
                origin.substring(0, origin.lastIndexOf('.')),
                origin.substring(origin.lastIndexOf('.') + 1));
    }

    protected File[] rootListBasedOnOsName() {
        if (System.getProperty("os.name").startsWith("Mac")) {
            return new File("/Volumes").listFiles();
        }

        if (System.getProperty("os.name").startsWith("Linux")) {
            return new File("/proc/mounts").listFiles();
        }

        //if (System.getProperty("os.name").startsWith("Windows")) {}
        //Let's think that File.listRoots() is a correct method for all other operation systems (e.g. Windows)
        return File.listRoots();
    }

    protected List<String> findFiles(String url, String name) {
        File[] files = new File(url).listFiles((dir, fileName) -> fileName.contains(name));
        return files != null && files.length > 0
                ? stream(files).map(File::getAbsolutePath).collect(toList())
                : emptyList();
    }
}
