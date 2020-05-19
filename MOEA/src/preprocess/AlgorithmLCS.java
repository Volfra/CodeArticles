package preprocess;

import java.io.IOException;
import java.util.ArrayList;
import genetic.Util;

public class AlgorithmLCS {
	
	public static ArrayList<CharInteger> VectorMLCS (ArrayList<ArrayList<Character>> Matrix) {
		
		ArrayList<CharInteger> SeqNum1 = new ArrayList<CharInteger>();
		SeqNum1 = LCS.Convert(Util.ArraytoString(Matrix.get(0).toString()));
		
		ArrayList<CharInteger> SeqNum2;
		
		for (int i=1; i<Matrix.size(); i++) {
			SeqNum2 = new ArrayList<CharInteger>();
			SeqNum2 = LCS.Convert(Util.ArraytoString(Matrix.get(i).toString()));
			SeqNum1 = LCS.AlgLCS(SeqNum1, SeqNum2);
		}
		
        //print
        System.out.println();
        for (int k=0; k<SeqNum1.size(); k++) {
        	System.out.print(SeqNum1.get(k).getC());
        }
        System.out.println();
        for (int k=0; k<SeqNum1.size(); k++) {
        	System.out.print(SeqNum1.get(k).getI());
        }
        System.out.println();
		
        return SeqNum1;
        
	}
	
}
