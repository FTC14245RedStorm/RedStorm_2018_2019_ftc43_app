package org.firstinspires.ftc.teamcode;
// These lines import necessary software for this op mode.
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import RedStorm.Robot.Robot;


// This line establishes this op mode as a teleop op mode and allows for it to be displayed
// in the drop down list on the Driver Station phone to be chosen to run.
@TeleOp
@Disabled



// This line temporarily takes this op mode off of the drop down list until it is
// commented out.
//@Disabled
// This line establishes the name of the op mode and
// extends the header file "OpMode" in order to create a teleop op mode.  just for fun

public class FirstTeleop extends OpMode{
    // Create an instance of Robot and store it into robot
    public Robot robot = new Robot();
    /**
     * init - initializes the robot and puts out a message that tells when
     * the robot is initialized
     */
    @Override
    public void init() {
        robot.initialize(hardwareMap);
        telemetry.addData("Initialized", true);
        telemetry.update();
    }
    /** loop - called in a continuous loop by the FTC SDK, this can be considered the "main" method
     *  for teleop
     */
    @Override
    public void loop() {
        /* For this robot, gamepad1 will control the movement of the robot
           the left Y stick, will control the left drive side motors, the right Y
           will control the right drive side motors.

           The values returned by left_stick_y and right_stick_y are negated because:
           - When the stick is pushed away from the driver, the value returned is negative.
           - When the stick is pulled towards the driver, the value returned is positive.

           First get the value for the left Y and right Y sticks
          */
        double left  = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;
        double lift  =  gamepad2.right_stick_y;
        // double liftDown = gamepad1.dpad_down;
        // double liftUp = gamepad1.dpad_up;
        /* Insure that the values from the gamepad for left and right will
           always be between -1.0 and 1.0.  This is done since motor powers
           can only be between -1.0 (100% reverse) and 1.0 (100% forward)
         */
        left = Range.clip(left, -1, 1);
        right = Range.clip(right, -1, 1);
        lift = Range.clip(lift,  -1, 1);

        /* Smooth the right and left powers.  Smoothing will give the driver better control.
           See the smoothPower method for more information.
         */
        left  = smoothPower(left);
        right = smoothPower(right);
        lift = smoothPower(lift);
        /* Set the motor power for the robot.
         */
        robot.setDriveMotorPower(left, right);

      //  robot.setLiftMotorPower(lift);
    }
    /**
     * smoothPower will attempt to smooth or scale joystick input when driving the
     * robot in teleop mode.  By smoothing the joystick input more controlled movement
     * of the robot will occur, especially at lower speeds.
     * <br><br>
     * To scale the input, 16 values are used that increase in magnitude, the algorithm
     * will determine where the input value roughly falls in the array by multiplying it
     * by 16, then will use the corresponding array entry from the scaleArray variable to
     * return a scaled value.
     * <br><br>
     * <b>Example 1:</b> dVal (the input value or value passed to this method) is set to 0.76
     * <br>
     * Stepping through the algorithm
     * <ol>
     * <li> 0.76*16 = 12.16, but because we cast the calculations as an integer (int)
     * we lose the .16 so the value just is 12, variable index now contains 12.  <b>Note:</b>
     * the index variable will tell us which of the array entries in the scaleArray array to
     * use.</li>
     * <li> Check if the index is negative (less than zero), in this example the
     * variable index contains a positive 12</li>
     * <li> Check if the variable index is greater than 16, this is done so the
     * algorithm does not exceed the number of entries in the scaleArray array</li>
     * <li> Initialize the variable dScale to 0.0 (not really needed but we are
     * just being safe)</li>
     * <li> If dVal (value passed to this method) was initially negative, then
     * set the variable dScale to the negative of the scaleArray(index), in this example
     * dVal was initially 0.76 so not negative</li>
     * <li> If dVal (value passed to this method) was initially positive, then
     * set the variable dScale to the scaleArray(index), since index is 12, then
     * scaleArray(12) = 0.60.  <b>Remember, in java the first array index is 0,
     * this is why scaleArray(12) is not 0.50</b></li>
     * <li> Return the dScale value (0.60)</li>
     * </ol>
     * <p>
     * <br><br>
     * <b>Example 2</b> dVal (the input value or value passed to this method) is set to -0.43
     * <br>
     * Stepping through the algorithm
     * <ol>
     * <li> -0.43*16 = -6.88, but because we cast the calculations as an integer (int)
     * we lose the .88 so the value just is -6, variable index now contains -6.  <b>Note:</b>
     * the index variable will tell us which of the array entries in the scaleArray array to
     * use.</li>
     * <li> Check if the index is negative (less than zero), in this example the
     * variable index is negative, so make the negative a negative (essentially
     * multiplying the variable index by -1, the variable index now contains 6</li>
     * <li> Check if the variable index is greater than 16, this is done so the
     * algorithm does not exceed the number of entries in the scaleArray array</li>
     * <li> Initialize the variable dScale to 0.0 (not really needed but we are
     * just being safe)</li>
     * <li> If dVal (value passed to this method) was initially negative, then
     * set the variable dScale to the negative of the scaleArray(index), in this example
     * dVal was initially -0.43, so make sure to return a negative value of scaleArray(6).
     * scaleArray(6) is equal to 0.18 and the negative of that is -0.18 <b>Remember,
     * in java the first array index is 0, this is why scaleArray(6) is not 0.15</b></li>
     * <li> Return the dScale value (-0.18)</li>
     * </ol>
     *
     * @param dVal the value to be scaled -between -1.0 and 1.0
     * @return the scaled value
     * <B>Author(s)</B> Unknown - copied from internet
     */
    double smoothPower(double dVal) {
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
