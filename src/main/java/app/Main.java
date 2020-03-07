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
					new InputStreamReader(new FileInputStream("ENTER_FILE_LOCATION"));
			transformer.transform(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
