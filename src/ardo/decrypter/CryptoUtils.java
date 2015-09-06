package ardo.decrypter;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CryptoUtils {
	
	private static byte[] decrypt_bytes(byte[] keyBytes, byte[] iv, byte[] ciphertext) 
			throws NoSuchAlgorithmException, 
			       NoSuchPaddingException, 
			       InvalidKeyException, 
			       InvalidAlgorithmParameterException, 
			       IllegalBlockSizeException, 
			       BadPaddingException {
		
		SecretKey key = new SecretKeySpec(keyBytes, "AES");
		// PKCS5 aka PKCS7 for big blocks of data.
		Cipher decrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		decrypter.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		byte[] plaintext_bytes = decrypter.doFinal(ciphertext);
		
		
		return plaintext_bytes;
	}
	
	public static String decrypt(String key, String encrypted_text) throws Exception {

		if (encrypted_text.substring(0, 3).equals("$2$")) {
			
			byte[] the_key = DatatypeConverter.parseBase64Binary(key);
			byte[] iv = DatatypeConverter.parseBase64Binary(encrypted_text.substring(3, 3+24));
			byte[] ciphertext = DatatypeConverter.parseBase64Binary(encrypted_text.substring(3+24));
	
			return new String(decrypt_bytes(the_key, iv, ciphertext), "UTF-8");
		}
		
		throw new Exception("can't decrypt");
	}

	public static String readKey(String filename) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(filename));

		try {
			while (scanner.hasNextLine()) {
				String next_line = scanner.nextLine().trim();
				if (next_line.startsWith("--") || next_line.equals(""))
					continue;
				return next_line;
			}
			return "";
		} finally {
			scanner.close();
		}
	}

	
	
}
