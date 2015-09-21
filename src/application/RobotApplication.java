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
				Math.toRadians(49.89),
				Math.toRadians(68.53),
				Math.toRadians(0),
				Math.toRadians(-74.31),
				Math.toRadians(94.73),
				Math.toRadians(-92.56),
				Math.toRadians(0)
				);
		UsedTool.getFrame("TCP").moveAsync(ptp(_start).setJointVelocityRel(0.3));
		/*-----------------------------------------------------------*/
		/*-----------------[Base Calibration]--------------------------*/
		UsedTool.getFrame("TCP").move(linRel(0,0,200).setCartVelocity(cart_vel).setJointJerkRel(0.2).breakWhen(Z_contact));
		UsedTool.getFrame("TCP").move(linRel(0,0,-10).setCartVelocity(cart_vel));
		
		UsedTool.getFrame("TCP").move(linRel(200,0,0).setCartVelocity(cart_vel).setJointJerkRel(0.2).breakWhen(X_contact));
		Frame X1=robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame());
		System.out.println("X: First point");
		UsedTool.getFrame("TCP").moveAsync(linRel(-10,0,0).setCartVelocity(cart_vel).setJointJerkRel(0.2));
		UsedTool.getFrame("TCP").move(linRel(-400,0,0).setCartVelocity(cart_vel).setJointJerkRel(0.2).breakWhen(X_contact));
		System.out.println("X: Second point");
		Frame X2=robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame());
		
		Frame centre = X1.copyWithRedundancy();
		centre.setZ((X1.getZ()+X2.getZ())/2);
		
		UsedTool.getFrame("TCP").move(lin(centre).setCartVelocity(100));
		
		UsedTool.getFrame("TCP").move(linRel(0,200,0).setCartVelocity(cart_vel).setJointJerkRel(0.2).breakWhen(Y_contact));
		Frame Y1=robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame());
		System.out.println("Y: First point");
		UsedTool.getFrame("TCP").moveAsync(linRel(0,-10,0).setCartVelocity(cart_vel).setJointJerkRel(0.2));
		UsedTool.getFrame("TCP").move(linRel(0,-400,0).setCartVelocity(cart_vel).setJointJerkRel(0.2).breakWhen(Y_contact));
		System.out.println("Y: Second point");
		Frame Y2=robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame());
		
		centre.setX((Y1.getX()+Y2.getX()/2));
		centre.setX((Y1.getY()+Y2.getY()/2));
		
		UsedTool.getFrame("TCP").move(lin(centre).setCartVelocity(100));
		
		
		
		//process();
		
		
	}
	
	public void process()
	{
		Spline process = new Spline(
				spl(getApplicationData().getFrame("/Process/P1")),
				spl(getApplicationData().getFrame("/Process/P2")),
				spl(getApplicationData().getFrame("/Process/P3")),
				spl(getApplicationData().getFrame("/Process/P4"))
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
