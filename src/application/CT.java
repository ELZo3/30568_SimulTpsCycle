package application;


import java.math.MathContext;

import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
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

	public void initialize() {
		kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
		lbr_iiwa_14_R820_1 = (LBR) getDevice(kuka_Sunrise_Cabinet_1,
				"LBR_iiwa_14_R820_1");
	}

	public void run() {
		lbr_iiwa_14_R820_1.move(ptpHome());
		lbr_iiwa_14_R820_1.move(ptp(Math.toRadians(50),0,0,0,0,0,0).setJointVelocityRel(0.5));
		lbr_iiwa_14_R820_1.move(ptp(0,0,0,Math.toRadians(-100),0,0,0));
		lbr_iiwa_14_R820_1.move(ptpHome());

	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		CT app = new CT();
		app.runApplication();
	}
}
