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


    public Robot robot = new Robot();    // Create a new instance of the robot!


    public void runOpMode () {

        // Initialize and set up the robot's drive motors
        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status:  ", "Initialized");
        telemetry.update();

        double wallDistanceToTravel = 0;
        double distanceFromWall;
        double wallDistanceTraveled = 0;
        String remember = new String();


        // Wait for the start button to be pushed!
        waitForStart();


            robot.resetEncoders();
            robot.runWithEncoders();

            wallDistanceToTravel = robot.calculateEncoderCounts(36);

            robot.setDriveMotorPower(0.5, 0.5);

            while (opModeIsActive() && wallDistanceToTravel >= wallDistanceTraveled) {

                distanceFromWall = robot.getRightDistance();
                wallDistanceTraveled = robot.getDriveEncoderCount();

                telemetry.addData("left distance", "(%.2f)",robot.getLeftDistance());
                telemetry.addData("right distance", "(%.2f)",robot.getRightDistance());
                telemetry.addData("front distance", "(%.2f)",robot.getFrontDistance());
                telemetry.update();

                if (distanceFromWall > 250) {
                    if (remember.equals("right")) {
                        robot.setDriveMotorPower(0.45, 0.55);
                    }
                    else {
                        if (remember.equals("left")) {
                            robot.setDriveMotorPower(0.55, 0.45);
                        }
                        else {
                            robot.setDriveMotorPower(0.5, 0.5);
                        }
                    }

                }

                if (distanceFromWall > 5.0) {
                    robot.setDriveMotorPower(0.55, 0.45);
                    telemetry.addLine("turning right - towards wall");
                    remember = "right";
                }
                else {
                    if (distanceFromWall < 3.0) {
                        robot.setDriveMotorPower(0.45, 0.55);
                        telemetry.addLine("turning left - away from wall ");
                        remember = "left";
                    }
                    else {
                        robot.setDriveMotorPower(0.5, 0.5);
                        telemetry.addLine("not turning going straight");
                        remember = "straight";
                    }
                }

            }

 



    }


}