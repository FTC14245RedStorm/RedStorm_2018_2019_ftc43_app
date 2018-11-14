package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import RedStorm.Robot.Robot;

@Autonomous(name="Autonomous", group="autonomous")

public class autonomous extends LinearOpMode {

    public Robot robot= new Robot();

    @Override
    public void runOpMode() {
        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status ", "Initialized");
        telemetry.update();

        waitForStart();

        robot.setLiftMotorPower(-.4);

        while (opModeIsActive() &&
                robot.getLiftEncoderCount() < 40) {

        }

        robot.setLiftMotorPower(0);

        //we should be dismounted from space lander

        robot.setDriveMotorPower(-.4, .4);

        while (opModeIsActive() &&
                robot.getLeftDriveEncoderCounts() < 90) {

        }
        while (opModeIsActive() &&
                robot.getRightDriveEncoderCounts() < 90) {

        }

        robot.setDriveMotorPower(0, 0);

        //we should be out of handle

        robot.setDriveMotorPower(-.3, -.3);

        while (opModeIsActive() &&
                robot.getLeftDriveEncoderCounts() < 30) {
        }

        while (opModeIsActive() &&
                robot.getRightDriveEncoderCounts() <30) {
        }
        robot.setDriveMotorPower(0, 0);

        //we should now be away from handle facing the lander

        }

    }

