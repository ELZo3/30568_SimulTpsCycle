package application;


import java.math.MathContext;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.params.VelocityRelParameter;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class CT extends RoboticsAPIApplication {
	private Controller kuka_Sunrise_Cabinet_1;
	private LBR lbr_iiwa_14_R820_1;
	private Tool pointe;

	public void initialize() {
		kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
		lbr_iiwa_14_R820_1 = (LBR) getDevice(kuka_Sunrise_Cabinet_1,
				"LBR_iiwa_14_R820_1");
		pointe=getApplicationData().createFromTemplate("pointe");
		pointe.attachTo(lbr_iiwa_14_R820_1.getFlange());
	}

	public void run() {
		//lbr_iiwa_14_R820_1.move(ptpHome());
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/A1")));
		
		//lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/TC/P1")));
		//lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/TC/P2")));
		//lbr_iiwa_14_R820_1.move(ptpHome().setJointVelocityRel(0.2));

	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		CT app = new CT();
		app.runApplication();
	}
}
