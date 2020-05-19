package genetic;

import java.util.ArrayList;

public class Individual {

	private double ObjFun1;
	private double ObjFun2;
	private double ObjFun3;
	private int Nondominant;
	private ArrayList<Character> Vec_Alignment;
	private ArrayList<Double> Vec_Individual;
	private ArrayList<ArrayList<Character>> Matrix_Alignment;
	private ArrayList<ArrayList<Double>> Matrix_Individual;
	private ArrayList<ArrayList<Character>> P1_Matrix_Alignment;
	private ArrayList<ArrayList<Character>> P2_Matrix_Alignment;

	
	public Individual (){
		ObjFun1 = 0.0;
		ObjFun2 = 0.0;
		ObjFun3 = 0.0;
		Nondominant = 0;
	}
	
	public double getObjFun1() {
		return ObjFun1;
	}

	public void setObjFun1(double objFun1) {
		ObjFun1 = objFun1;
	}

	public double getObjFun2() {
		return ObjFun2;
	}

	public void setObjFun2(double objFun2) {
		ObjFun2 = objFun2;
	}

	public double getObjFun3() {
		return ObjFun3;
	}

	public void setObjFun3(double objFun3) {
		ObjFun3 = objFun3;
	}
	
	public ArrayList<ArrayList<Character>> getP1_Matrix_Alignment() {
		return P1_Matrix_Alignment;
	}

	public void setP1_Matrix_Alignment(ArrayList<ArrayList<Character>> matrixAlignment) {

		P1_Matrix_Alignment = new ArrayList<ArrayList<Character>>();
		for (int i=0; i<matrixAlignment.size(); i++) {
			Vec_Alignment = new ArrayList<Character>();
			for (int j=0; j<matrixAlignment.get(i).size(); j++)
				Vec_Alignment.add(matrixAlignment.get(i).get(j));
			P1_Matrix_Alignment.add(Vec_Alignment);
		}
		
	}

	public ArrayList<ArrayList<Character>> getP2_Matrix_Alignment() {
		return P2_Matrix_Alignment;
	}
	
	public void setP2_Matrix_Alignment(ArrayList<ArrayList<Character>> matrixAlignment) {

		P2_Matrix_Alignment = new ArrayList<ArrayList<Character>>();
		for (int i=0; i<matrixAlignment.size(); i++) {
			Vec_Alignment = new ArrayList<Character>();
			for (int j=0; j<matrixAlignment.get(i).size(); j++)
				Vec_Alignment.add(matrixAlignment.get(i).get(j));
			P2_Matrix_Alignment.add(Vec_Alignment);
		}
		
	}
	
	public ArrayList<ArrayList<Character>> getMatrix_Alignment() {
		return Matrix_Alignment;
	}

	public void setMatrix_Alignment(ArrayList<ArrayList<Character>> matrixAlignment) {

		Matrix_Alignment = new ArrayList<ArrayList<Character>>();
		for (int i=0; i<matrixAlignment.size(); i++) {
			Vec_Alignment = new ArrayList<Character>();
			for (int j=0; j<matrixAlignment.get(i).size(); j++)
				Vec_Alignment.add(matrixAlignment.get(i).get(j));
			Matrix_Alignment.add(Vec_Alignment);
		}
		
	}

	public ArrayList<ArrayList<Double>> getMatrix_Individual() {
		return Matrix_Individual;
	}

	public void setMatrix_Individual(ArrayList<ArrayList<Double>> matrixIndividual) {
		
		Matrix_Individual = new ArrayList<ArrayList<Double>>(); 
		for (int i=0; i<matrixIndividual.size(); i++) {
			Vec_Individual = new ArrayList<Double>();
			for (int j=0; j<matrixIndividual.get(i).size(); j++)
				Vec_Individual.add(matrixIndividual.get(i).get(j));
			Matrix_Individual.add(Vec_Individual);
		}		
		
	}

	public int getNondominant() {
		return Nondominant;
	}

	public void setNondominant(int nondominant) {
		Nondominant = nondominant;
	}
	
	public void set_Delcaracter (int seq, int pos) {
		Matrix_Alignment.get(seq).remove(pos);
	}
	
	public void set_Inscaracter (int seq, int pos) {
		Matrix_Alignment.get(seq).add(pos, '-');
	}
	
	public void set_Delcaracter(int pos) {
		if (Matrix_Alignment.get(0).get(pos)=='-')
			for (int i=0; i<Matrix_Alignment.size(); i++)
				Matrix_Alignment.get(i).remove(pos);		
	}
}
