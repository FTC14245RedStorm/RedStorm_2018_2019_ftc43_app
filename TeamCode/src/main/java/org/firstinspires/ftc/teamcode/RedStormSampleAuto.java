package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import RedStorm.Robot.Robot;

@Autonomous (name="Sample Auto", group="Samples")
@Disabled

/**
 * Created by Steve Kocik as a sample for RedStorm to build off of...
 */

public class RedStormSampleAuto extends LinearOpMode {


    public Robot robot = new Robot();    // Create a new instance of the robot

    @Override
    public void runOpMode() {


        // Initialize and set up the robot's drive motors
        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status ", "Initialized");
        telemetry.update();

        // Wait for the start button to be pushed
        waitForStart();

        // Calculate the number of encoder counts to travel for the defined distance

        double encoderCountstoTravel = robot.calculateEncoderCounts(24);   //  Calculate the number of encoder counts to travel 24 inches
        double liftEncoderCountstoTravel = robot.calculateEncoderCounts(4);

        // While the FacingCrater period is still active AND the robot has not reached the number
        // of encoder counts to travel 24 inches
        while(opModeIsActive()) {

            telemetry.addData("Status ", "opModeIsActive");

            if (robot.getDriveEncoderCount() < encoderCountstoTravel) {
                robot.setDriveMotorPower(0.5, 0.50);   // Set power to 50%
                telemetry.addData("encoderCount", "(%.0f)",robot.getDriveEncoderCount());
            }
            else {
                robot.setDriveMotorPower(0.0,0.0);

                // add code here to drop the team marker
            }

            telemetry.update();
        }


        robot.setDriveMotorPower(0.0,0.0);         // Motors stop

        while(opModeIsActive()) {

            telemetry.addData("Status ", "opModeIsActive");

            if (robot.getLiftEncoderCount() < liftEncoderCountstoTravel) {
                robot.setLiftMotorPower(0.5);
                telemetry.addData("encoderCount", "(%.0f)",robot.getLiftEncoderCount());
            }
        }
/**
       robot.setTeamMarkerArm(0.85);
       robot.setTeamMarkerGrip(0.5);
*/
    }
}