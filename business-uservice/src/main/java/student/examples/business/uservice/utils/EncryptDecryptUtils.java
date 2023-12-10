package student.examples.business.uservice.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecryptUtils {

	public static String encodeToBase64(String originalString) {
		byte[] encodedBytes = Base64.getEncoder().encode(originalString.getBytes());
		return new String(encodedBytes);
	}

	public static String decodeFromBase64(String base64String) {
		byte[] decodedBytes = Base64.getDecoder().decode(base64String);
		return new String(decodedBytes);
	}

	public static SecretKey getKey() {
		String hardcodedKey = "ThisIsA16ByteKey";
		byte[] keyBytes = hardcodedKey.getBytes(StandardCharsets.UTF_8);
		return new SecretKeySpec(keyBytes, "AES");
	}

	public static String encrypt(String plaintext) {
		SecretKey secretKey = getKey();
		Cipher cipher;
		byte[] encryptedBytes = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String decrypt(String encryptedText) {
		SecretKey secretKey = getKey();
		Cipher cipher;
		byte[] decryptedBytes = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
			decryptedBytes = cipher.doFinal(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}
}
