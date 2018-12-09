package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import RedStorm.Robot.Robot;

@Autonomous
@Disabled
public class BadFacingCrater extends LinearOpMode {

    public Robot robot = new Robot();    // Create a new instance of the robot

    public void runOpMode () throws InterruptedException {


        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status ", "Initialized");
        telemetry.update();


        double startDeployTime = System.currentTimeMillis();

        robot.setLiftServo(.6);

        Thread.sleep(1000);
        waitForStart();

        robot.setLiftMotorPower(-.2);

        while (opModeIsActive() &&
                robot.getLiftEncoderCount() < 135
                &&
                System.currentTimeMillis() - startDeployTime < 3000) {

        }

        robot.setLiftMotorPower(0);

        Thread.sleep(1000);

        //we should be dismounted from space lander

        robot.setDriveMotorPower(.2, -.2);

        while (opModeIsActive() &&
                robot.getHeading() < 10) {

        }


        robot.setDriveMotorPower(0, 0);

        //we should be out of handle


        robot.setDriveMotorPower(-.2, -.2);
        while (opModeIsActive() &&
                robot.getRightDriveEncoderCounts() < 4) {
        }

        while (opModeIsActive() &&
                robot.getLeftDriveEncoderCounts() < 4) {

        }
        robot.setDriveMotorPower(0, 0);


        robot.setDriveMotorPower(-.2, .2);

        while (opModeIsActive() &&
                robot.getHeading() > 0) {
        }


        robot.setDriveMotorPower(0, 0);

        //robot is out of lander












        }





}

