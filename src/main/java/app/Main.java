package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import javax.xml.transform.Transformer;

import transformer.XMLTransformer;

public class Main {

	public static void main(String[] args) {
		XMLTransformer transformer = new XMLTransformer();
		
		try {
			InputStreamReader reader = 
					new InputStreamReader(new FileInputStream("C:\\Users\\andre\\eclipse-workspace\\XMLReading\\src\\main\\resources\\Pain008sample.xml"));
			transformer.transform(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
