package rok.theft.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.util.function.Supplier;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/08/2021
 */
public class JsonConfigManager<T> {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder()
			                                 .setPrettyPrinting()
			                                 .serializeNulls()
			                                 .create();
	private final File configPath;
	private T data = null;

	public JsonConfigManager(Class<T> clazz, String file, Supplier<T> constructor, boolean force) {
		this.configPath = new File(ServerLifecycleHooks.getCurrentServer().getWorldPath(new FolderName("serverconfig")).toFile(), file + ".json");
		LOGGER.info("Loading config file at " + configPath.getAbsolutePath() + ".");
		if (!configPath.exists() && !force) {
			LOGGER.info("Could not find config file at " + configPath.getAbsolutePath() + ", creating a new one...");
			createConfig(constructor, false);
		} else if (force) {
			LOGGER.info("Forcing creation of config file at " + configPath.getAbsolutePath() + "...");
			createConfig(constructor, true);
		}

		try {
			final String read = FileIOUtils.read(configPath);
			this.data = fromJson(read, clazz);
			LOGGER.info("Config file loaded at " + configPath.getAbsolutePath() + ".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createConfig(Supplier<T> constructor, boolean delete) {
		if (delete) {
			configPath.delete();
		}

		try {
			if (configPath.createNewFile()) {
				LOGGER.info("Generated new config file at " + configPath.getAbsolutePath() + ".");
				FileIOUtils.write(configPath, toJson(constructor.get()));
			} else {
				LOGGER.error("Could not create config file at " + configPath.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public T get() {
		return data;
	}

	public void save() {
		try {
			FileIOUtils.write(configPath, toJson(data));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	public void save(T tData) {
		try {
			FileIOUtils.write(configPath, toJson(tData));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static <T> String toJson(T object) {
		return GSON.toJson(object);
	}

	public static <T> T fromJson(String json, Class<T> clazz) throws JsonParseException {
		return GSON.fromJson(json, clazz);
	}

	public static <T> T fromJson(String json, Type type) throws JsonParseException {
		return GSON.fromJson(json, type);
	}
}
