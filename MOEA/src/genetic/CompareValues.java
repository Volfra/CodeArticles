package genetic;

import java.util.Comparator;

public class CompareValues implements Comparator<Individual>{

    public int compare(Individual O1, Individual O2)
    {
        if(O2.getObjFun2()<O1.getObjFun2()) 
        	return 1;
        
        if(O2.getObjFun2()==O1.getObjFun2()) 
        	return 0;
        
        return -1;
        
   }
}
