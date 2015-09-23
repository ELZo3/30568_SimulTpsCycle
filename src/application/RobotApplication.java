package application;


import baseCalculation.Recaler_base;

import com.kuka.roboticsAPI.RoboticsAPIContext;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.World;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;
import com.kuka.roboticsAPI.motionModel.Spline;
import com.kuka.roboticsAPI.persistenceModel.IPersistenceEngine;
import com.kuka.roboticsAPI.persistenceModel.XmlApplicationDataSource;

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
	private Frame P1, P2, P3,P4;

	public void initialize() {
		controller = (Controller) getContext().getControllers().toArray()[0];
		robot = (LBR) controller.getDevices().toArray()[0];
		UsedTool = getApplicationData().createFromTemplate("UsedTool");
		UsedTool.attachTo(robot.getFlange());
		X_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.X, 4);
		Y_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.Y, 4);
		Z_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.Z, 4);

		cart_vel=120.0;
	}

	public void run() {
		/*-----------------[Start Position]--------------------------*/
		JointPosition _start = new JointPosition(
				Math.toRadians(53.12),
				Math.toRadians(66.70),
				Math.toRadians(0),
				Math.toRadians(-90.50),
				Math.toRadians(97.30),
				Math.toRadians(-94.21),
				Math.toRadians(16.50)
				);
		UsedTool.getFrame("TCP").moveAsync(ptp(_start).setJointVelocityRel(0.3));
		/*-----------------------------------------------------------*/
		/*-----------------[Base Calibration]--------------------------*/
		P1=getApplicationData().getFrame("/Process").copyWithRedundancy();
		P2=getApplicationData().getFrame("/Process").copyWithRedundancy();
		P3=getApplicationData().getFrame("/Process").copyWithRedundancy();
		P4=getApplicationData().getFrame("/Process").copyWithRedundancy();
		
		// Saving the first frame
		UsedTool.getFrame("TCP").move(linRel(0,0,500).setJointJerkRel(0.2).setJointAccelerationRel(0.1).setCartVelocity(25).breakWhen(Z_contact));
		P4.setY(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getY());
		System.out.println("First position saved !");
		UsedTool.getFrame("TCP").moveAsync(linRel(0,0,-20).setJointJerkRel(0.2).setJointAccelerationRel(0.1).setCartVelocity(100));
		UsedTool.getFrame("TCP").moveAsync(linRel(0,-100,0).setJointJerkRel(0.2).setJointAccelerationRel(0.1).setBlendingRel(0.1).setCartVelocity(100));
		UsedTool.getFrame("TCP").moveAsync(linRel(0,0,30).setJointJerkRel(0.2).setJointAccelerationRel(0.1).setBlendingRel(0.1).setCartVelocity(100));
		// Saving the second frame
		UsedTool.getFrame("TCP").move(linRel(0,200,0).setJointJerkRel(0.2).setCartVelocity(25).setJointAccelerationRel(0.1).breakWhen(Y_contact));
		P1.setX(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getX()+2.5);
		P1.setZ(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getZ());
		System.out.println("Second position saved !");
		// Saving the third frame
		UsedTool.getFrame("TCP").moveAsync(linRel(-30,-20,0).setBlendingRel(0.1).setCartVelocity(160));
		UsedTool.getFrame("TCP").move(linRel(0,200,0).setJointJerkRel(0.2).setCartVelocity(25).setJointAccelerationRel(0.1).breakWhen(Y_contact));
		P2.setX(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getX()+2.5);
		P2.setZ(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getZ());
		System.out.println("Third position saved !");
		// Saving the fourth frame
		UsedTool.getFrame("TCP").moveAsync(linRel(-130,-30,0).setCartVelocity(160).setBlendingRel(0.1));
		UsedTool.getFrame("TCP").moveAsync(linRel(0,80,0).setCartVelocity(160).setBlendingRel(0.1));
		UsedTool.getFrame("TCP").move(linRel(200,0,0).setJointJerkRel(0.2).setCartVelocity(25).setJointAccelerationRel(0.1).breakWhen(X_contact));
		P3.setX(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getX());
		P3.setZ(robot.getCurrentCartesianPosition(UsedTool.getFrame("TCP"), World.Current.getRootFrame()).getZ()-2.5);
		System.out.println("Fourth position saved !");

		Recaler_base rb = new Recaler_base();
		AbstractFrame new_base = rb.calcul_base(P1, P2, P3,P4, getApplicationData().getFrame("/Process"));
		UsedTool.getFrame("TCP").moveAsync(linRel(-20,0,0).setCartVelocity(160).setBlendingRel(0.1));
		UsedTool.getFrame("TCP").moveAsync(linRel(50,50,-200).setCartVelocity(160).setBlendingRel(0.1));
		//UsedTool.getFrame("TCP").move(ptp(new_base).setJointVelocityRel(0.2));


		new_base.setRedundancyInformation(robot, getApplicationData().getFrame("/Process").getRedundancyInformationForDevice(robot));

		final IPersistenceEngine engine = this.getContext().getEngine(IPersistenceEngine.class);
		final XmlApplicationDataSource defaultDataSource = (XmlApplicationDataSource) engine.getDefaultDataSource();

		defaultDataSource.changeFrameTransformation(getApplicationData().getFrame("/Process"), new_base.transformationFromWorld());

		defaultDataSource.saveFile(false);

		
		process();
		UsedTool.getFrame("TCP").move(linRel(0,0,-100).setCartVelocity(160));

	}

	public void process()
	{
		Spline process = new Spline(
				lin(getApplicationData().getFrame("/Process/P1")),
				lin(getApplicationData().getFrame("/Process/P2")),
				spl(getApplicationData().getFrame("/Process/P3")),
				spl(getApplicationData().getFrame("/Process/P4")),
				spl(getApplicationData().getFrame("/Process/P5")),
				spl(getApplicationData().getFrame("/Process/P6")),
				spl(getApplicationData().getFrame("/Process/P7")),
				lin(getApplicationData().getFrame("/Process/P8")),
				lin(getApplicationData().getFrame("/Process/P1"))
				);

		UsedTool.getFrame("TCP").move(process.setCartVelocity(150));
	}


	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		RobotApplication app = new RobotApplication();
		app.runApplication();
	}
}
