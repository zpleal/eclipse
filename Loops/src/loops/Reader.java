package loops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final String  DIC_FILE = "loops/pt-PT-AO.dic";
		
		 InputStream in = ClassLoader.getSystemResourceAsStream(DIC_FILE);
		 BufferedReader reader = new BufferedReader(new  InputStreamReader(in,"UTF-8"));

		 String line;
		 while((line = reader.readLine()) != null) {
			 
			 System.out.println(line);
		 }
		 reader.close();
		 
	}

}
