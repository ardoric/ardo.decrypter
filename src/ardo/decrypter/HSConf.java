package ardo.decrypter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HSConf {
	
	private Map<String,String> keys = new HashMap<String,String>();
	
	public HSConf(String filename) throws SAXException, IOException, ParserConfigurationException {
		readHSConf(filename);
	}
	
	private void readHSConf(String filename) throws SAXException, IOException, ParserConfigurationException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filename));
		readEncryptedKeys(doc.getDocumentElement());
	}
	
	private void readEncryptedKeys(Element rootNode) {
		NodeList children = rootNode.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			if (!(children.item(i) instanceof Element))
				continue;
			Element e = (Element)children.item(i);
			
			if (e.hasAttribute("encrypted") && e.getAttribute("encrypted").toLowerCase().equals("true")) {
				keys.put(e.getTagName(), e.getTextContent());
			}
			if (e.hasAttribute("encryptedConfiguration") && e.getAttribute("encryptedConfiguration").toLowerCase().equals("true")) {
				keys.put(e.getTagName(), e.getTextContent());
			}
			readEncryptedKeys(e);
		}

	}

	public HSConf() throws SAXException, IOException, ParserConfigurationException {
		this("server.hsconf");
	}
	
	public Set<Entry<String, String>> entries() {
		return keys.entrySet();
	}
}
