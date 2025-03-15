package net.miarma.contaminus.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateParser {
	public static long parseDate(LocalDateTime date) {
		return date.toEpochSecond(ZoneOffset.UTC);
	}
}
