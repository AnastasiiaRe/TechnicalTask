package framework.platform.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Utilitarian class that provides simple file I/O operations
 */
public class FileAssistant {
    private FileAssistant() {

    }

    /**
     * Load a file resource via the {@link ClassLoader}
     *
     * @param fileName
     *            The name of the file
     * @return An object of type {@link InputStream} that represents the stream of a file that was read from the file
     *         system.
     */
    public static InputStream loadFile(String fileName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream iStream = loader.getResourceAsStream(fileName);
        if (iStream == null) {
            try {
                iStream = new FileInputStream(fileName);
            } catch (FileNotFoundException e) { // NOSONAR
                // Gobbling the checked exception here and doing nothing with it
                // because we are explicitly checking if the inputstream is null
                // and then throwing a runtime exception
            }
        }
        if (iStream == null) {
            throw new IllegalArgumentException("[" + fileName + "] is not a valid resource");
        }
        return iStream;
    }

    /**
     * Load a file resource via the {@link ClassLoader}
     *
     * @param file
     *            An object of type {@link File} which represents a file object
     * @return An object of type {@link InputStream} that represents the stream of a file that was read from the file
     *         system.
     */
    public static InputStream loadFile(File file) {
        return loadFile(file.getAbsolutePath());
    }

    /**
     * Read a file resource via the {@link ClassLoader}. Return it as a {@link String}.
     *
     * @param fileName
     *            The file name can either be an absolute path or a relative path from the project's base directory.
     * @return content of the file
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader buffreader = new BufferedReader(new InputStreamReader(loadFile(fileName),
                Charset.forName("UTF-8")))) {
            String line = null;
            while ((line = buffreader.readLine()) != null) {
                output.append(line);
            }
        }
        return output.toString();
    }
}
