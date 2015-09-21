package application;


import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.World;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;
import com.kuka.roboticsAPI.motionModel.Spline;

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
	private ForceCondition X_contact;
	private ForceCondition Y_contact;
	private ForceCondition Z_contact;
	private Double cart_vel;
	private Frame P1, P2, P3;

	public void initialize() {
		controller = (Controller) getContext().getControllers().toArray()[0];
		robot = (LBR) controller.getDevices().toArray()[0];
		UsedTool = getApplicationData().createFromTemplate("UsedTool");
		UsedTool.attachTo(robot.getFlange());
		X_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.X, 5);
		Y_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.Y, 5);
		Z_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.Z, 5);
		
		cart_vel=120.0;
				}

	public void run() {
		/*-----------------[Start Position]--------------------------*/
		JointPosition _start = new JointPosition(
				Math.toRadians(42.57),
				Math.toRadians(62.42),
				Math.toRadians(0),
				Math.toRadians(-107.66),
				Math.toRadians(87.88),
				Math.toRadians(-88.79),
				Math.toRadians(29.21)
				);
		UsedTool.getFrame("TCP").moveAsync(ptp(_start).setJointVelocityRel(0.3));
		/*-----------------------------------------------------------*/
		/*-----------------[Base Calibration]--------------------------*/
		P1=getApplicationData().getFrame("/Process").copyWithRedundancy();
		P2=getApplicationData().getFrame("/Process").copyWithRedundancy();
		P3=getApplicationData().getFrame("/Process").copyWithRedundancy();
		// Saving the first frame
		UsedTool.getFrame("TCP").move(linRel(0,200,0).setJointJerkRel(0.2).setCartVelocity(80).breakWhen(Y_contact));
		P1.setX(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getX());
		P1.setY(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getY());
		System.out.println("First position saved !");
		// Saving the second frame
		UsedTool.getFrame("TCP").moveAsync(linRel(-30,-20,0).setCartVelocity(160));
		UsedTool.getFrame("TCP").move(linRel(0,200,0).setJointJerkRel(0.2).setCartVelocity(80).breakWhen(Y_contact));
		P2.setX(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getX());
		P2.setY(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getY());
		System.out.println("Second position saved !");
		// Saving the third frame
		UsedTool.getFrame("TCP").moveAsync(linRel(-150,-20,0).setCartVelocity(160));
		UsedTool.getFrame("TCP").moveAsync(linRel(0,50,0).setCartVelocity(160));
		UsedTool.getFrame("TCP").move(linRel(200,0,0).setJointJerkRel(0.2).setCartVelocity(80).breakWhen(X_contact));
		P3.setX(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getX());
		P3.setY(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getY());
		System.out.println("Second position saved !");
		//process();
		
		
	}
	
	public void process()
	{
		Spline process = new Spline(
				spl(getApplicationData().getFrame("/Process/P1")),
				spl(getApplicationData().getFrame("/Process/P2")),
				spl(getApplicationData().getFrame("/Process/P3")),
				spl(getApplicationData().getFrame("/Process/P4")),
				spl(getApplicationData().getFrame("/Process/P5"))
				).setCartVelocity(150);
		
		UsedTool.getFrame("TCP").move(process);
	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		RobotApplication app = new RobotApplication();
		app.runApplication();
	}
}
