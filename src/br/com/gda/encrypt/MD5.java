package br.com.gda.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class MD5 {
	private static MessageDigest digester;

	static {
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String crypt(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}

		digester.update(str.getBytes());
		byte[] hash = digester.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
		}
		return hexString.toString();
	}

	public String token(String path, String auth, String zoneId) {

		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(zoneId));

		dateTime = dateTime.minusYears(3);

		String randomKey = dateTime.toLocalDate().toString() + dateTime.getHour() + dateTime.getMinute();

		String pathDataMD5 = crypt(path + auth);

		return crypt(pathDataMD5 + randomKey);
	}

	public boolean isTokenValid(String token, String path, String auth, String zoneId) {

		return token.equals(token(path, auth, zoneId)) || token.equals(tokenPrevious1(path, auth, zoneId))
				|| token.equals(tokenPrevious2(path, auth, zoneId));
	}

	private String tokenPrevious1(String path, String auth, String zoneId) {

		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(zoneId));

		dateTime = dateTime.minusYears(3).minusMinutes(1);

		String randomKey = dateTime.toLocalDate().toString() + dateTime.getHour() + dateTime.getMinute();

		String pathDataMD5 = crypt(path + auth);

		return crypt(pathDataMD5 + randomKey);
	}

	private String tokenPrevious2(String path, String auth, String zoneId) {

		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(zoneId));

		dateTime = dateTime.minusYears(3).minusMinutes(2);

		String randomKey = dateTime.toLocalDate().toString() + dateTime.getHour() + dateTime.getMinute();

		String pathDataMD5 = crypt(path + auth);

		return crypt(pathDataMD5 + randomKey);
	}

}
