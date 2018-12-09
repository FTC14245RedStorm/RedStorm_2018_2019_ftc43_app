package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import RedStorm.Robot.Robot;

@Autonomous
@Disabled
public class BadFacingDepot extends LinearOpMode {

    public Robot robot = new Robot();    // Create a new instance of the robot

    public void runOpMode () throws InterruptedException {


        robot.initialize(hardwareMap);  // Initialize robot
        robot.resetEncoders();          // Initialize encoder counts
        robot.runWithEncoders();        // Tell motors to run with encoders

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for start button
        waitForStart();

        double startDeployTime = System.currentTimeMillis();

        robot.setLiftServo(.6);

        Thread.sleep(1000);

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

        double totalEncoderCount = robot.calculateEncoderCounts(36);

        robot.setDriveMotorPower(0.5, 0.5);

        while(opModeIsActive() && robot.getLeftDriveEncoderCounts() < totalEncoderCount){
            telemetry.addData("Status", "opModeIsActive");

            telemetry.addData("Left Drive Encoder Counts", "(%.0f)",robot.getLeftDriveEncoderCounts());
            telemetry.addData("Right Drive Encoder Counts", "(%.0f)",robot.getRightDriveEncoderCounts());   // Drive backwards until arrived at depot
            telemetry.update();

        }
        robot.setDriveMotorPower(0.0, 0.0);  // Robot stops

        

        robot.setDriveMotorPower(-0.5, 0.5);  // Robot turns 90 degrees to deposit team marker

        while (opModeIsActive() &&
                robot.getHeading() < 90) {
        }
        robot.setDriveMotorPower(0,0);  //stop turning Robot

        // deposit marker

        while (opModeIsActive()) {
            telemetry.addData("left distance", robot.getLeftDistance());
            telemetry.addData("right distance", robot.getRightDistance());
            telemetry.addData("front distance", robot.getFrontDistance());       // Drive along the wall until crater and drive straight into crater
            telemetry.update();

        }



    }




}

