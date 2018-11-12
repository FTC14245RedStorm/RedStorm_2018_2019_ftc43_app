package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import RedStorm.Robot.Robot;
public class newTurningWithGyro {
    public DcMotor motorFrontLeft = null;
    public DcMotor motorFrontRight = null;
    public ModernRoboticsI2cRangeSensor mrRangeFront;
    public BNO055IMU imu;

    public HardwareMap hwMap =  null;

    public Orientation angles;
    public BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    public void initialize(HardwareMap ahwMap) {

        hwMap = ahwMap;

        motorFrontLeft = hwMap.get(DcMotor.class, "left_Front");
        motorFrontRight = hwMap.get(DcMotor.class, "right_Front");

        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);

        mrRangeFront = hwMap.get(ModernRoboticsI2cRangeSensor.class, "mr_range_front");

        imu = hwMap.get(BNO055IMU.class, "imu");
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.mode = BNO055IMU.SensorMode.IMU;
        imu.initialize(parameters);
    }

    public float getHeading() {

        float heading;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        heading = Math.abs(angles.firstAngle);

        return heading;
    }

    if (angles == 0) {
        motorFrontLeft.setPower(.2);
        motorFrontRight.setPower(.2);
    }

    public float getHeading() {

        float heading;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        heading = Math.abs(angles.firstAngle);

        return heading;
    }

    if (angles < 90) {
        motorFrontLeft.setPower(.2);
        motorFrontRight.setPower(.2);
    }

    public float getHeading() {

        float heading;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        heading = Math.abs(angles.firstAngle);

        return heading;
    }

    if (angles == 90) {
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
    }

}
}
