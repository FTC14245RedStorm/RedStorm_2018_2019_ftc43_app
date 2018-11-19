package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import RedStorm.Robot.Robot;

@Autonomous(name="Turning with Gyro", group="gyro")

public class FacingDepotAuto extends LinearOpMode {

    public Robot robot = new Robot();    // Create a new instance of the robot

    public void runOpMode () {


        robot.initialize(hardwareMap);  // Initialize robot
        robot.resetEncoders();          // Initialize encoder counts
        robot.runWithEncoders();        // Tell motors to run with encoders

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for start button
        waitForStart();

        // Put deploy code here

        while(opModeIsActive())  {
            telemetry.addData("Status", "opModeIsActive")

            robot.setDriveMotorPower(-0.5, -0.5);
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

        // Deposit the marker

        while (opModeIsActive()) {
            telemetry.addData("left distance", robot.getLeftDistance());
            telemetry.addData("right distance", robot.getRightDistance());
            telemetry.addData("front distance", robot.getFrontDistance());       // Drive along the wall until crater
            telemetry.update();

        }

        // Drive straight into crater/onto crater   `

    }




}

