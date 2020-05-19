package genetic;

import java.util.ArrayList;
import java.util.Random;

public class Crossover {

	public static ArrayList<ArrayList<Double>> Convert1 (ArrayList<ArrayList<Character>> Matrix) {
		
		ArrayList<Double> A_temp;
		ArrayList<ArrayList<Double>> M_temp =  new ArrayList<ArrayList<Double>>();

		int i = 0;
		while (i<Matrix.size()) {
			
			A_temp = new ArrayList<Double>();
			
			if (Matrix.get(i).get(0)!='-')
				A_temp.add(1.0);
			else
				A_temp.add(0.0);
			
			for (int j=1; j<Matrix.get(i).size(); j++) {				
				if (Matrix.get(i).get(j)!='-')
					A_temp.add(1.0+A_temp.get(j-1));
				else
					A_temp.add(0.0+A_temp.get(j-1));
			}
			
			//System.out.println("(1):"+ Matrix.get(i));
			//System.out.println("(2):"+ A_temp);

			M_temp.add(A_temp);
			i++;

		}
		
		return M_temp;
	}
	
	public static ArrayList<ArrayList<Character>> Convert2 (ArrayList<ArrayList<Double>> Matrix){
		
		ArrayList<ArrayList<Character>> Align = new ArrayList<ArrayList<Character>>();
		ArrayList<Character> seq;
		int k;
		
    	for (int i=0; i<Matrix.size(); i++) {
    		k=0;
    		seq = new ArrayList<Character>();
    		for (int j=0; j<Matrix.get(i).size(); j++) {
    			if (Matrix.get(i).get(j).intValue()!=k) {
    				seq.add(Algorithm.Matrix_Protein.get(i).get(Matrix.get(i).get(j).intValue()-1));
    				k=Matrix.get(i).get(j).intValue();
    			}
    			else
    				seq.add('-');
    		}
    		    		
    		Align.add(seq);
    	}

    	//System.out.println(Align);
    	
    	return Align;
    	
	}
	
	public static ArrayList<ArrayList<Double>> Method (Individual Parent1, Individual Parent2) {
				
		Random r = new Random(System.nanoTime());

		//Crossover
		Individual P1 = new Individual();
		Individual P2 = new Individual();
		ArrayList<ArrayList<Double>> C = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> A_temp;
		
		P1.setMatrix_Individual(Parent1.getMatrix_Individual());
		P2.setMatrix_Individual(Parent2.getMatrix_Individual());
		
		//System.out.println("Parent1"+Parent1.getMatrix_Individual());
		//System.out.println("Parent1"+P1);
		//System.out.println("Parent2"+Parent2.getMatrix_Individual());
		//System.out.println("Parent2"+P2);
		
		//Method Crossover: select by random a gene in each sequence and compare it if are equals
		int pos11, pos21, pos12=-1, pos22=-1;
			
		pos11 = r.nextInt(P1.getMatrix_Individual().get(0).size());
		pos21 = r.nextInt(P1.getMatrix_Individual().get(0).size());
		while (pos11==pos21)
			pos21 = r.nextInt(P1.getMatrix_Individual().get(0).size());
		
		//swap for pos2 ever > pos1
		if (pos11>pos21) { 
			pos21 = pos21+pos11; 
			pos11 = pos21-pos11;
			pos21 = pos21-pos11; 
		}
		
		//extracting column vector for p1, p2 in the two individuals	
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		ArrayList<Double> z = new ArrayList<Double>();
		
		for (int i=0; i<P1.getMatrix_Individual().size(); i++) {
			x.add(P1.getMatrix_Individual().get(i).get(pos11));
			y.add(P1.getMatrix_Individual().get(i).get(pos21));
		}

		boolean flag=true;
		for (int i=0; i<P2.getMatrix_Individual().get(0).size(); i++) {
			
			for (int h=0; h<P2.getMatrix_Individual().size(); h++) 				
				z.add(P2.getMatrix_Individual().get(h).get(i));
			
			if ((x.equals(z))&&(flag)) {
				flag=false;
				pos12=i;
				//System.out.println("vector x "+x);
				//System.out.println("vector z "+z);
			}
			
			if (y.equals(z)&&(!flag)) {
				pos22=i;
				//System.out.println("vector y "+y);
				//System.out.println("vector z "+z);
				break;
			}
			
			z.clear();
				
		}
		
		//System.out.println("Finish pos11:" + pos11 + " pos21:" + pos21 + " -- pos12:" + pos12 + " pos22:" + pos22);
		
		for (int i=0; i<P1.getMatrix_Individual().size(); i++) {
			
			A_temp = new ArrayList<Double>();
			//System.out.println(pos1+" "+pos2+" "+P1.get(i).get(pos1)+" "+P2.get(i).get(pos1)+" "+P1.get(i).get(pos2)+" "+P2.get(i).get(pos2));
			
			if ((pos12!=-1) && (pos22!=-1)) { 
				
				for (int k=0; k<pos12; k++)
					A_temp.add(P2.getMatrix_Individual().get(i).get(k));
					
				for (int h=pos11; h<pos21; h++)
					A_temp.add(P1.getMatrix_Individual().get(i).get(h));
				
				for (int k=pos22; k<P2.getMatrix_Individual().get(i).size(); k++)
					A_temp.add(P2.getMatrix_Individual().get(i).get(k));
			
				C.add(A_temp);
			
			}
		}
		
		//System.out.println("Child12"+C);
		
		return C;
		
	}
	
}
