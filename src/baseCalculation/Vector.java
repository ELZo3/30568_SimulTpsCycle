package baseCalculation;

public class Vector {
	double a;
	double b;
	double c;

	public Vector(double A, double B, double C) {
		this.a=A;
		this.b=B;
		this.c=C;
	}
	
	public double dotProduct(Vector v)
	{
		return (this.a*v.getA()+this.b*v.getB()+this.c*v.getC());
	}
	
	

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

}
