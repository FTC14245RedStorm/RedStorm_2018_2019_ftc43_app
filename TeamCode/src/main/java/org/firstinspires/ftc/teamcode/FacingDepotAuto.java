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
            telemetry.addData("Right Drive Encoder Counts", "(%.0f)",robot.getRightDriveEncoderCounts());

            telemetry.update();

        }
        robot.setDriveMotorPower(0.0, 0.0);

        robot.setDriveMotorPower(-0.5, 0.5);

        while (opModeIsActive() &&
                robot.getHeading() < 90) {
        }
        robot.setDriveMotorPower(0,0);//stop turning Robot

        // Put deposit code here


    }




}

