package RedStorm.reference; // These lines import necessary software for this op mode.

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Robotics Club on 10/27/15.
 */

public class TrueProphetTeleOp extends OpMode { // This line establishes the name of the op mode and extends
    // the header file "OpMode" in order to create a teleop op mode.

    DcMotor motorArm, motorLift, motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight, redLED, blueLED; // These lines
    Servo servoClimberLeft, servoClimberRight, servoHook, servoClimberFront, servoAllClear; // establish the names of the motors,
    GyroSensor sensorGyro;                                                                  // servos, and sensors we will be using.
    TouchSensor sensorTouch;

    double x = -1;              // These lines establish the names and values of variables having to do with
    boolean isRed = true;       // the LED lights and their control.
    boolean goingDown = false;
    boolean lightToggle = true;

    boolean nudging = false;    // These lines establish the names and values of variables having to do with
    boolean armNudging = false; // the nudging feature.
    int nudgeCount = 0;
    int armNudgeCount = 0;

    boolean armReset = false; // This line establishes the name and value of a boolean having to do with
    // the touch sensor control of the arm.

    double SERVO_CLIMBER_LEFT_POSITION = 1.0;  // These lines establish the names and values of doubles
    double SERVO_CLIMBER_RIGHT_POSITION = 0.0; // having to do with the initialization positions of the
    double SERVO_HOOK_POSITION = 0.0;          // servos.
    double SERVO_CLIMBER_FRONT_POSITION = 1.0;

    @Override
    public void start() { // This section of the code is the initialization routine the robot undergoes.

        // Motors \\                                                  // These lines establish a link between the
        motorArm = hardwareMap.dcMotor.get("motorArm");               // code and the hardware for the motors. The
        motorLift = hardwareMap.dcMotor.get("motorLift");             // names in quotations are the names of the
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");   // motors we set on the phone.
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        redLED = hardwareMap.dcMotor.get("redLED");
        blueLED = hardwareMap.dcMotor.get("blueLED");
//          \\~~~~~//

        // Motor Init \\
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);  // These lines reverse the necessary motors in order
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE); // to make the robot drive straight.
//          \\~~~~~//

        // Servos \\
        servoClimberLeft = hardwareMap.servo.get("servoClimberLeft");   // These lines establish a link between the
        servoClimberRight = hardwareMap.servo.get("servoClimberRight"); // code and the hardware for the servos. The
        servoHook = hardwareMap.servo.get("servoHook");                 // names in quotations are the names of the
        servoClimberFront = hardwareMap.servo.get("servoClimberFront"); // servos we set on the phone.
        servoAllClear = hardwareMap.servo.get("servoAllClear");
//          \\~~~~~//

        // Servo Init \\
        servoClimberLeft.setPosition(SERVO_CLIMBER_LEFT_POSITION);   // These lines initialize the servos to their default
        servoClimberRight.setPosition(SERVO_CLIMBER_RIGHT_POSITION); // positions using the doubles declared above.
        servoHook.setPosition(SERVO_HOOK_POSITION);
        servoClimberFront.setPosition(SERVO_CLIMBER_FRONT_POSITION);
//          \\~~~~~//

        // Sensors \\
        sensorGyro = hardwareMap.gyroSensor.get("gyro");    // These lines establish a link between the code and the hardware
        sensorTouch = hardwareMap.touchSensor.get("touch"); // for the sensors. The names in quotations are the names of the
        // sensors we set on the phone.

        sensorGyro.calibrate(); // This line calibrates the gyro sensor.
