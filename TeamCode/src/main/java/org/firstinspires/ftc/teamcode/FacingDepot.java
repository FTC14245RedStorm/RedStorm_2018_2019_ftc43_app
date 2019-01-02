package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import RedStorm.Robot.Robot;

@Autonomous(name="FacingDepot", group="FacingCrater")

public class FacingDepot extends LinearOpMode {

    public Robot robot = new Robot();
    String remember = new String();

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


        robot.setDriveMotorPower(-.2, 0);

        double crntHeading = robot.getHeading();
        robot.initializeIMU();

        while (opModeIsActive() &&
                robot.getHeading() < 3) {
            telemetry.addData("Initial heading: ", "%5.2f", crntHeading);
            telemetry.addData("heading: ","%5.2f",robot.getHeading());
            telemetry.update();
        }
        telemetry.addData("Status ", "Straight");
        telemetry.update();

        distanceToTravel = robot.calculateEncoderCounts(42);
        robot.resetEncoders();
        robot.runWithEncoders();
        robot.setDriveMotorPower(-.8, -.8);
        while (opModeIsActive() &&
                robot.getDriveEncoderCount() < distanceToTravel) {
        }
        telemetry.addData("Status ", "InDepot");
        telemetry.update();

        robot.setDriveMotorPower(0, 0);

        robot.initializeIMU();
        robot.setDriveMotorPower(.5, 0);
        while (opModeIsActive() &&
                robot.getHeading() < 50) {
            telemetry.addData("Initial heading: ", "%5.2f", crntHeading);
            telemetry.addData("heading: ","%5.2f",robot.getHeading());
            telemetry.update();
        }

        robot.setDriveMotorPower(0, 0);
        robot.setTeamMarkerArm(0.1);

        Thread.sleep(1000);

        robot.setTeamMarkerGrip(.5);

        Thread.sleep(1000);

        robot.setTeamMarkerArm(-.5);

        Thread.sleep(1000);
        robot.resetEncoders();
        robot.runWithEncoders();

       double wallDistanceToTravel = robot.calculateEncoderCounts(70);

        robot.setDriveMotorPower(0.75, 0.75);
        double wallDistanceTraveled = 0;


        while (opModeIsActive() && wallDistanceToTravel >= wallDistanceTraveled) {


            double distanceFromWall = robot.getRightDistance();
            wallDistanceTraveled = robot.getDriveEncoderCount();

            telemetry.addData("left distance", "(%.2f)", robot.getLeftDistance());
            telemetry.addData("right distance", "(%.2f)", robot.getRightDistance());
            telemetry.addData("front distance", "(%.2f)", robot.getFrontDistance());
            telemetry.update();

            if (distanceFromWall > 250) {
                if (remember.equals("right")) {
                    robot.setDriveMotorPower(0.5, 0.55);
                } else {
                    if (remember.equals("left")) {
                        robot.setDriveMotorPower(0.55, 0.5);
                    } else {
                        robot.setDriveMotorPower(0.5, 0.5);
                    }
                }

            }

            if (distanceFromWall > 5.0) {
                robot.setDriveMotorPower(0.525, 0.5);
                telemetry.addLine("turning right - towards wall");
                remember = "right";
            } else {
                if (distanceFromWall < 3.0) {
                    robot.setDriveMotorPower(0.5, 0.525);
                    telemetry.addLine("turning left - away from wall ");
                    remember = "left";
                } else {
                    robot.setDriveMotorPower(0.5, 0.5);
                    telemetry.addLine("not turning going straight");
                    remember = "straight";
                }
            }


        }

    }
}



