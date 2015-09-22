package application;


import java.util.concurrent.TimeUnit;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.conditionModel.JointTorqueCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.JointConfigurationInformation;
import com.kuka.roboticsAPI.deviceModel.JointEnum;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.CartDOF;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.geometricModel.math.CoordinateAxis;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.controlModeModel.CartesianImpedanceControlMode;

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
public class Demo extends RoboticsAPIApplication {
	private Controller controller;
	private LBR robot;
	private Tool UsedTool;
	private ForceCondition X_contact;
	private ForceCondition Y_contact;
	private ForceCondition Z_contact;
	private CartesianImpedanceControlMode souple;
	private CartesianImpedanceControlMode rigide;
	private JointTorqueCondition end;
	
	
	public void initialize() {
		controller = (Controller) getContext().getControllers().toArray()[0];
		robot = (LBR) controller.getDevices().toArray()[0];
		UsedTool = getApplicationData().createFromTemplate("UsedTool");
		UsedTool.attachTo(robot.getFlange());
		X_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.X, 5);
		Y_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.Y, 5);
		Z_contact = ForceCondition.createNormalForceCondition(UsedTool.getFrame("TCP"), CoordinateAxis.Z, 5);
		souple = new CartesianImpedanceControlMode();
		souple.parametrize(CartDOF.TRANSL).setStiffness(150);
		
		rigide = new CartesianImpedanceControlMode();
		rigide.parametrize(CartDOF.TRANSL).setStiffness(5000);
		
		end = new JointTorqueCondition(JointEnum.J7, -5, 5);
		
		
				}

	public void run() {
		
		JointPosition start = new JointPosition(
				Math.toRadians(42.48),
				Math.toRadians(8.10),
				Math.toRadians(0),
				Math.toRadians(-86.51),
				Math.toRadians(0),
				Math.toRadians(84),
				Math.toRadians(27.45)
				);
		
		robot.move(ptp(start).setJointVelocityRel(0.5));
		
		System.out.println("Detection de contact selon X");
		IMotionContainer move1 =UsedTool.getFrame("TCP").move(linRel().setXOffset(-200).setCartVelocity(50).breakWhen(X_contact));
		if(move1.getFiredBreakConditionInfo()!=null)
		{
			System.out.println("Collision d�tect�e");
			ThreadUtil.milliSleep(1000);
		}
		else
		{
			System.out.println("Le premier point est atteint");
		}
		robot.move(ptp(start).setJointVelocityRel(0.5));
		
		System.out.println("Detection de contact selon Y");
		IMotionContainer move2 =UsedTool.getFrame("TCP").move(linRel().setYOffset(400).setCartVelocity(50).breakWhen(X_contact));
		if(move2.getFiredBreakConditionInfo()!=null)
		{
			System.out.println("Collision d�tect�e");
			ThreadUtil.milliSleep(1000);
		}
		else
		{
			System.out.println("Le deuxi�me point est atteint");
		}
		robot.move(ptp(start).setJointVelocityRel(0.5));
		
		System.out.println("Detection de contact selon Z");
		IMotionContainer move3 =UsedTool.getFrame("TCP").move(linRel().setZOffset(400).setCartVelocity(50).breakWhen(X_contact));
		if(move3.getFiredBreakConditionInfo()!=null)
		{
			System.out.println("Collision d�tect�e");
			ThreadUtil.milliSleep(1000);
		}
		else
		{
			System.out.println("Le troisi�me point est atteint");
		}
		robot.move(ptp(start).setJointVelocityRel(0.5));
		
		System.out.println("Garder la position en mode rigide");
		UsedTool.getFrame("TCP").move(positionHold(rigide, -1, TimeUnit.MINUTES).breakWhen(end));
		System.out.println("Garder la position en mode souple");
		UsedTool.getFrame("TCP").move(positionHold(souple, -1, TimeUnit.MINUTES).breakWhen(end));
		
		
	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		Demo app = new Demo();
		app.runApplication();
	}
}