//           \\~~~~~//
    }

    @Override
    public void init() { // Using this section of the code is another way to initialize the robot.

    }

    @Override
    public void loop() { // This section of the code is the main teleop program.

        float left = -gamepad1.left_stick_y;   // These lines establish the joystick input values as the float
        float right = -gamepad1.right_stick_y; // variables of "left", "right", "arm", and "lift".
        float arm = gamepad2.right_stick_y;
        float lift = gamepad2.left_stick_y;

        right = Range.clip(right, -1, 1);      // These lines clip the extreme ends of the joystick input
        left = Range.clip(left, -1, 1);        // values in the resulting floats to avoid exceeding values
        arm = Range.clip(arm, -1, 1);          // accepted by the program.
        lift = Range.clip(lift, -1, 1);

        right = (float) scaleInput(right);    // These lines scale the joystick input values in the resulting
        left = (float) scaleInput(left);      // floats to the values in the array in the double below, which
        arm = (float) scaleInput(arm);        // are the only ones the program accepts.
        lift = (float) scaleInput(lift);

        setDriveTrainPower(left, right); // This line is an implementation of the public void "setDriveTrainPower" below.
        // It sets the power of the motors to the joystick input values in the floats.



        servoClimberLeft.setPosition(Range.clip(Math.abs(gamepad1.left_trigger - 1), 0, 1)); // This line sets the position of
        // servoClimberLeft to the value
        // of the left trigger on the first
        // controller. The clipping of the
        // value makes sure the servo does
        // not extend too far.

        servoClimberRight.setPosition(gamepad1.right_trigger); // This line sets the position of servoClimberRight to the value
        // of the right trigger on the first controller.

        if (gamepad2.a) servoHook.setPosition(SERVO_HOOK_POSITION); // This line sets the position of servoHook to its default
        // position whenever 'a' is pressed on the second controller.

        if (gamepad2.b) servoHook.setPosition(1.0); // This line sets the position of servoHook to its 'up' position whenever 'b'
        // is pressed on the second controller.

        if (gamepad1.a) servoAllClear.setPosition(0); // This line sets the position of servoAllClear to its 'down' position
            // whenever 'a' is pressed on the first controller.

        else if (gamepad1.y) servoAllClear.setPosition(1); // This line sets the position of servoAllClear to its 'up' position
            // whenever 'y' is pressed on the first controller.

        else servoAllClear.setPosition(0.5); // "If neither button is pressed, servoAllClear is positioned in between the other
        // two positions."

        if (gamepad2.y) servoClimberFront.setPosition(SERVO_CLIMBER_FRONT_POSITION); // This line sets the position of
        // servoClimberFront to its default
        // position whenever 'y' is pressed on the
        // second controller.

        if (gamepad2.x) servoClimberFront.setPosition(0.0); // This line sets the position of servoClimberFront to its 'up'
        // position whenever 'x' is pressed on the second controller.



        if (gamepad1.b) {isRed = true; lightToggle = true;} // "If 'b' is pressed on the first controller, then isRed is true
        // and lightToggle is true. This means that the color of our
        // alliance is red."

        if (gamepad1.x) {isRed = false; lightToggle = true;} // "If 'x' is pressed on the first controller, then isRed is
        // false and lightToggle is true. This means that the color
        // of our alliance is blue."

        if (gamepad1.back) { // "If 'back' is pressed on the first controller, lightToggle is false. This means that the color
            // of the LEDs can now be switched back and forth with the triggers on the second controller."

            lightToggle = false;
        }

        blueLED.setPower(gamepad2.left_trigger); // These lines set the power of the LEDs to the trigger values
        redLED.setPower(gamepad2.right_trigger); // on the second controller.

        if (x >= 1) {  // "If the value of the variable x reaches or goes beyond the value of 1, goingDown is true. This means that
            // the LEDs should now be turning off slowly (their strength should be 'goingDown')."

            goingDown = true;
        }
        if (x <= 0) { // "If the value of the variable x reaches or goes beyond the value of 0, goingDown is false. This means that
            // the LEDs should now be turning on slowly (their strength should not be 'goingDown')."

            goingDown = false;
        }

        if (!goingDown) x = x + 0.01; // "If the LEDs should now be turning on slowly (their strength should not be 'goingDown'),
            // the variable x equals its current value plus 0.01, until it reaches the value of 1. This
            // allows the LEDs to turn on slowly...

        else x = x - 0.01; // ...else if the LEDs should now be turning off slowly (their strength should be 'goingDown'), the
        // variable x equals its current value minus 0.01, until it reaches the value of 0. This allows the
        // LEDs to turn off slowly."

        if (gamepad2.left_trigger <= 0 && gamepad2.right_trigger <= 0 && lightToggle) { // "If both triggers are pressed on the
            // second controller and lightToggle is true...

            if (isRed) redLED.setPower(Range.clip(x, 0.05, 1)); // ...then if isRed is true, the power of the red LED is set to the
                // variable x and is clipped to between the values of 0.05 and 1...

            else blueLED.setPower(Range.clip(x, 0.05, 1)); // ... else if isRed is false, the power of the blue LED is set to the
            // variable x and is clipped to between the values of 0.05 and 1. This all
            // means that the LEDs are returned to their normal state of turning
            // on and off slowly."
        }



        if (sensorTouch.isPressed()) { // "If the touch sensor is pressed (the arm is all of the way down)...

            if (!armReset) { // ...if the arm has not already been reset...

                motorArm.setPower(0); // ...the power of motorArm is set to 0 and armReset is true (the arm
                armReset = true;      // can now only go back up)...
            }
            else { // ...else if the arm has been reset...

                if (Math.abs(gamepad2.right_stick_y) > 0.07) { // ...if the absolute value of the right joystick
                    // on the second controller is above a certain
                    // threshold (0.07)...

                    double armFactor = 0.3; // ...armFactor is equal to 0.3, and the arm will move more slowly...

                    if (gamepad2.right_bumper) armFactor = 1; // ...if the right bumper on the second
                    // controller is pressed, then armFactor is
                    // equal to 1 instead, and the arm will move
                    // more quickly...

                    motorArm.setPower(Range.clip(-arm, 0, 1) * armFactor); // ...and the power of motorArm
                    // is set to the joystick input values
                    // and clipped so that it cannot go
                    // below zero (or farther into the
                    // chassis, burning out the motor in
                    // the process). It is also multiplied
                    // by the chosen armFactor...
                }
                else { // ...else if the right joystick on the second controller is not being utilized...

                    motorArm.setPower(0); // ...the power of motorArm is set to zero...
                }
            }
        }
        else { // ...Else if the touch sensor is not being pressed (the arm is up)...

            if (armReset) armReset = false; // ...if armReset has accidentally been activated, in which case
            // the arm can only go up, it is immediately deactivated...

            if (Math.abs(gamepad2.right_stick_y) > 0.07) { // ...if the absolute value of the right joystick
                // on the second controller is above a certain
                // threshold (0.07)...

                double armFactor = 0.3 ; // ...armFactor is equal to 0.3, and the arm will move at a medium
                // speed...

                if (gamepad2.left_bumper) armFactor = 0.1; // ...if the left bumper on the second controller
                // is pressed, then armFactor is equal to 0.1
                // instead, and the arm will move at a slow speed...

                if (gamepad2.left_trigger > 0.1) armFactor = 0.05; // ...if the left trigger on the second controller
                // is above a certain threshold (0.1), then
                // armFactor is equal to 0.05 instead, and the arm
                // will move at a very slow speed...

                if (gamepad2.right_bumper) armFactor = 1; // ...if the right bumper on the second controller is
                // pressed, then armFactor is equal to 1 and the arm will
                // move at a fast speed...

                motorArm.setPower(-arm * armFactor); // ...and the power of motorArm is set to the joystick
                // input values. It is also multiplied by the chosen
                // armFactor...
            }
            else { // ...else if the right joystick on the second controller is not being utilized...

                motorArm.setPower(0); // ...the power of motorArm is set to zero."
            }
        }



        if (Math.abs(gamepad2.left_stick_y) > 0.07) { // "If the left joystick on the second controller is
            // above a certain threshold (0.07)...

            double liftFactor = .3; // ...liftFactor is equal to 0.3, and the lift will move more slowly...

            if (gamepad2.right_bumper) liftFactor = 1; // ...if the right bumper on the second controller
            // is pressed, liftFactor is equal to 1 instead and
            // the lift will move more quickly...

            motorLift.setPower(lift * liftFactor); // ...and the power of motorLift is set to the joystick
            // input values. It is also multiplied by the chosen
            // liftFactor...
        }
        else { // ...else if the left joystick on the second controller is not being utilized...

            motorLift.setPower(0); // ...the power of motorLift is set to zero."
        }



        if (gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right) { // "If any
            // of the dpad buttons are pressed on the
            // first controller...

            if (!nudging) { // ...if nudging is false (the robot is still being nudged)...

                if (gamepad1.dpad_up) setDriveTrainPower(1, 1); // ...if 'up' was pressed, the robot moves forward...

                else if (gamepad1.dpad_down) setDriveTrainPower(-1, -1); // ...else if 'down' was pressed, the robot
                    // moves backward...

                else if (gamepad1.dpad_left) setDriveTrainPower(-1, 1); // ...else if 'left' was pressed, the robot
                    // turns left...

                else if (gamepad1.dpad_right) setDriveTrainPower(1, -1); // ...else if 'right' was pressed, the robot
                // turns right...
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

        if (gamepad2.dpad_up || gamepad2.dpad_down || gamepad2.dpad_left || gamepad2.dpad_right) { // "If any of
            // the dpad buttons are pressed on the second
            // controller...

            if (!armNudging) { // ...if armNudging is false (the robot is still being nudged)...

                if (gamepad2.dpad_up) motorArm.setPower(1); // ...if 'up' was pressed, the arm moves up...

                else if (gamepad2.dpad_down) motorArm.setPower(-1); // ...else if 'down' was pressed, the
                    // arm moves down...

                else if (gamepad2.dpad_left) motorLift.setPower(1); // ...else if 'left' was pressed, the
                    // lift moves up...

                else if (gamepad2.dpad_right) motorLift.setPower(-1); // ...else if 'right' was pressed,
                // the lift moves down...
            }

            armNudgeCount++; // ...after this one loop cycle, the number of the armNudgeCount goes up by one...

            if (armNudgeCount > 5) { // ...if the number of the armNudgeCount goes above 5...

                armNudging = true; // ...armNudging is true, so the robot cannot nudge anymore. This allows for
                // 5 loop cycles of movement...
            }
        }
        else { // ...else if armNudging is true (the robot is no longer being nudged)...

            armNudging = false; // ...armNudging is returned to false, which allows nudging again if any of the
            // dpad buttons are pressed on the second controller...

            armNudgeCount = 0; // ...and armNudgeCount is returned to 0."
        }



        if (gamepad1.start || gamepad2.start) { // "If 'start' is pressed on either of the controllers...

            kill(); // ...the public void 'kill' is implemented."
        }
    }

    public void setDriveTrainPower (float left, float right) { // This public void, when implemented above,
        // sets the power of the motors. Whatever
        // is inputted into each parameter above is
        // then substituted into its corresponding spot
        // in the public void.

        motorBackRight.setPower(right);
        motorBackLeft.setPower(left);
        motorFrontRight.setPower(right * 0.75); // Multiplying the joystick input values in these two lines by
        motorFrontLeft.setPower(left * 0.75);   // 0.75 accounts for the different gear ratios.
    }

    public void kill() { // This public void, when implemented above, acts as an emergency stop for the robot.
        // It sets the power of all of the motors to 0 and returns all of the servos to their
        // default positions.

        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorArm.setPower(0);
        motorLift.setPower(0);
        servoClimberLeft.setPosition(SERVO_CLIMBER_LEFT_POSITION);
        servoClimberRight.setPosition(SERVO_CLIMBER_RIGHT_POSITION);
        servoHook.setPosition(SERVO_HOOK_POSITION);
        servoClimberFront.setPosition(SERVO_CLIMBER_FRONT_POSITION);
        redLED.setPower(0);
        blueLED.setPower(0);
    }

    double scaleInput(double dVal)  { // When implemented above, this double scales the joystick input values
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