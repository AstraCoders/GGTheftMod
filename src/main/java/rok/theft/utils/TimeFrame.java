package rok.theft.utils;

import net.minecraft.client.resources.Language;
import net.minecraft.util.text.LanguageMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 04/08/2021
 */
public class TimeFrame {

	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	private final Long days;
	private final Long hours;
	private final Long minutes;

	public TimeFrame() {
		this.millis = System.currentTimeMillis();
		days = TimeUnit.MILLISECONDS.toDays(millis);
		hours = TimeUnit.MILLISECONDS.toHours(millis - TimeUnit.DAYS.toMillis(days));
		minutes = TimeUnit.MILLISECONDS.toMinutes((millis - TimeUnit.DAYS.toMillis(days)) - TimeUnit.HOURS.toMillis(hours));
		seconds = TimeUnit.MILLISECONDS.toSeconds(((millis - TimeUnit.DAYS.toMillis(days)) - TimeUnit.HOURS.toMillis(hours)) - TimeUnit.MINUTES.toMillis(minutes));
	}

	public TimeFrame(Long millis) {
		this.millis = millis;
		days = TimeUnit.MILLISECONDS.toDays(millis);
		hours = TimeUnit.MILLISECONDS.toHours(millis - TimeUnit.DAYS.toMillis(days));
		minutes = TimeUnit.MILLISECONDS.toMinutes((millis - TimeUnit.DAYS.toMillis(days)) - TimeUnit.HOURS.toMillis(hours));
		seconds = TimeUnit.MILLISECONDS.toSeconds(((millis - TimeUnit.DAYS.toMillis(days)) - TimeUnit.HOURS.toMillis(hours)) - TimeUnit.MINUTES.toMillis(minutes));
	}

	public Long getDays() {
		return days;
	}

	public Long getHours() {
		return hours;
	}

	public Long getMinutes() {
		return minutes;
	}

	public Long getSeconds() {
		return seconds;
	}

	public Long getMillis() {
		return millis;
	}

	private final Long seconds;
	private final Long millis;

	public static TimeFrame fromDate(Date date) {
		return new TimeFrame(date.getTime());
	}

	public static String getFormated(Long millis) {
		Date date = new Date(millis);
		return sdf1.format(date);
	}

	public static String getFormatedNoHours(Long millis) {
		Date date = new Date(millis);
		return sdf2.format(date);
	}

	public Long getAllInDays() {
		return TimeUnit.MILLISECONDS.toDays(this.millis);
	}

	public Long getAllInHours() {
		return TimeUnit.MILLISECONDS.toHours(this.millis);
	}

	public Long getALlInMinutes() {
		return TimeUnit.MILLISECONDS.toMinutes(this.millis);
	}

	public Long getAllInSeconds() {
		return TimeUnit.MILLISECONDS.toSeconds(this.millis);
	}

	public String getFormated() {
		Date date = new Date(millis);
		return sdf1.format(date);
	}

	// -----------------------------------------------------------------------------------------------------------------------------//
	// Static Commands
	// -----------------------------------------------------------------------------------------------------------------------------//

	public String getFormatedNoHours() {
		Date date = new Date(millis);
		return sdf2.format(date);
	}

	public static Long getSeconds(Long millis) {
		return TimeUnit.MILLISECONDS.toSeconds(millis);
	}

	public static Long getMiutes(Long millis) {
		return TimeUnit.MILLISECONDS.toMinutes(millis);
	}

	public static Long getDays(Long millis) {
		return TimeUnit.MILLISECONDS.toDays(millis);
	}

	public TimeFrame getDiferenceUntilNow() {
		return new TimeFrame(System.currentTimeMillis() - this.millis);
	}

	public String getFormatedDiscursive() {
		return getFormatedDiscursive(false);
	}

	public String getFormatedDiscursive(boolean includeMillis) {
		String second = LanguageMap.getInstance().getOrDefault("timeframe.theft.second");
		String minute = LanguageMap.getInstance().getOrDefault("timeframe.theft.minute");
		String hour = LanguageMap.getInstance().getOrDefault("timeframe.theft.hour");
		String day = LanguageMap.getInstance().getOrDefault("timeframe.theft.day");
		String and = LanguageMap.getInstance().getOrDefault("timeframe.theft.and");

		String dia = this.getDays() >= 2 ? day+"s" : day;
		String hora = this.getHours() >= 2 ? hour+"s" : hour;
		String minuto = this.getMinutes() >= 2 ? minute+"s" : minute;
		String segundo = this.getSeconds() >= 2 || ((includeMillis || this.getSeconds() == 0) && (this.millis % 1000) >= 2) ? second+"s" : second;

		StringBuilder stringBuilder = new StringBuilder();

		if (this.getDays() > 0) {
			stringBuilder.append(this.getDays() + " " + dia);
		}
		if (this.getHours() > 0) {
			stringBuilder.append((stringBuilder.length() > 0 ? ", " : "") + this.getHours() + " " + hora);
		}
		if (this.getMinutes() > 0) {
			stringBuilder.append((stringBuilder.length() > 0 ? ", " : "") + this.getMinutes() + " " + minuto);
		}
		if (this.getSeconds() > 0 || includeMillis) {
			stringBuilder.append((stringBuilder.length() > 0 ? ", " : "") + this.getSeconds() + (includeMillis ? "." + (this.millis % 1000) : "") + " " + segundo);
		}
		for (int i = stringBuilder.length() - 1; i > 0; i--) {
			char aChar = stringBuilder.charAt(i);
			if (aChar == ',') {
				stringBuilder.deleteCharAt(i);
				stringBuilder.insert(i, and);
				stringBuilder.insert(i, " ");
				break;
			}
		}
		return stringBuilder.toString();
	}

	public Date toDate() {
		return new Date(this.millis);
	}
}
