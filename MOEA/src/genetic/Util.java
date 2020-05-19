package genetic;

public class Util {
	
	public static String ArraytoString (String s) {
		
		s = s.replace(",", "");
		s = s.replace("[", "");
		s = s.replace("]", "");
		s = s.replace(" ", "");
	   
		return s.trim();
	}
	
	public static void Print_Ind (Individual X) {
		
	    for(int i = 0; i < X.getMatrix_Individual().size();i++) {  
	    	System.out.println();
	    	for(int j = 0; j < X.getMatrix_Individual().get(i).size(); j++){  
	    		System.out.print(X.getMatrix_Individual().get(i).get(j)+"\t");  
	        }    
	    }
	   
	}

	public static void Print_Alig (Individual X) {
		
	    System.out.println();
	    for(int i = 0; i < X.getMatrix_Alignment().size();i++)   
	    	System.out.println(X.getMatrix_Alignment().get(i));  
	   
	}
	
}
