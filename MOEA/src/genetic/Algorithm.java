package genetic;

import java.io.*;
import java.util.*;

import preprocess.*;

public class Algorithm {
	
	//parameters file output
	public static final String NAMEFILEIN1 = "ClustalWS.txt";
	public static final String NAMEFILEIN2 = "ProbconsWS.txt";
	public static final String NAMEFILEIN3 = "MuscleWS.txt";
	public static final String NAMEFILEIN4 = "TcoffeeWS.txt";
	public static final String NAMEFILEIN5 = "MafftWS.txt";
	public static final String NAMEFILEIN6 = "ClustalOWS.txt";
	
	public static final String NAMEFILEOUT = "fileout.txt";
	public static final	String PATH = "/home/user-siga/workspace/WSoto/";
	public FileReader fstreamr;
	public FileWriter fstreamw;
	public BufferedReader in_fitness; 
	public BufferedWriter out_fitness;
	
	//parameters genetic algorithm
	public static final int N_INDIVIDUALS = 500;
	public static final int NUMBER_GENERATIONS = 100;
	public static final double PERCEN_POPMUTATION = 0.5;
	public static final int NUMBER_EXECUTIONS = 1;
	public static final int NUMBER_GENESMUT = 10;
	public static final int NUMBER_TOOLS = 6; /* CLUSTALW - CLUSTALOW - PROBCONS - MUSCLE - TCOFFEE - MAFFT */
	
