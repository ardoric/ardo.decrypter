package ardo.decrypter;

import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) throws Exception {
		String key = CryptoUtils.readKey("private.key");
		
		HSConf hsconf = new HSConf("server.hsconf");
		for (Entry<String, String> entry : hsconf.entries()) {
			
			try {
				System.out.println(entry.getKey() + " " + CryptoUtils.decrypt(key,entry.getValue()));
			} catch (Exception e) {
				System.out.println(entry.getKey() + " <error>");
			}
			
		}
	}

}
