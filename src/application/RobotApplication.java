package application;


import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;

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
public class RobotApplication extends RoboticsAPIApplication {
	private Controller controller;
	private LBR robot;
	private Tool UsedTool;
	private ForceCondition Z_contact;
	private Double cart_vel;

	public void initialize() {
		controller = (Controller) getContext().getControllers().toArray()[0];
		robot = (LBR) controller.getDevices().toArray()[0];
		UsedTool = getApplicationData().createFromTemplate("UsedTool");
		UsedTool.attachTo(robot.getFlange());
		Z_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.Z, 5);
		
		cart_vel=120.0;
				}

	public void run() {
		/*-----------------[Start Position]--------------------------*/
		JointPosition _start = new JointPosition(
				Math.toRadians(47.79),
				Math.toRadians(63.82),
				Math.toRadians(0),
				Math.toRadians(-80.60),
				Math.toRadians(89.87),
				Math.toRadians(-90.82),
				Math.toRadians(0)
				);
		UsedTool.getFrame("TCP").moveAsync(ptp(_start).setJointVelocityRel(0.3));
		/*-----------------------------------------------------------*/
		
		UsedTool.getFrame("TCP").move(linRel(0,0,200).setCartVelocity(cart_vel).setJointJerkRel(0.5).breakWhen(Z_contact));
		
	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		RobotApplication app = new RobotApplication();
		app.runApplication();
	}
}
