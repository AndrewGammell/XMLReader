package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import javax.xml.transform.Transformer;

import xmlUtils.XMLReading;
import xmlUtils.XMLSort;

public class Main {

	public static void main(String[] args) {
		XMLReading transformer = new XMLReading();
		
		try {
			InputStreamReader reader = 
					new InputStreamReader(new FileInputStream("C:\\Users\\andre\\eclipse-workspace\\XMLReading\\src\\main\\resources\\Pain008sample.xml"));
//			transformer.transform(reader);
			new XMLSort().sortTxns(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