	//general attributes
	public static ArrayList<Character> Seq_Protein;
	public static ArrayList<Character> Seq_Protein_Alig;
	public static ArrayList<ArrayList<Character>> Matrix_Protein;
	public static ArrayList<ArrayList<Character>> Matrix_Protein_Alig;
	public static ArrayList<String> Name_Protein; 
	public static int max;
	public ArrayList<Double> Row_individual;
	public Individual x_Individual;
	public ArrayList<Individual> Population;
	public ArrayList<Individual> Population_aux;
	public Random r = new Random(System.nanoTime());
	
	
	public void LoadFile(String namefileinput) {
		
    	/* Begin: load file format FASTA */ 	
    	
    	try {
    		FileReader fr = new FileReader(PATH+namefileinput);
    		BufferedReader br = new BufferedReader(fr);

    		max = 0;
    		Matrix_Protein = new ArrayList<ArrayList<Character>>();
    		Matrix_Protein_Alig = new ArrayList<ArrayList<Character>>();
    		Seq_Protein = new ArrayList<Character>();
    		Name_Protein = new ArrayList<String>();
	      
    		String line = br.readLine();
    		Name_Protein.add(line);
    		while((line = br.readLine()) != null) {
    			if (!(line.contains(">"))){
    				for (int i=0; i<line.length(); i++)
    					Seq_Protein.add(line.charAt(i));
    			} 
    			else {
    				
    				Name_Protein.add(line);
    				
    				if (Seq_Protein.size() > max)
    					max = Seq_Protein.size();    				
    				
					Matrix_Protein_Alig.add (Seq_Protein);

					Seq_Protein_Alig = new ArrayList<Character>(Seq_Protein);
					
					for (int i=0; i<Seq_Protein.size(); i++)
						Seq_Protein_Alig.add(Seq_Protein.get(i));

					Iterator<Character> it = Seq_Protein_Alig.iterator();
					while (it.hasNext())
						if (it.next().equals('-'))
							it.remove();
					
					Matrix_Protein.add(Seq_Protein_Alig); 
            		
    				System.out.println(Seq_Protein);

    				Seq_Protein = new ArrayList<Character>();
    			}
    			
    		}
    		
			Matrix_Protein_Alig.add (Seq_Protein);
			
			Seq_Protein_Alig = new ArrayList<Character>(Seq_Protein);
			
			for (int i=0; i<Seq_Protein.size(); i++)
				Seq_Protein_Alig.add(Seq_Protein.get(i));
			
			Iterator<Character> it = Seq_Protein_Alig.iterator();
			while (it.hasNext())
				if (it.next().equals('-'))
					it.remove();
			Matrix_Protein.add(Seq_Protein_Alig);
			
			System.out.println(Seq_Protein);
    				
    		fr.close();
	    }
	    	catch(Exception e) {
	    		System.out.println("Exception: " + e);
	    }
    	    
	    //System.out.println(Matrix_Protein);  

    	/* End: load file format FASTA */
		
	}
	
	
    public Algorithm() throws IOException{

		fstreamw = new FileWriter(PATH+NAMEFILEOUT);
		BufferedWriter bw = new BufferedWriter(fstreamw);
	    
	    for (int k=1; k<=NUMBER_EXECUTIONS; k++) {
	    	
	    	System.out.println("NUMBER_EXECUTIONS:"+k);
	    	
	 	    Population = new ArrayList<Individual>(); 
	    	Population_aux = new ArrayList<Individual>();
	 	    
	 	    for (int x=1; x<=NUMBER_TOOLS; x++) {
	 	    	switch (x) {
	 	    		case 1: LoadFile(NAMEFILEIN1); 
	 	    				System.out.println("Cargado ClustalW...");
	 	    				break;
	 	    		case 2: LoadFile(NAMEFILEIN2); 
	    					System.out.println("Cargado ProbconsWS...");
	    					break;
	 	    		case 3: LoadFile(NAMEFILEIN3); 
	    					System.out.println("Cargado MuscleWS...");
	    					break;
	 	    		case 4: LoadFile(NAMEFILEIN4); 
	    					System.out.println("Cargado TcoffeeWS...");
	    					break;
	 	    		case 5: LoadFile(NAMEFILEIN5); 	 	    				
	 	    				System.out.println("Cargado MafftWS...");
	 	    				break;
	 	    		case 6: LoadFile(NAMEFILEIN6); 
	    					System.out.println("Cargado ClustalOWS...");
	    					break;
	 	    	}
	 	    
	 	    	create_population();
	    	}
	 	   
	 	    Population_aux=null;
	 	    
		    for (int i=1; i<=NUMBER_GENERATIONS; i++) {
		    	System.out.println("NUMBER_GENERATIONS:"+i);

		    	crossover();
		    	
				for (int m=1; m<=(Population.size()*PERCEN_POPMUTATION); m++) {
					mutation(r.nextInt(Population.size()));
				}
		    	
	    		System.out.println("Before Pareto Frontier: "+Population.size());
	    		selection(i);
	    		System.out.println("After Pareto Frontier: "+Population.size());
	    		
		    }

	    	//System.out.println(Name_Protein);
		    for (int i=0; i<Population.size(); i++) 
		    	WriteFile(bw, Population.get(i).getMatrix_Alignment());

	    }
	    
  	  	bw.close();
  	  	
    }
    
    
    public void WriteFile (BufferedWriter B, ArrayList<ArrayList<Character>> M) throws IOException {
    	
	    //B.append("\nExecution:"+k+"\n");
		//B.append("\n"+Population.get(i).getObjFun1()+" "+Population.get(i).getObjFun2()+"\n");
    	for(int j = 0; j < M.size(); j++){   
	    	B.append(Name_Protein.get(j)+"\n");
	    	B.append(Util.ArraytoString(M.get(j).toString())+"\n");  
		}
    	
    }
    
    
    public void create_population () {
	    
    	Individual x_Individual = new Individual();
    	int n_Individual1, n_Individual2;
    	
    	ArrayList<ArrayList<Character>> Matrix = new ArrayList<ArrayList<Character>>(); 
    	
    	for (int i=0; i<Matrix_Protein_Alig.size(); i++) {
    		ArrayList<Character> x = new ArrayList<Character>(Matrix_Protein_Alig.get(i));
    		Matrix.add(x);
    	}    	
    	    	
		//System.out.println(Matrix);
    	x_Individual.setMatrix_Alignment(Matrix);		

		//Util.Print_Alig(x_Individual);
		//Util.Print_Ind(x_Individual); 

    	Population_aux.add(x_Individual);
    
    	if (Population_aux.size()==NUMBER_TOOLS)
    		while (Population.size()<=N_INDIVIDUALS) {
    	    	n_Individual1 = r.nextInt(Population_aux.size());
    	    	n_Individual2 = r.nextInt(Population_aux.size());
    			while (n_Individual1 == n_Individual2)
    				n_Individual2 = r.nextInt(Population_aux.size());
    			crossover_stage(Population_aux, n_Individual1, n_Individual2);
    		}

    } 
    
