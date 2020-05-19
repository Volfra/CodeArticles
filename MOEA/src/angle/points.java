package angle;

public class points {

	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private double x3;
	private double y3;
	private double x4;
	private double y4;
	
	public void line1 (double a, double b, double c, double d) {
		
		x1 = a;
		y1 = b;
		x2 = c;
		y2 = d;
		
	}
	
	public void line2 (double a, double b, double c, double d) {
		
		x3 = a;
		y3 = b;
		x4 = c;
		y4 = d;
		
	}

	public double angle () {
		
		double m1 = (y1 - y2)/(x1 - x2);
		System.out.println("pendiente1: "+m1);
		System.out.println("angle1: "+Math.abs(Math.toDegrees(Math.atan(m1))));
		
		double m2 = (y3 - y4)/(x3 - x4);
		System.out.println("pendiente2: "+m2);
		System.out.println("angle2 : "+Math.abs(Math.toDegrees(Math.atan(m2))));
		
		if ((Math.abs(Math.toDegrees(Math.atan(m1)))==90.0)||(Math.abs(Math.toDegrees(Math.atan(m2)))==90.0))
			return (Math.abs(Math.toDegrees(Math.atan(m1)))+Math.abs(Math.toDegrees(Math.atan(m2))));
		else
			return 180.0-(Math.abs(Math.toDegrees(Math.atan(m1)))+Math.abs(Math.toDegrees(Math.atan(m2))));
		
	}
	
	public static void main(String[] args) {
		
		points p = new points();
		double[] ps = {1.0, 3.0, 2.0, 1.0, 1.0, 2.0, 1.0, 3.0};
		
		/*
		for (int i=0; i<ps.length/2-1; i=i+3) {
			p.line1(ps[i],ps[i+1],ps[i+2],ps[i+3]);
			p.line2(ps[i+4],ps[i+5],ps[i+6],ps[i+7]);
		}
		*/
		
		p.line1(1.0, 3.0, 1.0, 2.0);
		p.line2(2.0, 1.0, 1.0, 2.0);
		System.out.println(p.angle());
		
	}
	
}
