package xmlUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLReading {

	private XMLStreamReader xmlReader;
	private XMLInputFactory xmlFactory;
	Document doc;
	Element root = null;
	Element element = null;

	public void transform(Reader reader) {

		try{
			setupXMLReader(reader);
			setupDocument();

			xmlReader.next();
			if(xmlReader.isStartElement()) {
				doc.appendChild(createElement(false));
			}		

		}catch(Exception e) {
			e.printStackTrace();
		}
		writeXmlDocumentToXmlFile(doc);
	}


	private Element createElement(boolean onlyVam) throws XMLStreamException {
		System.out.println("Create Element Called");
		boolean onlyVAM = onlyVam;

		String name = null;
		Element element = null;

		if(xmlReader.isStartElement()) {
			name = xmlReader.getLocalName();
			element = doc.createElement(name); 
			System.out.println("Created Element: " + name);
			xmlReader.next();
		}		

		if(xmlReader.isCharacters() && !xmlReader.getText().isBlank()) {
			
			if(name.equals("PmtInfId") && xmlReader.getText().equalsIgnoreCase("VIRTUAL")) {
				onlyVAM = true;
				System.out.println("////////////////////////VAM FOUND/////////////////////////////////////////////////////");
			}
			
			if(name.equals("IBAN")&& xmlReader.getText().equalsIgnoreCase("VIRTAUL_IBAN")) {
				System.out.println("////////////////////////////////VIRTUAL IBAN FOUND/////////////////////////////////////////");
			}
			element.setTextContent(xmlReader.getText());
			System.out.println("Set Text "  + xmlReader.getText() + " for: " + name);

			xmlReader.next();
		}else if(xmlReader.getText().isBlank()) {
			xmlReader.next();
		}

		if(xmlReader.isEndElement() && xmlReader.getLocalName().equals(name)) {
			xmlReader.next();
			System.out.println("Returning Element: " + name);

			return element;
		}

		if(xmlReader.isStartElement() && !xmlReader.getLocalName().isBlank()) {

			do {
				System.out.println("Creating Child Element For: " + name);
				element.appendChild(createElement(onlyVAM));
				
				while (xmlReader.isCharacters()){ 
					xmlReader.next();

				}
				
				
				System.out.println("LOCALNAME BEFORE CHECK: " + xmlReader.getLocalName());
				
			}while(!(xmlReader.isEndElement() && xmlReader.getLocalName().equals(name)));

		}
		System.out.println("FINAL RETURN FOR: " + name);
		xmlReader.next();
		return element;
	}


	private void setupDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		doc = documentBuilder.newDocument(); 
	}

	private void setupXMLReader(Reader reader) throws XMLStreamException {
		xmlFactory = XMLInputFactory.newInstance();
		xmlReader = xmlFactory.createXMLStreamReader(reader);
	}

	private static void writeXmlDocumentToXmlFile(Document xmlDocument) {
		String fileName = "MyFile";
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;

		try {

			transformer = tf.newTransformer();
			FileOutputStream outStream = new FileOutputStream(new File(fileName)); 
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(outStream));

		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
