package baseCalculation;

import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;

public class Recaler_base {
	private Frame Base;
	
	public Frame calcul_base(Frame P1, Frame P2, Frame P3, ObjectFrame _Ancient_Base)
	{
		//--------(Copy of the base that we want to calibrate)--------------//
		Base=_Ancient_Base.copyWithRedundancy();
		
		//--------((P1P2) line)--------------//
		// The equation of this line is (D): ax+by+c=0
		// We will calculate the vector that contains the following variables a,b and c
		
		Vector x_vector = new Vector(0,0,0);
		
		x_vector.setA(P2.getY()-P1.getY());			// a
		x_vector.setB(-(P2.getX()-P1.getX()));		// b
		
		// c is the result of the scalar product of (a,b) vector and (P1.getX, P1.getY) vector
		
		Vector V_P1 = new Vector(P1.getX(), P1.getY(), 0);
		Vector u = new Vector(x_vector.getA(),x_vector.getB(),0);
		x_vector.setC(-u.dotProduct(V_P1)); //c
		
		//--------(The perpendicular line to (D) that contains P3)--------------//
		// The equation of this line is (D'): a'x+b'y+c'=0
		// We will calculate the vector that contains the following variables a',b' and c'
		
		Vector y_vector = new Vector(0,0,0);
		y_vector.setA(P2.getX()-P1.getX());		// a'
		y_vector.setB(P2.getY()-P1.getY());		// b'
		
		//c' is the result of the scalar product of (a',b') and (P3.getX, P3.getY) vector
		Vector V_P3 = new Vector(P3.getX(), P3.getY(), 0);
		Vector v = new Vector(y_vector.getA(),y_vector.getB(),0);
		y_vector.setC(-v.dotProduct(V_P3)); //c'
		
		//--------------(Origin Calculation = Intersection of (D) and (D'))--------------//
		// In order to have the origin, we must solve a system of the two equations (D) and (D')
		// Thus we need to define determinant
		
		double det = (x_vector.getA()*y_vector.getB())-(y_vector.getA()*x_vector.getB());
		double detx= (y_vector.getC()*x_vector.getB())-(x_vector.getC()*y_vector.getB());
		double dety= (y_vector.getA()*x_vector.getC())-(x_vector.getA()*y_vector.getC());
		
		Base.setX(detx/det);
		Base.setY(dety/det);
		
		if(Base.getY()==P1.getY())
		{
			return Base;
		}
		else
		{
			Base.setAlphaRad(Base.getAlphaRad()+Math.atan((Base.getY()-P1.getY())/((Base.getX()-P1.getX()))));
			return Base;
		}
		
		
		
	}

}
