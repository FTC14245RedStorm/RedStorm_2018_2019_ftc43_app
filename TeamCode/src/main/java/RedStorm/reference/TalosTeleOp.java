package RedStorm.reference; // These lines import necessary software for this op mode.

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Katelin Zichittella on 11/1/2016.
 */

@TeleOp // This line establishes this op mode as a teleop op mode and allows for it to be displayed
// in the drop down list on the Driver Station phone to be chosen to run.
@Disabled

public class TalosTeleOp extends OpMode { // This line establishes the name of the op mode and
    // extends the header file "OpMode" in order to create a teleop op mode.

    DcMotor motorBackLeft, motorBackRight, motorFrontLeft, motorFrontRight, // These lines establish
            motorShooter, motorSweeper, motorLift1, motorLift2;             // the names of the
    Servo servoBeacon, servoGate, servoLiftLeft, servoLiftRight;            // motors, servos, and
    GyroSensor sensorGyro;                                                  // sensors we will be
                                                                            // using.

    double motorFactor = 1.0;             // These lines establish and set the values of the
    double sweeperMode;                   // variables we will be using.
    boolean gamepad2_a_currState = false;
    boolean gamepad2_a_prevState = false;
    boolean gamepad2_b_currState = false;
    boolean gamepad2_b_prevState = false;

    byte[] rangeSensorLeftCache;  // A "byte", "I2cDevice", and "I2cDeviceSynch" line is necessary
    byte[] rangeSensorRightCache; // here for each sensor with which more than one of each type is
                                  // used. They should follow this pattern of naming.
    I2cDevice rangeSensorLeft;
    I2cDevice rangeSensorRight;
    I2cDeviceSynch rangeSensorLeftReader;
    I2cDeviceSynch rangeSensorRightReader;

    byte[] colorSensorLeftCache;
    byte[] colorSensorRightCache;
    byte[] colorSensorFrontCache;

    I2cDevice colorSensorLeft;
    I2cDevice colorSensorRight;
    I2cDevice colorSensorFront;
    I2cDeviceSynch colorSensorLeftReader;
    I2cDeviceSynch colorSensorRightReader;
    I2cDeviceSynch colorSensorFrontReader;

