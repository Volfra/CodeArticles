package genetic;

import java.util.ArrayList;
import java.util.Collections;

public class MinimumEntropy {

	public static ArrayList<Double> MFreq;

	public static double getScore (ArrayList<ArrayList<Character>> Matrix) {
		
		int pos=20; //Gap character '-'
		double val_freq, col_entropy = 0.0, entropy = 0.0;
		ArrayList<Double> Freq = new ArrayList<Double>();
		
		MFreq = new ArrayList<Double>();
		
		for (int l=0; l<Matrix.get(0).size(); l++){
			
			Freq = new ArrayList<Double>(Collections.nCopies(22, 0.0));
			
			for (int index = 0; index<Matrix.size(); index++){
				
				pos = Score_PAM250_AGap.getPos(Matrix.get(index).get(l));
				Freq.set(pos, Freq.get(pos)+1);
				//System.out.print(Matrix.get(index).charAt(l)); //column of characters
				
			}

			//Util.Print(Matrix);
			//System.out.println("Col:"+l+" "+Freq);
			
			for (int i=0; i<=21; i++) {
				val_freq = Freq.get(i);
				if (val_freq > 0) {
					Freq.set(i, Freq.get(i)/Matrix.size());
					col_entropy += val_freq * Math.log10(Freq.get(i));
				}
			}
			
			if ((col_entropy==0.0) && (pos==20)) col_entropy=-10000;
			
			col_entropy *= -1;
			Freq.set(21, col_entropy);
			entropy += col_entropy;
			
			MFreq.add(Math.abs(col_entropy));
			
			col_entropy = 0.0;
			
			//System.out.println("Col:"+l+" "+Freq+"\nEntropy:"+entropy);
			
		}
		
		//System.out.println(MFreq);
		
		return entropy;
	}
	
}
