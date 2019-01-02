package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Katelin Zichittella on 2/22/2017.
 */

@TeleOp
@Disabled

public class MecanumTeleOp extends OpMode {

    DcMotor motorBackLeft, motorBackRight, motorFrontLeft, motorFrontRight;

    @Override
    public void init() { /*This is the initialization routine that the robot undergoes. */

        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");           // These lines establish a link between
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");         // the code and the hardware for the
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");         // motors. The names in quotations are
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");       //the names of the motors we set on the phone.

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);             //These lines reverse the right motors
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);            //in order to negate the fact that the
                                                                            //motors are placed on the robot
                                                                            //to mirror each other.
    }

    @Override
    public void loop() {

        float backLeft = gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;    // These lines establish the joystick input values as
        float backRight = gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;   // the float variables "backLeft", "backRight", "frontLeft", and "frontRight", which
        float frontLeft = gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;   //correspond to the back left, back right, front left,
        float frontRight = gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;  // and front right wheels of the robot.

        backLeft = Range.clip(backLeft, -1, 1);     // These lines clip the extreme ends of the joystick input
        backRight = Range.clip(backRight, -1, 1);   // values in the resulting floats to avoid exceeding
        frontLeft = Range.clip(frontLeft, -1, 1);   // values accepted by the program.
        frontRight = Range.clip(frontRight, -1, 1);

        backLeft = (float) scaleInput(backLeft);        // These lines scale the joystick input values in the
        backRight = (float) scaleInput(backRight);      // resulting floats to the values in the array in the
        frontLeft = (float) scaleInput(frontLeft);      // double below, which are the only ones the program
        frontRight = (float) scaleInput(frontRight);    // accepts.

        setMotorPower(backLeft, backRight, frontLeft, frontRight);  // This line is an implementation of the public void
        // "setMotorPower" below. It sets the power of the motors to the joystick input values in
        // the floats.
    }

    @Override
    public void stop() {

    }

    public void setMotorPower(float backLeft, float backRight, float frontLeft, float frontRight) { // This public void, when implemented
                                                                                                    // above, sets the power of the four motors.
                                                                                                    // Whatever is inputted into each of the four
                                                                                                    // parameters above is then substituted
                                                                                                    // into its corresponding spot in the
                                                                                                    // public void.

        motorBackLeft.setPower(backLeft);   // These lines set the power of each motor to the desired power.
        motorBackRight.setPower(backRight);
        motorFrontLeft.setPower(frontLeft);
        motorFrontRight.setPower(frontRight);
    }

    double scaleInput(double dVal) {        // When implemented above, this double scales the joystick input values
                                            // in the floats.

        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}