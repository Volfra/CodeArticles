package preprocess;

import java.util.ArrayList;

public class LCS {
	
	public static ArrayList<CharInteger> Convert (String x){
		
        ArrayList<CharInteger> xs = new ArrayList<CharInteger>();
        CharInteger ci;  

    	for (int i=0; i<x.length(); i++) {
    		ci = new CharInteger();
    		ci.setC(x.charAt(i));
    		ci.setI(1);
    		xs.add(ci);
    	}
    	
    	return xs;

	}
	
    public static ArrayList<CharInteger> AlgLCS (ArrayList<CharInteger> x, ArrayList<CharInteger> y) {
        
    	int M = x.size();
        int N = y.size();
        
        int[][] opt = new int[M+1][N+1];

        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (x.get(i).getC() == y.get(j).getC())
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }
        
        /*
        for (int i = 0; i<=M ; i++) {
        	System.out.println();
            for (int j = 0; j<=N; j++) {
            	System.out.print(opt[i][j]);
            }
        }
        */
        
        CharInteger ci;  
        ArrayList<CharInteger> total;
        total = new ArrayList<CharInteger>();
        
        int i = 0, j = 0;
        while(i < M && j < N) {
        	ci = new CharInteger();
            if (x.get(i).getC() == y.get(j).getC()) {
                ci.setC(x.get(i).getC());
                ci.setI(x.get(i).getI()+y.get(j).getI());
                total.add(ci);
                i++;
                j++;
            }
            else if 
            	(opt[i+1][j] >= opt[i][j+1]) {
            		ci.setC(x.get(i).getC());
            		ci.setI(x.get(i).getI()+0);
        			total.add(ci);
            		i++;
            	}
            else {                                
            		ci.setC(y.get(j).getC());
            		ci.setI(y.get(j).getI()+0);
            		total.add(ci);
            		j++;
            }
        }
        
        while (i<M) {
        	ci = new CharInteger();
        	ci.setC(x.get(i).getC());
        	ci.setI(x.get(i).getI()+0);
        	total.add(ci);
        	i++;
        }
        
        while (j<N) {
        	ci = new CharInteger();
        	ci.setC(y.get(j).getC());
        	ci.setI(y.get(j).getI()+0);
        	total.add(ci);
        	j++;
        }
        
        return total;

    }

}
