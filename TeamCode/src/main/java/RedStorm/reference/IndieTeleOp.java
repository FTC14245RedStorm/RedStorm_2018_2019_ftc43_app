package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Alex Snyder on 3/2/18.
 */

@TeleOp
@Disabled

public class IndieTeleOp extends OpMode {

    public IndieRobot robot = new IndieRobot();

    /* Define variables */
    float LeftBack;   // Power for left back motor
    float RightBack;  // Power for right back motor
    float LeftFront;  // Power for left front motor
    float RightFront; // Power for right front motor
    float glyphLift;  // Power for the glyph lift motor
    float relic;      // Power for relic motor
    float maxpwr;     // Maximum power of the four motors
    double powerFactor = 1;
    boolean nudging = false;
    int nudgeCount = 0;
    int timesPivoted = 0;
    public boolean currStateRightBumper1 = false;
    public boolean prevStateRightBumper1 = false;
    public boolean currStateLeftBumper1  = false;
    public boolean prevStateLeftBumper1  = false;
    public boolean currStateRightTrigger = false;
    public boolean prevStateRightTrigger = false;
    public boolean currStateLeftTrigger  = false;
    public boolean prevStateLeftTrigger  = false;
    public boolean currStateY = false;
    public boolean prevStateY = false;
    public boolean currStateX = false;
    public boolean prevStateX = false;
    public boolean currStateB = false;
    public boolean prevStateB = false;
    public boolean currStateA = false;
    public boolean prevStateA = false;

    @Override
    public void init() {

        robot.initialize(hardwareMap);

        telemetry.addData("Initialized", true);
        telemetry.update();
    }

    @Override
    public void start() {

        robot.initializeServosTeleOp();
    }

    @Override
    public void loop() {

        // "Mecanum Drive" functionality
        LeftBack = -gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;
        RightBack = -gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
        LeftFront = -gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
        RightFront = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;

        maxpwr = findMaxPower(LeftBack, LeftFront, RightBack, RightFront);

        LeftBack = LeftBack / maxpwr;
        RightBack = RightBack / maxpwr;
        LeftFront = LeftFront / maxpwr;
        RightFront = RightFront / maxpwr;

        LeftBack = (float) scaleInput(LeftBack);
        RightBack = (float) scaleInput(RightBack);
        LeftFront = (float) scaleInput(LeftFront);
        RightFront = (float) scaleInput(RightFront);

        robot.setDriveMotorPower(LeftFront * 0.95 * powerFactor, RightFront * 0.95 * powerFactor,
                LeftBack * 0.95 * powerFactor, RightBack * 0.95 * powerFactor);


        // "Power Factor" functionality
        currStateLeftBumper1 = gamepad1.left_bumper;
        if (currStateLeftBumper1 && currStateLeftBumper1 != prevStateLeftBumper1) {

            powerFactor = 0.5;
            prevStateLeftBumper1 = currStateLeftBumper1;
        }
        else if (!currStateLeftBumper1 && currStateLeftBumper1 != prevStateLeftBumper1) {

            prevStateLeftBumper1 = currStateLeftBumper1;
        }
        currStateRightBumper1 = gamepad1.right_bumper;
        if (currStateRightBumper1 && currStateRightBumper1 != prevStateRightBumper1) {

            powerFactor = 1;
            prevStateRightBumper1 = currStateRightBumper1;
        }
        else if (!currStateRightBumper1 && currStateRightBumper1 != prevStateRightBumper1) {

            prevStateRightBumper1 = currStateRightBumper1;
        }


        // "Nudging" functionality
        if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right) { // "If any
            // of the dpad buttons are pressed on the
            // first controller...

            if (!nudging) { // ...if nudging is false (the robot is still being nudged)...

                if (gamepad1.dpad_up) robot.setDriveMotorPower(0.75, 0.75, 0.75, 0.75); // ...if 'up' was pressed, the robot moves forward...

                else if (gamepad1.dpad_down) robot.setDriveMotorPower(-0.75, -0.75, -0.75, -0.75); // ...else if 'down' was pressed, the robot
                    // moves backward...

                else if (gamepad1.dpad_left) robot.setDriveMotorPower(-0.8, 0.8, 0.8, -0.8); // ...else if 'left' was pressed, the robot
                    // strafes left...

                else if (gamepad1.dpad_right) robot.setDriveMotorPower(0.8, -0.8, -0.8, 0.8); // ...else if 'right' was pressed, the robot
                // strafes right...
            }

            nudgeCount++; // ...after this one loop cycle, the number of the nudgeCount goes up by one...

            if (nudgeCount > 5) { // ...if the number of the nudgeCount goes above 5...

                nudging = true; // ...nudging is true, so the robot cannot nudge anymore. This allows for 5 loop cycles
                // of movement...
            }
        }
        else { // ...else if nudging is true (the robot is no longer being nudged)...

            nudging = false; // ...nudging is returned to false, which allows nudging again if any of the dpad buttons
            // are pressed on the first controller...

            nudgeCount = 0; // ...and nudgeCount is reset to 0."
        }