    @Override
    public void start() { // This section of the code is the initialization routine the robot
        // undergoes when Start is pressed on the Driver Station.

        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");         // These lines establish
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");       // a link between the code
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");       // and the hardware for
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");     // the motors, sensors,
        motorShooter = hardwareMap.dcMotor.get("motorShooter");           // and servos. The names
        motorSweeper = hardwareMap.dcMotor.get("motorSweeper");           // in quotations are the
        motorLift1 = hardwareMap.dcMotor.get("motorLift1");               // names we set on the
        motorLift2 = hardwareMap.dcMotor.get("motorLift2");               // phone.
        rangeSensorLeft = hardwareMap.i2cDevice.get("rangeSensorLeft");
        rangeSensorRight = hardwareMap.i2cDevice.get("rangeSensorRight");
        colorSensorLeft = hardwareMap.i2cDevice.get("colorSensorLeft");
        colorSensorRight = hardwareMap.i2cDevice.get("colorSensorRight");
        colorSensorFront = hardwareMap.i2cDevice.get("colorSensorFront");
        sensorGyro = hardwareMap.gyroSensor.get("sensorGyro");
        servoBeacon = hardwareMap.servo.get("servoBeacon");
        servoGate = hardwareMap.servo.get("servoGate");
        servoLiftLeft = hardwareMap.servo.get("servoLiftLeft");
        servoLiftRight = hardwareMap.servo.get("servoLiftRight");

        colorSensorLeftReader = new I2cDeviceSynchImpl(colorSensorLeft, I2cAddr.create8bit(0x3c), false);
        colorSensorRightReader = new I2cDeviceSynchImpl(colorSensorRight, I2cAddr.create8bit(0x3e), false);
        colorSensorFrontReader = new I2cDeviceSynchImpl(colorSensorFront, I2cAddr.create8bit(0x42), false);
        rangeSensorLeftReader = new I2cDeviceSynchImpl(rangeSensorLeft, I2cAddr.create8bit(0x28), false);
        rangeSensorRightReader = new I2cDeviceSynchImpl(rangeSensorRight, I2cAddr.create8bit(0x30), false);
        // These lines establish another link between the code and the hardware for just the sensors
        // with which we are using more than one of the same type. For each sensor they confirm
        // its I2c address that we set manually earlier using the Core Device Discovery program.

        colorSensorLeftReader.write8(3, 0);  // These lines initialize the LEDs on the color sensors
        colorSensorRightReader.write8(3, 0); // to be either on or off. A 0 sets the LED to be on,
        colorSensorFrontReader.write8(3, 1); // while a 1 sets the LED to be off.

        colorSensorLeftReader.engage();  // These lines get the sensors with which we are using more
        colorSensorRightReader.engage(); // than one of the same type ready to be used.
        colorSensorFrontReader.engage();
        rangeSensorLeftReader.engage();
        rangeSensorRightReader.engage();

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);   // These lines reverse the
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);  // necessary motors and set all of
        motorSweeper.setDirection(DcMotor.Direction.REVERSE);    // the servos to their desired
        servoBeacon.setPosition(0.0);                            // starting positions.
        servoGate.setPosition(0.0);
        servoLiftLeft.setPosition(0.70);
        servoLiftRight.setPosition(0.67);

        motorFactor = 1.0; // These lines set two of the variables to their initial values.
        sweeperMode = 0.0;
    }

    @Override
    public void init() { // Using this section of the code is another way to initialize the robot
                         // during the actual initialization period.

    }

    @Override
    public void loop() { // This section of the code is the main teleop program.

        float left = gamepad1.left_stick_y;   // These lines establish the joystick input values as
        float right = gamepad1.right_stick_y; // the float variables of "left", "right", "shoot",
        float shoot = gamepad2.right_stick_y; // and "lift".
        float lift = gamepad2.left_stick_y;

        left = Range.clip(left, -1, 1);   // These lines clip the extreme ends of the joystick input
        right = Range.clip(right, -1, 1); // values in the resulting floats to avoid exceeding
        shoot = Range.clip(shoot, -1, 1); // values accepted by the program.
        lift = Range.clip(lift, -1, 1);

        left = (float) scaleInput(left);   // These lines scale the joystick input values in the
        right = (float) scaleInput(right); // resulting floats to the values in the array in the
        shoot = (float) scaleInput(shoot); // double below, which are the only ones the program
        lift = (float) scaleInput(lift);   // accepts.

        setMotorPower(left * motorFactor, right * motorFactor); // This line is an implementation of
        // the public void "setMotorPower" below. It sets the power of the drive train motors to the
        // joystick input values in the floats multiplied by motorFactor, which can be changed by
        // toggling the x, y, a, and b buttons on the first gamepad in order to increase or decrease
        // overall speed, or reverse the drive train motors at two possible speeds.
        setAttachmentPower(sweeperMode, shoot, lift); // This line is an implementation of the
        // public void "setAttachmentPower" below. It sets the power of the attachment motors to
        // either sweeperMode, which can be changed by pressing the left and right bumpers on the
        // second gamepad, or the joystick input values in the floats.

        gamepad2_a_currState = gamepad2.a; // These lines set the current state variables to whether
        gamepad2_b_currState = gamepad2.b; // or not the a or b button is being pressed on the
                                           // second controller. The variables become true if the
                                           // buttons are being pressed and false if they are not.

        if (gamepad1.x) { // "If the x button is pressed on the first controller...

            motorFactor = 0.5; // ...motorFactor becomes equal to 0.5."
        }

        if (gamepad1.y) { // "If the y button is pressed on the first controller...

            motorFactor = 1.0; // ...motorFactor becomes equal to 1.0."
        }

        if (gamepad1.a) { // "If the a button is pressed on the first controller...

            motorFactor = -0.5; // ...motorFactor becomes equal to -0.5."
        }

        if (gamepad1.b) { // "If the b button is pressed on the first controller...

            motorFactor = -1.0; // ...motorFactor becomes equal to -1.0."
        }

        if (gamepad1.left_bumper) { // "If the left bumper is pressed on the first controller...

            servoBeacon.setPosition(1); // ...set the position of the beacon servo to 1 (left)."
        }

        if (gamepad1.right_bumper) { // "If the right bumper is pressed on the first controller...

            servoBeacon.setPosition(0); // ...set the position of the beacon servo to 0 (right)."
        }

        if (gamepad2_a_currState && gamepad2_a_currState != gamepad2_a_prevState) {
            // "If the a button on the second controller is pressed and it was not being pressed
            // before (the && denotes the word "and" and the != denotes the words "not equal to")...

            servoLiftLeft.setPosition(0.70);  // ...set the lift servos to their closed positions...
            servoLiftRight.setPosition(0.67);

            gamepad2_a_prevState = gamepad2_a_currState; // ...make the previous state of the a
                                                         // button of the second controller equal to
                                                         // the current state (pressed)."
        }
        else if (gamepad2_a_currState == false && gamepad2_a_currState != gamepad2_a_prevState) {
            // "Else if the a button on the second controller is not pressed and it was being
            // pressed before...

            gamepad2_a_prevState = gamepad2_a_currState; // ...make the previous state of the a
                                                         // button of the second controller equal to
                                                         // the current state (not pressed)."
        }

        if (gamepad2_b_currState && gamepad2_b_currState != gamepad2_b_prevState) {
            // "If the b button on the second controller is pressed and it was not being pressed
            // before...

            servoLiftLeft.setPosition(0.35);  // ...set the lift servos to their open positions...
            servoLiftRight.setPosition(0.32);

            gamepad2_b_prevState = gamepad2_b_currState; // ...make the previous state of the b
                                                         // button of the second controller equal to
                                                         // the current state (pressed)."
        }
        else if (gamepad2_b_currState == false && gamepad2_b_currState != gamepad2_b_prevState) {
            // "Else if the b button on the second controller is not pressed and it was being
            // pressed before...

            gamepad2_b_prevState = gamepad2_b_currState; // ...make the previous state of the b
                                                         // button of the second controller equal to
                                                         // the current state (not pressed)."
        }

        if (gamepad2.dpad_left) { // "If the left button on the dpad of the second controller is
                                  // pressed...

            servoLiftLeft.setPosition(0.29);  // ...set the lift servos to their "dab left"
            servoLiftRight.setPosition(0.67); // positions."
        }

        if (gamepad2.dpad_right) { // "If the right button on the dpad of the second controller is
                                   // pressed...

            servoLiftLeft.setPosition(0.70);  // ...set the lift servos to their "dab right"
            servoLiftRight.setPosition(0.26); // positions."
        }

        if (gamepad2.right_bumper) { // "If the right bumper is pressed on the second controller...

            sweeperMode = 1.0; // ...sweeperMode becomes equal to 1.0, which allows for the sweeper
                               // to sweep in the forward direction."
        }
        else if (gamepad2.left_bumper) { // "Else if the left bumper is pressed on the second
                                         // controller...

            sweeperMode = -1.0; // ...sweeperMode becomes equal to -1.0, which allows for the
            // sweeper to sweep in the reverse direction."
        }
        else { // "Else if neither bumper is being pressed on the second controller...

            sweeperMode = 0.0; // ...sweeperMode becomes equal to 0.0, which allows for the sweeper
            // to stop."
        }

        if (gamepad2.dpad_down) { // "If the down button on the dpad of the second controller is
                                  // pressed...

            servoGate.setPosition(0.3); // ...set the position of the gate servo to 0.3 (down).
        }
        else { // "Else if the down button on the dpad of the second controller is not being
            // pressed...

            servoGate.setPosition(0.0); // ...set the position of the gate servo to 0.0 (up).
        }
    }

    @Override
    public void stop() { // Anything put into this section of the code is what the robot does when
                         // Stop is pressed on the Driver Station.

    }

    public void setMotorPower (double left, double right) { // This public void, when implemented
                                                            // above, sets the power of the drive
                                                            // train motors. Whatever is inputted
                                                            // into each parameter above is then
                                                            // substituted into its corresponding
                                                            // spot in the public void.

        motorBackLeft.setPower(left);    // These lines set the power of the motors to the
        motorBackRight.setPower(right);  // desired left and right powers.
        motorFrontLeft.setPower(left);
        motorFrontRight.setPower(right);
    }

    public void setAttachmentPower (double sweeperMode, double shoot, double lift) {
        // This public void, when implemented above, sets the power of the attachment motors.
        // Whatever is inputted into each parameter above is then substituted into its corresponding
        // spot in the public void.

        motorSweeper.setPower(sweeperMode); // These lines set the power of the motors to the
        motorShooter.setPower(shoot);       // desired powers.
        motorLift1.setPower(lift);
        motorLift2.setPower(lift);
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

        // Now remember a negative value might have been passed to this method
        // So return the favor and make the scaled output negative if the method
        // originally passed a negative value
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}