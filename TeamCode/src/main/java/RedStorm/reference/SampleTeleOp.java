package RedStorm.reference; // These lines import necessary software for this op mode.

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Katelin Zichittella on 9/24/2016.
 */

@TeleOp // This line establishes this op mode as a teleop op mode and allows for it to be displayed
// in the drop down list on the Driver Station phone to be chosen to run.
//@Disabled // This line temporarily takes this op mode off of the drop down list until it is
// commented out.
@Disabled

public class SampleTeleOp extends OpMode { // This line establishes the name of the op mode and
    // extends the header file "OpMode" in order to create a teleop op mode.

    DcMotor motorLeft, motorRight; // This line establishes the names of the motors we will be
                                   // using.

    @Override
    public void init() { // This section of the code is the initialization routine the robot
                         // undergoes.

        motorLeft = hardwareMap.dcMotor.get("motorLeft");   // These lines establish a link between
        motorRight = hardwareMap.dcMotor.get("motorRight"); // the code and the hardware for the
                                                            // motors. The names in quotations are
                                                            // the names of the motors we set on
                                                            // the phone.

        motorLeft.setDirection(DcMotor.Direction.REVERSE); // This line reverses the left motor
                                                            // in order to negate the fact that the
                                                            // motors are placed on the robot to
                                                            // mirror each other.
    }

    @Override
    public void loop() { // This section of the code is the main teleop program that runs in a
                         // continuous loop.

        float left = gamepad1.left_stick_y;   // These lines establish the joystick input values as
        float right = gamepad1.right_stick_y; // the float variables "left" and "right", which
                                              // correspond to the left and right side of the robot.

        left = Range.clip(left, -1, 1);   // These lines clip the extreme ends of the joystick input
        right = Range.clip(right, -1, 1); // values in the resulting floats to avoid exceeding
                                          // values accepted by the program.

        left = (float) scaleInput(left);   // These lines scale the joystick input values in the
        right = (float) scaleInput(right); // resulting floats to the values in the array in the
                                           // double below, which are the only ones the program
                                           // accepts.

        setMotorPower(left, right); // This line is an implementation of the public void
        // "setMotorPower" below. It sets the power of the motors to the joystick input values in
        // the floats.
    }

    public void setMotorPower(double left, double right) { // This public void, when implemented
                                                           // above, sets the power of the motors.
                                                           // Whatever is inputted into each
                                                           // parameter above is then substituted
                                                           // into its corresponding spot in the
                                                           // public void.

        motorLeft.setPower(left); // This line sets the power of the left motor to the desired
                                  // power.
        motorRight.setPower(right); // This line sets the power of the right motor to the desired
                                    // power.
    }

    double scaleInput(double dVal) { // When implemented above, this double scales the joystick input values
                                     // in the floats.

        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

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