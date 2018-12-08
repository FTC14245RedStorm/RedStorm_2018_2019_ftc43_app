package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import RedStorm.Robot.Robot;

@Autonomous(name="FacingCrater", group="FacingCrater")

public class FacingCrater extends LinearOpMode {

    public Robot robot = new Robot();

    @Override
    public void runOpMode() throws InterruptedException{
        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status ", "Initialized");
        telemetry.update();

        waitForStart();

        double startDeployTime = System.currentTimeMillis();

        robot.setLiftServo(.6);

        Thread.sleep(250);

        robot.setLiftMotorPower(-.2);

        while (opModeIsActive() &&
                robot.getLiftEncoderCount() < 135
                &&
                System.currentTimeMillis() - startDeployTime < 3000) {

        }

        robot.setLiftMotorPower(0);

        Thread.sleep( 500);

        //we should be dismounted from space lander

        robot.setDriveMotorPower(.2, -.2);

        while (opModeIsActive() &&
                robot.getHeading() < 15) {

        }


        robot.setDriveMotorPower(0, 0);

        //we should be out of handle
        double distanceToTravel = robot.calculateEncoderCounts(4);


        robot.setDriveMotorPower(-.2, -.2);
        while (opModeIsActive() &&
                robot.getDriveEncoderCount() < distanceToTravel); {
        }



        robot.setDriveMotorPower(-.2, .2);


        while (opModeIsActive() &&
                robot.getHeading() > 0) {
        }
        distanceToTravel = robot.calculateEncoderCounts(24);
        robot.resetEncoders();
        robot.setDriveMotorPower(-.8, -.8);
        while (opModeIsActive() &&
                robot.getDriveEncoderCount() < distanceToTravel) {
        }


        //we should now be away from handle facing the lander

    }
}



