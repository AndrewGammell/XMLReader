package xmlUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

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

public class XMLSort {

	private XMLStreamReader xmlReader;
	private XMLInputFactory xmlFactory;
	Document VAMDoc;
	Document GBSDoc;
	Element VAMDocElement = null;
	Element GBSDocElement = null;
	Element VAMCustomer = null;
	Element GBSCustomer = null; 
	Element tempElement = null;
	String VAMFileName = "VAM";
	String GBSFileName = "GBS";


	public void sortTxns(Reader reader) {

		try{
			setupXMLReader(reader);
			setupDocument();
			getCommonElements();

		}catch(Exception e) {
			e.printStackTrace();
		}
		VAMDoc.appendChild(VAMDocElement);
		GBSDoc.appendChild(GBSDocElement);
		writeXmlDocumentToXmlFile(VAMDoc, VAMFileName);
		writeXmlDocumentToXmlFile(GBSDoc, GBSFileName);
	}

	private void getCommonElements() throws XMLStreamException {
		xmlReader.next();
		String name;

		if(xmlReader.isStartElement()) {
			name = xmlReader.getLocalName();
			VAMDocElement = VAMDoc.createElement(name); 
			GBSDocElement = GBSDoc.createElement(name);
			xmlReader.next();
		}	
		
		if(xmlReader.isCharacters()) {
			xmlReader.next();
		}
		
		if(xmlReader.isStartElement()) {
			Map<String,Element> child = getElement("DrctDbtTxInf");
			VAMDocElement.appendChild(child.get("VAM"));
			GBSDocElement.appendChild(child.get("GBS"));
		}
			
	}

	private Map<String,Element> getElement(String stoppingPoint) throws XMLStreamException {
		Map<String,Element> child = null;
		
		String name = null;
		Element VAMElement = null;
		Element GBSElement = null;
		
		
		if(xmlReader.isStartElement()) {
			name = xmlReader.getLocalName();
			System.out.println("Creating: "+xmlReader.getLocalName());

			VAMElement = VAMDoc.createElement(name); 
			GBSElement = GBSDoc.createElement(name); 
			xmlReader.next();
		}		

		if(xmlReader.isCharacters() && !xmlReader.getText().isBlank()) {
			VAMElement.setTextContent(xmlReader.getText());
			GBSElement.setTextContent(xmlReader.getText());
			xmlReader.next();
		}else if((!xmlReader.isStartElement()) && xmlReader.getText().isBlank()) {
			xmlReader.next();
		}

		if(xmlReader.isEndElement() && xmlReader.getLocalName().equals(name)) {
			xmlReader.next();
			child = new HashMap<String,Element>();
			child.put("VAM",VAMElement);
			child.put("GBS",GBSElement);
			return child;
		}

		if(xmlReader.isStartElement() 
				&& !xmlReader.getLocalName().isBlank() 
				&& !xmlReader.getLocalName().equals(stoppingPoint)) {

			do {
				
				Map<String, Element> temp = getElement(stoppingPoint);
				if(temp != null) {
					VAMElement.appendChild(temp.get("VAM"));
					GBSElement.appendChild(temp.get("GBS"));
				}
				while (xmlReader.isCharacters()){ 
					xmlReader.next();
				}
				
				if(xmlReader.isEndElement()) {
					System.out.println("Ending: " + xmlReader.getLocalName());
				}
				
			}while(!(xmlReader.isEndElement() && xmlReader.getLocalName().equals(name))
					&& !xmlReader.getLocalName().equals(stoppingPoint));
			
		}	
		child = new HashMap<String,Element>();
		child.put("VAM",VAMElement);
		child.put("GBS",GBSElement);
		if(!xmlReader.getLocalName().equals(stoppingPoint) && !xmlReader.isStartElement()) {
			xmlReader.next();
		}
		return child;
	}

	private void setupDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		VAMDoc = documentBuilder.newDocument(); 
		GBSDoc = documentBuilder.newDocument(); 
	}

	private void setupXMLReader(Reader reader) throws XMLStreamException {
		xmlFactory = XMLInputFactory.newInstance();
		xmlReader = xmlFactory.createXMLStreamReader(reader);
	}

	private static void writeXmlDocumentToXmlFile(Document xmlDocument, String fileName) {

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