        // "Set Glyph Lift Motor Power" functionality
        glyphLift = -gamepad2.right_stick_y;
        glyphLift = Range.clip(glyphLift, -1, 1);
        glyphLift = (float) scaleInput(glyphLift);
        robot.setGlyphLiftMotorPower(glyphLift * 0.90);


        // "Set Glyph Intake Motor Power" functionality
        if (gamepad2.right_bumper) {

            robot.setGlyphIntakeMotorPower(1.0);
        }
        else if (gamepad2.left_bumper){

            robot.setGlyphIntakeMotorPower(-0.75);
        }
        else {

            robot.setGlyphIntakeMotorPower(0.0);
        }


        // "Glyph Pivot Carry/Deposit" functionality
        if (gamepad2.right_trigger > 0.5) {

            currStateRightTrigger = true;
        }
        else {

            currStateRightTrigger = false;
        }
        if (currStateRightTrigger && currStateRightTrigger != prevStateRightTrigger) {

            timesPivoted++;
            if (timesPivoted % 2 == 1) {  // If timesPivoted is an odd number (it has been pressed once, three times, five times...)

                robot.glyphPivotDeposit();
            }
            else if (timesPivoted % 2 == 0) { // If timesPivoted is an even number (it has been pressed twice, four times, six times...)

                robot.glyphPivotCarry();
            }
            prevStateRightTrigger = currStateRightTrigger;
        }
        else if (!currStateRightTrigger && currStateRightTrigger != prevStateRightTrigger) {

            prevStateRightTrigger = currStateRightTrigger;
        }


        // "Glyph Pivot Rest" functionality
        if (gamepad2.left_trigger > 0.5) {

            currStateLeftTrigger = true;
        }
        else {

            currStateLeftTrigger = false;
        }
        if (currStateLeftTrigger && currStateLeftTrigger != prevStateLeftTrigger) {

            robot.glyphPivotRest();
            prevStateLeftTrigger = currStateLeftTrigger;
        }
        else if (!currStateLeftTrigger && currStateLeftTrigger != prevStateLeftTrigger) {

            prevStateLeftTrigger = currStateLeftTrigger;
        }


        // "Set Relic Motor Power" functionality
        relic = gamepad2.left_stick_y;
        relic = Range.clip(relic, -1, 1);
        relic = (float) scaleInput(relic);

        // If power is negative, then 1/2 the power for the arm coming back, else use 3/4 of the power.
        if (relic > 0.0) {
            robot.setRelicMotorPower(relic * 0.5);
        } else {
            robot.setRelicMotorPower(relic * 0.75);
        }



        // "Relic Wrist Up" functionality
        currStateY = gamepad2.y;
        if (currStateY && currStateY != prevStateY) {

            robot.wristUp();
            prevStateY = currStateY;
        }
        else if (!currStateY && currStateY != prevStateY) {

            prevStateY = currStateY;
        }


        // "Relic Wrist Down" functionality
        currStateA = gamepad2.a;
        if (currStateA && currStateA != prevStateA) {

            robot.wristDown();
            prevStateA = currStateA;
        }
        else if (!currStateA && currStateA != prevStateA) {

            prevStateA = currStateA;
        }


        // "Relic Gripper Close" functionality
        currStateX = gamepad2.x;
        if (currStateX && currStateX != prevStateX) {

            robot.gripperClose();
            prevStateX = currStateX;
        }
        else if (!currStateX && currStateX != prevStateX) {

            prevStateX = currStateX;
        }


        // "Relic Gripper Open" functionality
        currStateB = gamepad2.b;
        if (currStateB && currStateB != prevStateB && !gamepad2.start) {

            robot.gripperOpen();
            prevStateB = currStateB;
        }
        else if (!currStateB && currStateB != prevStateB) {

            prevStateB = currStateB;
        }
    }

    @Override
    public void stop() {

    }

    /**
     * scaleInput will attempt to smooth or scale joystick input when driving the
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
    double scaleInput(double dVal) {
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

    /**
     * findMaxPower - finds the maximum power of four power values
     *
     * @param pwr1 first power
     * @param pwr2 second power
     * @param pwr3 third power
     * @param pwr4 fourth power
     * @return maximum power of the four values
     * <B>Author(s):</B> Jason Sember and Steeeve
     */
    float findMaxPower(float pwr1, float pwr2, float pwr3, float pwr4) {

        float maxpwrA = Math.max(Math.abs(pwr1), Math.abs(pwr2));
        float maxpwrB = Math.max(Math.abs(pwr3), Math.abs(pwr4));
        float maxpwr = Math.max(Math.abs(maxpwrA), Math.abs(maxpwrB));

        if (maxpwr > 1.0) {

            return maxpwr;
        } else {

            return 1;
        }
    }
}