package genetic;

import java.util.ArrayList;

import preprocess.CharInteger;

public class ScoreMLCS {
	
	public static ArrayList<Double> CountChar;
	
	public static double getScore (ArrayList<CharInteger> V, ArrayList<ArrayList<Character>> Matrix) {
		
		CountChar = new ArrayList<Double>();
		
		double val, count=0;
		for (int j=0; j<Matrix.get(0).size(); j++) {
			val=0;
			for (int i=0; i<Matrix.size(); i++)
				if (Matrix.get(i).get(j)!='-')
					val++;
			CountChar.add(val);	
		}	
		
		/*
		System.out.println();
		for (int i=0; i<CountChar.size(); i++)
			System.out.print(CountChar.get(i).intValue()+" ");

		System.out.println();
		for (int i=0; i<V.size(); i++)
			System.out.print(V.get(i).getI()+" ");
		
		System.out.println();
		*/
		for (int i=0; i<V.size(); i++)
			if (V.get(i).getI()==CountChar.get(i).intValue())
				count++;
		
		//System.out.println("Matches between vectors:"+count);
		
		return count;
		
		
	}
	
	

}
