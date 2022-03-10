// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  //iitilalize controls
  private final XboxController xbox = new XboxController(0);
  //initialize variables for drive system
  float velocity = 0;
  //initialize all drive motors
  private final CANSparkMax leftFront = new CANSparkMax(10, CANSparkMax.MotorType.kBrushless);
  private final CANSparkMax leftRear = new CANSparkMax(1, CANSparkMax.MotorType.kBrushless);
  private final CANSparkMax rightFront = new CANSparkMax(2, CANSparkMax.MotorType.kBrushless);
  private final CANSparkMax rightRear = new CANSparkMax(3, CANSparkMax.MotorType.kBrushless);
  //initialize all drive motor groups
  private final SpeedControllerGroup left = new SpeedControllerGroup(leftFront, leftRear);
  private final SpeedControllerGroup right = new SpeedControllerGroup(rightFront, rightRear);
  private final DifferentialDrive drivetrain = new DifferentialDrive(right, left);
  //initialize firing motors
  private final CANSparkMax intake = new CANSparkMax(6, CANSparkMax.MotorType.kBrushless);
  private final CANSparkMax staging = new CANSparkMax(4, CANSparkMax.MotorType.kBrushless);
  private final CANSparkMax firing = new CANSparkMax(5, CANSparkMax.MotorType.kBrushless);
  private final CANSparkMax climb = new CANSparkMax(7, CANSparkMax.MotorType.kBrushless);
  //initialize sensors
  //initialize camera
  
  @Override
  public void robotInit() {
    //don't delete these
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
    //brute force code
	  //drivetrain.tankDrive(xbox.getRawAxis(1)/3, xbox.getRawAxis(5)/3);
    //input controls, change velocity
    if ((xbox.getRawAxis(2) > 0.2) && (velocity > -0.3)){
      velocity -= 0.01;
    }
    if ((xbox.getRawAxis(3) > 0.2) && (velocity < 0.3)){
      velocity += 0.01;
    }
    //output control, arcade or tank
    if (xbox.getRawAxis(0) > 0.1){
      //turn right
      if (xbox.getRawAxis(0) > 0.9){
        //full lock
        drivetrain.tankDrive(-1*velocity, velocity);
      }else{
        //partial turn
        drivetrain.tankDrive(velocity*(Math.abs(xbox.getRawAxis(0))/2), velocity);
      }
    }else if (xbox.getRawAxis(0) < -0.1){
      //turn left
      if (xbox.getRawAxis(0) < -0.9){
        //full lock
        drivetrain.tankDrive(velocity, -1*velocity);
      }else{
        //partial turn
        drivetrain.tankDrive(velocity, velocity*(Math.abs(xbox.getRawAxis(0))/2));
      }
    }else{
      //go straight
      drivetrain.tankDrive(velocity, velocity);
    }
    //move velocity towards 0
    if (velocity > 0){
      velocity -= 0.005;
    }
    if (velocity < 0){
      velocity += 0.005;
    }
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
