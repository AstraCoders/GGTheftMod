package rok.theft.config;

import java.util.function.Supplier;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 11/08/2022
 */
public class ConfigHelper {
	public static <T> JsonConfigManager<T> getConfig(String name, Supplier<T> typeClazz, Class<T> clazz) {
		return getConfig(name, typeClazz, clazz, false);
	}

	public static <T> JsonConfigManager<T> getConfig(String name, Supplier<T> typeClazz, Class<T> clazz, boolean force) {
		return new JsonConfigManager<>(clazz, name, typeClazz, force);
	}

}
