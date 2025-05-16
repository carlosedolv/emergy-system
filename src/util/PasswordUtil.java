package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
	public static String hashPassword(String password) {
		try {
			// 
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			// Aplica o hash na senha
			byte[] encodedHash = digest.digest(password.getBytes());

			// Converte para o texto hexadecimal
			StringBuilder hexString = new StringBuilder();
			for(byte b : encodedHash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append("0");
				hexString.append(hex);
			}
			
			return hexString.toString();
			
			
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro to apply SHA-256 in password", e);
		}
	}

}
