package org.firstinspires.ftc.teamcode;

import RedStorm.Robot.Robot;


public void (){
double imu=hardwareMap.get(BNO055IMU.class,"imu");
imu.initialize(parameters);

String formatAngle(AngleUnit angleUnit,double angle){
return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit,angle));}

String formatDegrees(double degrees){
return String.format(Locale.getDefault(),"%.1f",AngleUnit.DEGREES.normalize(degrees));

double gyroHeading = imu;
}

DcMotor rightDrive;
        rightDrive = hardwareMap.(dcMotor).get("rightDrive);
        right = hardwareMap.dcMotor.get("left");

DcMotor leftDrive;
        leftDrive = hardwareMap.(dcMotor).get("leftDrive");
        left = hardwareMap.dcMotor.get("left");

double gyroHeading = imu;
}



public class turningwithgyro {

/// program to turn the robot right if it is facing 0 degrees and needs to turn 90 degrees

    double hardwareMap =

    @Override
    boolean goalReached = true;
    public void loop() {
        gyroHeading = 0
        Robot.setDriveMotorPower("left", "right");
        Robot.initialize(hardwareMap);


        if (gyroHeading == 90) {
            goalReached = true; }

        if (goalReached) {
            leftDrive.setPower(0);
            rightDrive.setPower(0); }

        else () {
            leftDrive.setPower(20);
            rightDrive.setPower(0); }

    }
}
