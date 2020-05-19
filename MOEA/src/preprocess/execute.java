package preprocess;

import java.io.IOException;

import ws.client.Jws2Client;

import genetic.Score_PAM250_AGap;



public class execute {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		String tool=null;
		
		for (int i=1; i<=6; i++) {
			
			switch (i) {
				case 1: tool = "MafftWS"; break;
				case 2: tool = "MuscleWS"; break;
				case 3: tool = "ClustalWS"; break;
				case 4: tool = "TcoffeeWS"; break;
				case 5: tool = "ProbconsWS"; break;
				case 6: tool = "ClustalOWS"; break;
			};
				
			args = new String[4];
			args[0] = "-h=http://www.compbio.dundee.ac.uk/jabaws";
			args[1] = "-s="+tool;
			args[2] = "-i=c:\\Users\\Wilson\\Desktop\\sequences.txt";
			args[3] = "-o=c:\\Users\\Wilson\\Desktop\\"+tool+".txt";
			//args[4] = "-f=c:\\Users\\Wilson\\Desktop\\prm.in";
			
			Jws2Client.main(args);
		
		}
		
		Score_PAM250_AGap x = new Score_PAM250_AGap();
		
		double value=0;
		value += x.getScore("ADKHKHASASA-HA", "-D----ESAS--AE")+x.getScore("ADKHKHASASA-HA", "----HD-SASAA--")+x.getScore("ADKHKHASASA-HA", "--SAH-SKAHAHSD");
		value += x.getScore("-D----ESAS--AE", "----HD-SASAA--")+x.getScore("-D----ESAS--AE", "--SAH-SKAHAHSD");
		value += x.getScore("----HD-SASAA--", "--SAH-SKAHAHSD");
		
		System.out.println("1."+value);
		
		value += x.getScore("ADKHKHASASAHA-", "-D----ESASAE--")+x.getScore("ADKHKHASASAHA-", "-----HDSASAA--")+x.getScore("ADKHKHASASAHA-", "---SAHSKAHAHSD");
		value += x.getScore("-D----ESASAE--", "-----HDSASAA--")+x.getScore("-D----ESASAE--", "---SAHSKAHAHSD");
		value += x.getScore("-----HDSASAA--", "---SAHSKAHAHSD");
		
		System.out.println("2."+value);
		
		
		
	}

}