    public void fitness(int nind) {

    	//System.out.println(">>> Start fitness <<<");
    	
    	double score = 0.0;

    	Individual N_Individual = new Individual();
		N_Individual.setMatrix_Alignment(Population.get(nind).getMatrix_Alignment());
		N_Individual.setP1_Matrix_Alignment(Population.get(nind).getP1_Matrix_Alignment());
		N_Individual.setP2_Matrix_Alignment(Population.get(nind).getP2_Matrix_Alignment());
		
    	//Util.Print_Alig(Population.get(nind));

		/*** Objective Score METal ***/
		String file_parent1 = "parent1_ind.txt";
		String file_parent2 = "parent2_ind.txt";
		String file_child = "child_ind.txt";

		try {

			fstreamw = new FileWriter(PATH + file_parent1);
			BufferedWriter bw = new BufferedWriter(fstreamw);
			WriteFile(bw, Population.get(nind).getP1_Matrix_Alignment());
			bw.close();
			
			fstreamw = new FileWriter(PATH + file_parent2);
			bw = new BufferedWriter(fstreamw);
			WriteFile(bw, Population.get(nind).getP2_Matrix_Alignment());
			bw.close();
			
			fstreamw = new FileWriter(PATH + file_child);
			bw = new BufferedWriter(fstreamw);
			WriteFile(bw, Population.get(nind).getMatrix_Alignment());
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*** Objective Score MetAl measure parent1 ***/
		score = ScoreMetAl.getScore (PATH, file_parent1, file_child);
		/*** Objective Score MetAl measure parent2 ***/
		score += ScoreMetAl.getScore (PATH, file_parent2, file_child);
		score /= 2;
		
    	/*** Objective Score MetAl measure average ***/
    	N_Individual.setObjFun1(score);
		
    	/*** Create matrix for each individual (needed stage crossover) ***/
		N_Individual.setMatrix_Individual(Crossover.Convert1(N_Individual.getMatrix_Alignment()));
    	
		/*** Objective Score PAM250 Alignment ***/	
    	/*
		String seq1, seq2;

    	for (int i=0; i<N_Individual.getMatrix_Alignment().size(); i++) {

    		seq1 = Util.ArraytoString(N_Individual.getMatrix_Alignment().get(i).toString());
    		
    		for (int j=i+1; j<N_Individual.getMatrix_Alignment().size(); j++) {

        		seq2 = Util.ArraytoString(N_Individual.getMatrix_Alignment().get(j).toString());
    			score += Score_PAM250_AGap.getScore(seq1, seq2);
    			
    		}
    		   
    	}
    	
		//System.out.println("PAM and AffineGap: "+score+" ");
		N_Individual.setObjFun1(score);
    	*/
    	
    	/*** Objective Entropy ***/
    	score = MinimumEntropy.getScore(N_Individual.getMatrix_Alignment());
    	//System.out.println("Entropy: "+score+" ");
    	N_Individual.setObjFun2(score);
    	/*** Objective Entropy per Column ***/
    	N_Individual.getMatrix_Individual().add(MinimumEntropy.MFreq);
    	    	
    	Population.set(nind, N_Individual);
		
        //System.out.println(">>> End fitness <<< \n");

    }
    
    public void selection (int nm) {
    	
    	//System.out.println(">>> Start selection Pareto frontier<<<");
    					
		System.out.println("Select Pareto Frontier: "+Population.size());

		Population_aux = new ArrayList<Individual>();
				
		do {
			
			Collections.sort(Population, new CompareValues());
		
			for (int i=0; i<Population.size(); i++) 
				for (int j=i+1; j<Population.size(); j++) 
					if (Population.get(i).getObjFun1() > Population.get(j).getObjFun1())  
						Population.get(j).setNondominant(-1);
			
			Iterator<Individual> p_itr = Population.iterator();
			while (p_itr.hasNext()) {
				Individual p_itr_ind = new Individual();
				p_itr_ind = p_itr.next();
				if (p_itr_ind.getNondominant()!=-1) {
					Population_aux.add(p_itr_ind);
					p_itr.remove();
				}
			}
				
			for (int i=0; i<Population.size(); i++)
				Population.get(i).setNondominant(0);
		
			//System.out.println("After delete Pareto Frontier: "+Population_aux.size() + " " + Population.size());
			
		} while (Population_aux.size()<N_INDIVIDUALS);
		
		Population.clear();
		for (int i=0; i<Population_aux.size(); i++)
			Population.add(Population_aux.get(i));
		
		System.out.println("After delete Pareto Frontier: "+Population.size());
	    //for (int i=0; i<Population.size(); i++) {
	    	//System.out.println(Population.get(i).getObjFun1()+" "+Population.get(i).getObjFun2());
    		//System.out.println(Population.get(i).getMatrix_Alignment());
		//}
		
		int size_population = Population.size();
		
		if (nm < NUMBER_GENERATIONS) {
			if (size_population<N_INDIVIDUALS){ 
				for (int i=0; i<(N_INDIVIDUALS-size_population); i++) {
					Individual x = new Individual();
					x.setMatrix_Alignment(Population.get(r.nextInt(Population.size())).getMatrix_Alignment());
					Population.add(x);
					mutation(Population.size()-1);
				}
			} 
			else {
				while (Population.size()>N_INDIVIDUALS) 
					Population.remove(Population.size()-1);
			}
		}
		
		//System.out.print("Stage selection best individual:");
    	//System.out.println(Population.get(0).getObjFun1()+" "+Population.get(0).getObjFun2());
		
		//System.out.println("***After***");
	    //for (int i=0; i<Population.size(); i++) {
			//System.out.println(Population.get(i).getObjFun1()+" "+Population.get(i).getObjFun2());
			//System.out.println(Population.get(i).getMatrix_Alignment());
	    //}
	    
    	//System.out.println(">>> End selection <<< \n");
    	
    }
	
   	
    public void crossover() {

    	//System.out.println(">>> Start crossover <<<");

    	int n_Individual1, n_Individual2, size_population;
    	
    	size_population = Population.size();
		System.out.println("*** Now size population "+size_population+" ***");
    			
    	//Numbers_crossover
		for (int n_cross=1; n_cross<=(size_population/2); ) {
			
	    	n_Individual1 = r.nextInt(size_population);
	    	n_Individual2 = r.nextInt(size_population);
			while (n_Individual1 == n_Individual2)
				n_Individual2 = r.nextInt(size_population);

			//System.out.println("r1:"+n_Individual1+" r2:"+n_Individual2);
			
			n_cross+=crossover_stage(Population, n_Individual1, n_Individual2);

		}		
		
    	//System.out.println(">>> End crossover <<< \n");
    	
    }
    
    public int crossover_stage (ArrayList<Individual> P, int n_Individual1, int n_Individual2) {
    	
    	Individual Parent1 = new Individual();
    	Individual Parent2 = new Individual();
    	Individual Child1;
    	Individual Child2;
    	int ind_create = 0;
    	
    	Parent1.setMatrix_Alignment(P.get(n_Individual1).getMatrix_Alignment());
		Parent1.setMatrix_Individual(Crossover.Convert1(Parent1.getMatrix_Alignment()));
		Parent2.setMatrix_Alignment(P.get(n_Individual2).getMatrix_Alignment());
		Parent2.setMatrix_Individual(Crossover.Convert1(Parent2.getMatrix_Alignment()));
		Child1 = new Individual();
		Child2 = new Individual();
		
		Child1.setMatrix_Individual(Crossover.Method(Parent1, Parent2));
		Child1.setMatrix_Alignment(Crossover.Convert2(Child1.getMatrix_Individual()));
		Child1.setP1_Matrix_Alignment(Parent1.getMatrix_Alignment());
		Child1.setP2_Matrix_Alignment(Parent2.getMatrix_Alignment());
			
		Child2.setMatrix_Individual(Crossover.Method(Parent2, Parent1));
		Child2.setMatrix_Alignment(Crossover.Convert2(Child2.getMatrix_Individual()));
		Child2.setP1_Matrix_Alignment(Parent2.getMatrix_Alignment());
		Child2.setP2_Matrix_Alignment(Parent1.getMatrix_Alignment());

		//System.out.println("Parent1: "+Parent1.getMatrix_Alignment());
		//System.out.println("Child1:  "+Child1.getP1_Matrix_Alignment());
		//System.out.println("Parent2: "+Parent2.getMatrix_Alignment());
		//System.out.println("Child2:  "+Child1.getP2_Matrix_Alignment());
		
		//System.out.println("<<Parent1>>");
		//Util.Print_Ind(Parent1);
		//System.out.println("\n\n<<Parent2>>");
		//Util.Print_Ind(Parent2);
		//System.out.println("\n\n<<Child1>>");
		//Util.Print_Ind(Child1);
		//System.out.println("\n\n<<Child2>>");
		//Util.Print_Ind(Child2);

		if (!Child1.getMatrix_Individual().isEmpty()) {

			Population.add(Child1);
			
			fitness(Population.size()-1);
			
			ind_create+=1;
			
		}
		
		if (!Child2.getMatrix_Individual().isEmpty()) {

			Population.add(Child2);

			fitness(Population.size()-1);
			
			ind_create+=1;
			
		}
		
		return ind_create;
    	
    }
    
    public void mutation(int nind) {
    	
    	//System.out.println(">>> Start mutation <<<");

		int random_gen1, random_gen2;
		Individual N_Individual = new Individual();
		N_Individual.setMatrix_Alignment(Population.get(nind).getMatrix_Alignment());
		N_Individual.setP1_Matrix_Alignment(Population.get(nind).getP1_Matrix_Alignment());
		N_Individual.setP2_Matrix_Alignment(Population.get(nind).getP2_Matrix_Alignment());
		N_Individual.setObjFun1(Population.get(nind).getObjFun1());
		N_Individual.setObjFun2(Population.get(nind).getObjFun2());
		
		int mutseq = r.nextInt(N_Individual.getMatrix_Alignment().size());
		int randomseq;
		
		for (int i = 0; i < mutseq; i++) {
			
			randomseq = r.nextInt(N_Individual.getMatrix_Alignment().size());
			
			for (int j = 0; j < NUMBER_GENESMUT; j++) {
				
				random_gen1 = r.nextInt(N_Individual.getMatrix_Alignment().get(randomseq).size());
				while (N_Individual.getMatrix_Alignment().get(randomseq).get(random_gen1)!='-') 
					random_gen1 = r.nextInt(N_Individual.getMatrix_Alignment().get(randomseq).size());

				N_Individual.set_Delcaracter(randomseq, random_gen1);

				//include position final +1
				random_gen2 = r.nextInt(N_Individual.getMatrix_Alignment().get(randomseq).size());
				while (N_Individual.getMatrix_Alignment().get(randomseq).get(random_gen2)=='-') 
					random_gen2 = r.nextInt(N_Individual.getMatrix_Alignment().get(randomseq).size());
				
				N_Individual.set_Inscaracter(randomseq, random_gen2);
			}
		
		}
		
		//System.out.println(N_Individual.getObjFun1()+" "+N_Individual.getObjFun2());
		//Util.Print_Alig(N_Individual);
		
		Population.set(nind, N_Individual);
		
		fitness (nind);
		
    	//System.out.println(">>> End mutation <<< \n");
    	
    }
    
    public static void main(String args[]) throws IOException{
    	
    	long begin = System.currentTimeMillis();
    	
        new Algorithm();
        
        long end = System.currentTimeMillis();  

        System.out.println("\nTime: "+(end-begin)/1000+" s.");
        
    }
	
}
