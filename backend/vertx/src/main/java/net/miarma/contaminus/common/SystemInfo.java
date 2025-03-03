package net.miarma.contaminus.common;

public class SystemInfo {
	public static OSType getOS() {
		String envProperty = System.getProperty("os.name").toLowerCase();
		if(envProperty.contains("windows")) {
			return OSType.WINDOWS;
		} else if(envProperty.contains("linux")) {
			return OSType.LINUX;
		} else {
			return OSType.INVALID_OS;
		}
	}
}
