package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.TypeConversion;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import RedStorm.Robot.Robot;

@Autonomous(name="Drive Along Wall", group="distance")
@Disabled

public class driveAlongWall extends LinearOpMode {


    public Robot robot = new Robot();    // Create a new instance of the robot


    public void runOpMode () {

        // Initialize and set up the robot's drive motors
        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status:  ", "Initialized");
        telemetry.update();

        // Wait for the start button to be pushed
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("left distance", robot.getLeftDistance());
            telemetry.addData("right distance", robot.getRightDistance());
            telemetry.addData("front distance", robot.getFrontDistance());
            telemetry.update();

            robot.setDriveMotorPower(0.5, 0.5);

            while (opModeIsActive() && wallDistanceToTravel >= wallDistanceTraveled) {

                    if (distanceFromWall < 5) {
                        robot.setDriveMotorPower(0.6, 0.5);
                    }

                    else {
                        robot.setDriveMotorPower(0.5, 0.5);
                    }

                    if (distanceFromWall < 7) {
                        robot.setDriveMotorPower(0.5, 0.6);
                    }

                    else {
                        robot.setDriveMotorPower(0.5, 0.5);
                    }
            }

    }


    }


}