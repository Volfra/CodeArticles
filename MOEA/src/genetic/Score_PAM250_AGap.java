package genetic;

import java.util.HashMap;

/*
PAM250 substitution matrix
C 12
G -3   5
P -3  -1   6
S  0   1   1   1
A -2   1   1   1   2
T -2   0   0   1   1   3
D -5   1  -1   0   0   0   4
E -5   0  -1   0   0   0   3   4
N -4   0  -1   1   0   0   2   1   2
Q -5  -1   0  -1   0  -1   2   2   1   4
H -3  -2   0  -1  -1  -1   1   1   2   3   6
K -5  -2  -1   0  -1   0   0   0   1   1   0   5
R -4  -3   0   0  -2  -1  -1  -1   0   1   2   3   6
V -2  -1  -1  -1   0   0  -2  -2  -2  -2  -2  -2  -2   4
M -5  -3  -2  -2  -1  -1  -3  -2   0  -1  -2   0   0   2   6
I -2  -3  -2  -1  -1   0  -2  -2  -2  -2  -2  -2  -2   4   2   5
L -6  -4  -3  -3  -2  -2  -4  -3  -3  -2  -2  -3  -3   2   4   2   6
F -4  -5  -5  -3  -4  -3  -6  -5  -4  -5  -2  -5  -4  -1   0   1   2   9
Y  0  -5  -5  -3  -3  -3  -4  -4  -2  -4   0  -4  -5  -2  -2  -1  -1   7  10
W  -8  -7  -6  -2  -6  -5  -7  -7  -4  -5  -3  -3   2  -6  -4  -5  -2   0   0  17
*/

public class Score_PAM250_AGap{
	
	/*
	 * The ratio between GOP and GEP affect how much we view the size of gap
	 * Small GOP, large GEP: size more important
	 * Large GOP, small GEP: size less important
	 */
	
	private static final int GOP = -10; //GAP OPENING PENALTY
	private static final int GEP =  -2; //GAP EXTENSION PENALTY
	
    private static final int[][] MATRIX_SCORE = {
    	{12, -3, -3,  0, -2, -2, -5, -5, -4, -5, -3, -5, -4, -2, -5, -2, -6, -4,  0, -8, GEP},
    	{-3,  5, -1,  1,  1,  0,  1,  0,  0, -1, -2, -2, -3, -1, -3, -3, -4, -5, -5, -7, GEP},
    	{-3, -1,  6,  1,  1,  0, -1, -1, -1,  0,  0, -1,  0, -1, -2, -2, -3, -5, -5, -6, GEP},
    	{ 0,  1,  1,  1,  1,  1,  0,  0,  1, -1, -1,  0,  0, -1, -2, -1, -3, -3, -3, -2, GEP},
    	{-2,  1,  1,  1,  2,  1,  0,  0,  0,  0, -1, -1, -2,  0, -1, -1, -2, -4, -3, -6, GEP},
    	{-2,  0,  0,  1,  1,  3,  0,  0,  0, -1, -1,  0, -1,  0, -1,  0, -2, -3, -3, -5, GEP},
    	{-5,  1, -1,  0,  0,  0,  4,  3,  2,  2,  1,  0, -1, -2, -3, -2, -4, -6, -4, -7, GEP},
    	{-5,  0, -1,  0,  0,  0,  3,  4,  1,  2,  1,  0, -1, -2, -2, -2, -3, -5, -4, -7, GEP},
    	{-4,  0, -1,  1,  0,  0,  2,  1,  2,  1,  2,  1,  0, -2,  0, -2, -3, -4, -2, -4, GEP},
    	{-5, -1,  0, -1,  0, -1,  2,  2,  1,  4,  3,  1,  1, -2, -1, -2, -2, -5, -4, -5, GEP},
    	{-3, -2,  0, -1, -1, -1,  1,  1,  2,  3,  6,  0,  2, -2, -2, -2, -2, -2,  0, -3, GEP},
    	{-5, -2, -1,  0, -1,  0,  0,  0,  1,  1,  0,  5,  3, -2,  0, -2, -3, -5, -4, -3, GEP},
    	{-4, -3,  0,  0, -2, -1, -1, -1,  0,  1,  2,  3,  6, -2,  0, -2, -3, -4, -5,  2, GEP},
    	{-2, -1, -1, -1,  0,  0, -2, -2, -2, -2, -2, -2, -2,  4,  2,  4,  2, -1, -2, -6, GEP},
    	{-5, -3, -2, -2, -1, -1, -3, -2,  0, -1, -2,  0,  0,  2,  6,  2,  4,  0, -2, -4, GEP},
    	{-2, -3, -2, -1, -1,  0, -2, -2, -2, -2, -2, -2, -2,  4,  2,  5,  2,  1, -1, -5, GEP},
    	{-6, -4, -3, -3, -2, -2, -4, -3, -3, -2, -2, -3, -3,  2,  4,  2,  6,  2, -1, -2, GEP},
    	{-4, -5, -5, -3, -4, -3, -6, -5, -4, -5, -2, -5, -4, -1,  0,  1,  2,  9,  7,  0, GEP},
    	{ 0, -5, -5, -3, -3, -3, -4, -4, -2, -4,  0, -4, -5, -2, -2, -1, -1,  7,  10, 0, GEP},
    	{-8, -7, -6, -2, -6, -5, -7, -7, -4, -5, -3, -3,  2, -6, -4, -5, -2,  0,  0, 17, GEP},
    	{GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP, GEP}};

    public static int getPos(char a) {
    	
    	HashMap<Character, Integer> Proteins = new HashMap<Character, Integer>();
    	  	
    	Proteins.put('C', 0);
    	Proteins.put('G', 1);
    	Proteins.put('P', 2);
    	Proteins.put('S', 3);
    	Proteins.put('A', 4);
    	Proteins.put('T', 5);
    	Proteins.put('D', 6);
    	Proteins.put('E', 7);
    	Proteins.put('N', 8);
    	Proteins.put('Q', 9);
    	Proteins.put('H', 10);
    	Proteins.put('K', 11);
    	Proteins.put('R', 12);
    	Proteins.put('V', 13);
    	Proteins.put('M', 14);
    	Proteins.put('I', 15);
    	Proteins.put('L', 16);
    	Proteins.put('F', 17);
    	Proteins.put('Y', 18);
    	Proteins.put('W', 19);
    	Proteins.put('-', 20);
    	
    	return Proteins.get((String.valueOf(a)).toUpperCase().charAt(0));
    	
    }

    public static double getScore(String S1, String S2) {
    	
    	double score = 0.0;
    	int n_count_gap = 0;
    	    	
    	for (int index=0; index<S1.length(); index++) {
    		if ((S1.charAt(index)=='-') || (S2.charAt(index)=='-')) {
    			if (index>0) {
    				if ((S1.charAt(index-1)!='-') && (S2.charAt(index-1)!='-'))
    					n_count_gap++;
    			} else 
    				n_count_gap++;
    		}
    		
    		score += MATRIX_SCORE[getPos(S1.charAt(index))][getPos(S2.charAt(index))];
    		//System.out.println(score+" "+S1.charAt(index)+" "+S2.charAt(index)+"|("+n_count_gap+")");
    	}
    
    	//System.out.println("GAPS:" + n_count_gap +" "+ S1 +" "+ S2);
    	score = score + (GOP * n_count_gap);
    		
    	return score;
    }
}