package genetic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ScoreMetAl {

	public static double getScore (String PATH, String file_parent, String file_child) {
		
		double score = 0.0;

		try {
			
			String s = PATH + "metalexe/metal " + PATH + file_parent + " " + PATH + file_child;
			
			Process p = Runtime.getRuntime().exec(s);
					
			InputStream is = p.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        
	        s = reader.readLine();
	        //System.out.println(s.length()+" "+s.lastIndexOf(' '));
	        s = s.substring(s.lastIndexOf(' '), s.length());
	        s = s.trim();
	        //System.out.println("Measure Parent1-Child: "+s);
	        
	        //Print more lines of result
	        //while ((s = reader.readLine()) != null)
	            //System.out.println(s);
	        
	        is.close();
	        
	        score = Double.parseDouble(s);
	        
	        //System.out.println("score MetAI: "+score);
	        
		} catch (IOException e) {
			e.printStackTrace();
		}

        return score;
		
	}
	
}
