package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Katelin Zichittella on 2/27/2017.
 */

public abstract class MecanumAutonomousHeader extends LinearOpMode {

    DcMotor motorBackLeft, motorBackRight, motorFrontLeft, motorFrontRight;

    public void initialize() {

        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
    }

    public void strafe(double degrees, double power) {

        if (opModeIsActive()) {

            motorBackLeft.setPower((Math.sin(degrees) * power) - (Math.cos(degrees) * power));
            motorBackRight.setPower((Math.sin(degrees) * power) + (Math.cos(degrees) * power));
            motorFrontLeft.setPower((Math.sin(degrees) * power) + (Math.cos(degrees) * power));
            motorFrontRight.setPower((Math.sin(degrees) * power) - (Math.cos(degrees) * power));
        }
    }

    public void strafe2(double degrees, double power) {

        if (opModeIsActive()) {

            motorBackLeft.setPower(power * Math.sin(degrees + 45));
            motorBackRight.setPower(power * Math.cos(degrees + 45));
            motorFrontLeft.setPower(power * Math.cos(degrees + 45));
            motorFrontRight.setPower(power * Math.sin(degrees + 45));
        }
    }

    public void pause() {

        if (opModeIsActive()) {

            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }
    }
}