package application;


import java.math.MathContext;

import com.kuka.common.ThreadUtil;
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
		//********************************************************************************
		//*******************************Cycle auto***************************************
		//********************************************************************************
	    /*
		//Reading OCR + Pick Up PCB1
		lbr_iiwa_14_R820_1.moveAsync(ptp(getApplicationData().getFrame("/Version1robot1/A1")).setBlendingCart(2));
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-21).setBlendingRel(0.1));
		ThreadUtil.milliSleep(1000);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,21).setBlendingRel(0.1));
		
		//Drop PCB1		
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/C1"))); 	
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-10).setBlendingRel(0.1));
		ThreadUtil.milliSleep(1000);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,10).setBlendingRel(0.1));
		
		//Reading OCR + Pick Up PCB2
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/A1")));
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-21).setBlendingRel(0.1));
		ThreadUtil.milliSleep(1000);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,21).setBlendingRel(0.1));

		//Drop PCB2
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/C2"))); 
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-10).setBlendingRel(0.1));
		ThreadUtil.milliSleep(1000);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,10).setBlendingRel(0.1));		

		//Pick Up Sensor 1		
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/D1")));
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-30).setBlendingRel(0.1));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,30).setBlendingRel(0.1));

		//Control Sensor 1
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl1"))); 	
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl2")));
		ThreadUtil.milliSleep(500);		
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl3"))); 
		ThreadUtil.milliSleep(500);	
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl4")));
		ThreadUtil.milliSleep(500);	
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl5")));
		ThreadUtil.milliSleep(500);	
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl6")));
		ThreadUtil.milliSleep(500);
	
		//Drop Sensor 1
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/B1")));
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-30).setBlendingRel(0.1));
		ThreadUtil.milliSleep(2000);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,30).setBlendingRel(0.1));
		
		//Pick Up Sensor 2
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/D2")));
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-30).setBlendingRel(0.1));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,30).setBlendingRel(0.1));
	
		//Control Sensor 2
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl1"))); 	
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl2")));
		ThreadUtil.milliSleep(500);		
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl3"))); 
		ThreadUtil.milliSleep(500);	
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl4")));
		ThreadUtil.milliSleep(500);	
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl5")));
		ThreadUtil.milliSleep(500);	
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/Ctrl6")));
		ThreadUtil.milliSleep(500);
		
		//Drop Sensor 2
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/B1")));		
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,-30).setBlendingRel(0.1));
		ThreadUtil.milliSleep(2000);
		lbr_iiwa_14_R820_1.getFlange().moveAsync(linRel(0,0,30).setBlendingRel(0.1));
		
		//Return A1
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/A1")));
		*/
		
		
		//********************************************************************************
		//*******************************Remplacement tray pcb****************************
		//********************************************************************************
		/*
		//Pickup Tray vide
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/A1")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-30));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,30));
		
		//Drop Tray vide
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/G5")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-30));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,30));
		
		//Pickup Tray plein
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/G7")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-30));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,30));
		
		//Drop Tray plein
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/A1")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-30));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,30));
		*/
		
		//********************************************************************************
		//*******************************Remplacement tray sensor*************************
	    //********************************************************************************		
		
		//Pickup Tray vide
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/B1")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-10));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,10));
		
		//Drop Tray vide
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/G1")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-10));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,10));
		
		//Pickup Tray plein
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/G3")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-10));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,10));
		
		//Drop Tray plein
		lbr_iiwa_14_R820_1.move(ptp(getApplicationData().getFrame("/Version1robot1/B1")));
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,-10));
		ThreadUtil.milliSleep(500);
		lbr_iiwa_14_R820_1.getFlange().move(linRel(0,0,10));
		
	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		CT app = new CT();
		app.runApplication();
	}
}
