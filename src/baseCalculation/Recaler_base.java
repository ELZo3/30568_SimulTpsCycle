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
		
		Vector Z_vector = new Vector(0,0,0);
		
		Z_vector.setB(P2.getZ()-P1.getZ());			// a
		Z_vector.setA(-(P2.getX()-P1.getX()));		// b
		
		// c is the result of the scalar product of (a,b) vector and (P1.getX, P1.getY) vector
		
		Vector V_P1 = new Vector(P1.getZ(), P1.getX(), 0);
		Vector u = new Vector(Z_vector.getA(),Z_vector.getB(),0);
		Z_vector.setC(-u.dotProduct(V_P1)); //c
		
		//--------(The perpendicular line to (D) that contains P3)--------------//
		// The equation of this line is (D'): a'x+b'y+c'=0
		// We will calculate the vector that contains the following variables a',b' and c'
		
		Vector X_vector = new Vector(0,0,0);
		X_vector.setA(P2.getZ()-P1.getZ());		// a'
		X_vector.setB(P2.getX()-P1.getX());		// b'
		
		//c' is the result of the scalar product of (a',b') and (P3.getX, P3.getY) vector
		Vector V_P3 = new Vector(P3.getZ(), P3.getX(), 0);
		Vector v = new Vector(X_vector.getA(),X_vector.getB(),0);
		X_vector.setC(-v.dotProduct(V_P3)); //c'
		
		//--------------(Origin Calculation = Intersection of (D) and (D'))--------------//
		// In order to have the origin, we must solve a system of the two equations (D) and (D')
		// Thus we need to define determinant
		
		double det = (Z_vector.getA()*X_vector.getB())-(X_vector.getA()*Z_vector.getB());
		double detz= (X_vector.getC()*Z_vector.getB())-(Z_vector.getC()*X_vector.getB());
		double detx= (X_vector.getA()*Z_vector.getC())-(Z_vector.getA()*X_vector.getC());
		
		Base.setX(detx/det);
		Base.setZ(detz/det);
		
		if(Base.getX()==P1.getX())
		{
			return Base;
		}
		else
		{
			Base.setAlphaRad(Base.getAlphaRad()+Math.atan((Base.getX()-P1.getX())/((Base.getZ()-P1.getZ()))));
			return Base;
		}
		
		
		
	}

}
