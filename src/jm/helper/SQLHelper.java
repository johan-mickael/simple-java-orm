package jm.helper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SQLHelper {
	private static final String driver = "driver";
	private static final String url = "url";
	private static final String username = "username";
	private static final String password = "password";
	private static final String configFile = "Config.xml";
	private static final String tag = "prop";
	private static final String attributeName = "name";
	
	public static Connection getConnection(String config) throws ClassNotFoundException, Exception {
		Class.forName(getConfig(config, driver));
		Properties props = new Properties();
		props.setProperty("user", getConfig(config, username));
		props.setProperty("password", getConfig(config, password));
		Connection co = DriverManager.getConnection(getConfig(config, url), props);
		co.setAutoCommit(false);
		return co;
	}
	
	public static Connection getConnection() throws ClassNotFoundException, Exception {
		return getConnection(null);
	}
	
	public static String getConfig(String xmlConfig, String attribute) throws Exception {
		xmlConfig = (Objects.isNull(xmlConfig)) ? configFile : xmlConfig;
		try {
			File file = new File(xmlConfig);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName(tag);
			for (int itr = 0; itr < nodeList.getLength(); itr++){  
				Node node = nodeList.item(itr);
				if (node.getNodeType() == Node.ELEMENT_NODE) {  
					Element eElement = (Element) node;  
					if(eElement.hasAttribute(attributeName) && eElement.getAttribute(attributeName).equals(attribute)) {
						return eElement.getTextContent();
					}
				}  
			}  
		}   
		catch (Exception e) {  
			throw e;
		}   
		throw new Exception("Invalid file configuration");
	}
}
