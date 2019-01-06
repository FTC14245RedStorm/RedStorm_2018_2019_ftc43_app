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
         //       robot.getLiftEncoderCount() < 135
         //       &&
                System.currentTimeMillis() - startDeployTime < 3000) {

        }
        telemetry.addData("Status ", "Deployed");
        telemetry.update();

        robot.setLiftMotorPower(0);

        Thread.sleep( 500);

        //we should be dismounted from space lander

        robot.setDriveMotorPower(.2, -.2);

        while (opModeIsActive() &&
                robot.getHeading() < 15) {

        }
        telemetry.addData("Status ", "Turned");
        telemetry.update();

        robot.resetEncoders();
        robot.runWithEncoders();
        robot.setDriveMotorPower(0, 0);

        //we should be out of handle
        double distanceToTravel = robot.calculateEncoderCounts(4);
        telemetry.addData("Status ", "DistTravCalc");
        telemetry.addData("distance to travel: ","%5.2f",distanceToTravel);
        telemetry.addData("enc count: ","%5.2f",robot.getDriveEncoderCount());
        telemetry.update();


        robot.setDriveMotorPower(-.2, -.2);
        while (opModeIsActive() &&
                robot.getDriveEncoderCount() < distanceToTravel); {
            telemetry.addData("Status ", "MovingAwayFromLander");
            telemetry.addData("Enc Count:", "%5.2f",robot.getDriveEncoderCount());
            telemetry.update();


        }

        telemetry.addData("Status ", "AwayFromLander");
        telemetry.update();


        robot.setDriveMotorPower(-.2, .2);

        double crntHeading = robot.getHeading();
        robot.initializeIMU();

        while (opModeIsActive() &&
                robot.getHeading() < 10) {
            telemetry.addData("Initial heading: ", "%5.2f", crntHeading);
            telemetry.addData("heading: ","%5.2f",robot.getHeading());
            telemetry.update();
        }
        telemetry.addData("Status ", "Straight");
        telemetry.update();

        distanceToTravel = robot.calculateEncoderCounts(24);
        robot.resetEncoders();
        robot.runWithEncoders();
        robot.setDriveMotorPower(-.8, -.8);
        while (opModeIsActive() &&
                robot.getDriveEncoderCount() < distanceToTravel) {
        }
        telemetry.addData("Status ", "TouchingCrater");
        telemetry.update();

        robot.setDriveMotorPower(0, 0);
        //Turning robot to extend arm into crater
        robot.initializeIMU();
        while (opModeIsActive() &&
                robot.getHeading() < 90) {
            telemetry.addData("Initial heading: ", "%5.2f", crntHeading);
            telemetry.addData("heading: ", "%5.2f", robot.getHeading());
            telemetry.update();
        }
        robot.setDriveMotorPower(0, 0);
        robot.setTeamMarkerArm(.1);  //Extend arm into crater







        // We should now be away from handle facing the lander
    }
}



