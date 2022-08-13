package rok.theft.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/08/2021
 */
public class FileIOUtils {
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	public static String read(File path) throws IOException {
		return String.join("", Files.readAllLines(path.toPath(), UTF8));
	}

	public static void write(File path, String contents) throws IOException {
		Files.write(path.toPath(), contents.getBytes(UTF8));
	}
}
