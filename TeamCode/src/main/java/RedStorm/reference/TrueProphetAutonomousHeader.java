package RedStorm.reference; // These lines import necessary software for this program.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Robotics Club on 10/14/2015.
 */



// IMPORTANT NOTE: IN ALL OF THE AUTONOMOUS OP MODES, THE FRONT OF THE ROBOT IS REALLY THE BACK. THAT IS WHY WHEN THE
// POWER IS A NEGATIVE VALUE THE ROBOT IS MOVING "FORWARD".
// ALSO: THE LINE "anyMotorNameHere.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);" IS NOW
// "anyMotorNameHere.setMode(DcMotor.RunMode.RUN_USING_ENCODER);".



public abstract class TrueProphetAutonomousHeader extends LinearOpMode { // This line establishes this program as a public abstract class
    // that extends the header file "LinearOpMode". This makes it
    // a header file itself that the real FacingCrater op modes will
    // extend.

    DcMotor motorArm, motorLift, motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight, redLED, blueLED; // These
    Servo servoClimberLeft, servoClimberRight, servoHook, servoClimberFront, servoAllClear; // lines establish the names of
    GyroSensor sensorGyro;                                                                  // the motors, servos, and
    // sensors we will be using.

    int teamColor = 0;                         // This line establishes the initial value of the integer teamColor as 0.

    double SERVO_CLIMBER_LEFT_POSITION = 1.0;  // These lines establish the names and values of doubles that will serve
    double SERVO_CLIMBER_RIGHT_POSITION = 0.0; // as the initialization and default positions of the servos.
    double SERVO_HOOK_POSITION = 0.0;
    double SERVO_CLIMBER_FRONT_POSITION = 1.0;
    double SERVO_ALL_CLEAR_POSITION = 1.0;



    public void initEverything() { // This public void will go at the start of each FacingCrater op mode and will serve as
        // the initialization routine the robot undergoes.

        // Motors \\                                                  // These lines establish a link between the code
        motorArm = hardwareMap.dcMotor.get("motorArm");               // and the hardware for the motors. The names in
        motorLift = hardwareMap.dcMotor.get("motorLift");             // quotations are the names of the motors we set
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");   // on the phone.
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        redLED = hardwareMap.dcMotor.get("redLED");
        blueLED = hardwareMap.dcMotor.get("blueLED");
//          \\~~~~~//

        // Motor Init \\
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);  // These lines reverse the necessary motors in order
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE); // to make the robot drive straight.

        if (teamColor == 1) {      // This if statement sets the color of the LEDs depending on which value teamColor
            // is declared as in each FacingCrater op mode.
            blueLED.setPower(1);
        }
        else if (teamColor == 2) {

            redLED.setPower(1);
        }
        else {

            blueLED.setPower(.5);
            redLED.setPower(.5);
        }
//          \\~~~~~//

        // Servos \\
        servoClimberLeft = hardwareMap.servo.get("servoClimberLeft");   // These lines establish a link between the
        servoClimberRight = hardwareMap.servo.get("servoClimberRight"); // code and the hardware for the servos. The
        servoHook = hardwareMap.servo.get("servoHook");                 // names in quotations are the names of the
        servoClimberFront = hardwareMap.servo.get("servoClimberFront"); // servos we set on the phone.
        servoAllClear = hardwareMap.servo.get("servoAllClear");
//          \\~~~~~//

        // Servo Init \\
        servoClimberLeft.setPosition(SERVO_CLIMBER_LEFT_POSITION);   // These lines initialize the servos to their
        servoClimberRight.setPosition(SERVO_CLIMBER_RIGHT_POSITION); // default positions using the doubles declared
        servoHook.setPosition(SERVO_HOOK_POSITION);                  // above.
        servoClimberFront.setPosition(SERVO_CLIMBER_FRONT_POSITION);
        servoAllClear.setPosition(SERVO_ALL_CLEAR_POSITION);
//          \\~~~~~//

        // Sensor Init \\
        sensorGyro = hardwareMap.gyroSensor.get("gyro"); // This line establishes a link between the code and the
        // hardware for the gyro sensor. The name in quotations
        // is the name of the gyro sensor we set on the phone.

        sensorGyro.calibrate(); // This line calibrates the gyro sensor.
//           \\~~~~~//

        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // These lines set the motors to the mode
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // RUN_USING_ENCODER in order to be run
        motorArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);       // utilizing their encoders.
        motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hardwareMap.logDevices(); // I am not really sure what this line is for, but I guess it is necessary.
    }



    public void moveWheels (int distance, double power) { // This public void is called in the FacingCrater op modes
        // whenever the robot has to move forward or backward using
        // encoders. It has two parameters: distance in centimeters
        // the robot should travel and the power the motors should
        // run at. These can be changed with each implementation of
        // the public void. Whatever is inputted into each parameter
        // is then substituted into its corresponding spots in the
        // public void.

        if (power < 0.0) { // "If the power inputted is below 0 (the robot should move forward)...

            distance = (distance * 60) + Math.abs(motorBackLeft.getCurrentPosition()); // ...the distance inputted in
            // centimeters is multiplied by
            // 60 to convert it to encoder
            // counts and added to the
            // absolute value of the current
            // position of motorBackLeft
            // (measured in encoder counts).
            // This calculates the position
            // the robot has to reach next...

            sensorGyro.resetZAxisIntegrator(); // ...the heading of the gyro sensor (its orientation measured in degrees)
            // is reset to 0...

            motorBackLeft.setTargetPosition(distance); // ...the target position of motorBackLeft (and therefore all motors,
            // by the way) is set to the new distance value...

            setDriveTrainPower(power, power); // ...the power of the motors is set to the inputted value...

            boolean ignore = false; // ...a boolean called ignore is established, and its initial value is false...

            while (Math.abs(motorBackLeft.getCurrentPosition()) < Math.abs(distance)) { // ...while the absolute value of
                // the current position of
                // motorBackLeft is less than the
                // absolute value of distance (or
                // while the robot is still moving
                // forward, increasing its encoder
                // count as it approaches the target
                // distance)...

                int heading = sensorGyro.getHeading(); // ...the current heading (degree measure) of the gyro sensor is
                // established as the integer heading...

                if (heading >= 180) { // ...if heading is equal to or above 180 degrees...

                    heading = 360 - heading; // ...heading is now equal to 360 degrees minus the original heading. This ensures
                    // the heading measure does not exceed 180 degrees. For example, a measure of 270
                    // degrees would become a measure of 90 degrees in the opposite direction...
                }

                if (Math.abs(sensorGyro.rawX()) > 1000 || Math.abs(sensorGyro.rawY()) > 1000) {ignore = true;} // ...if the gyro
                // sensor reads that the robot has
                // tipped up over a block, ignore is
                // true...

                if (Math.abs(heading) > 5 && !ignore) { // ...if the absolute value of heading is greater than 5 degrees (if the
                    // robot has veered of to either direction) and ignore is false (this means
                    // that if the robot has tipped up over a block, this will not happen)...

                    if (heading > 0) { // ...if heading is greater than 0 degrees (the robot has veered to the right)...

                        setDriveTrainPower(power, -power); // ...the robot will turn back to the left until the gyro sensor reads
                        // that its heading has returned to normal...
                    }
                    if (heading < 0) { // ...else if the heading is less than 0 degrees (the robot has veered to the left)...

                        setDriveTrainPower(-power, power); // ...the robot will turn back to the right until the gyro sensor reads
                        // that its heading has returned to normal...
                    }
                }
                else { // ...else if the robot is driving straight or ignore is true (or both)...

                    setDriveTrainPower(power, power); // ...the robot will move forward as normal...
                }
            }

            setDriveTrainPower(0, 0); // ...once the program has exited the while loop (the robot has reached its target position)
            // the power of the motors is set to 0...
        }
        else if (power >= 0.0) { // ...Else if the power inputted is equal to or above 0 (the robot should stop or move backward)...

            distance = Math.abs(motorBackLeft.getCurrentPosition()) - (distance * 60); // ...the distance inputted in
            // centimeters is multiplied by
            // 60 to convert it to encoder
            // counts and subtracted from the
            // absolute value of the current
            // position of motorBackLeft
            // (measured in encoder counts).
            // This calculates the position
            // the robot has to reach next...

            sensorGyro.resetZAxisIntegrator(); // ...the heading of the gyro sensor (its orientation measured in degrees)
            // is reset to 0...

            motorBackLeft.setTargetPosition(distance); // ...the target position of motorBackLeft is set to the new distance
            // value...

            setDriveTrainPower(power, power); // ...the power of the motors is set to the inputted value...

            boolean ignore = false; // ...a boolean called ignore is established, and its initial value is false...

            while (Math.abs(motorBackLeft.getCurrentPosition()) > Math.abs(distance)) { // ...while the absolute value of
                // the current position of
                // motorBackLeft is greater than the
                // absolute value of distance (or
                // while the robot is still moving
                // backward, decreasing its encoder
                // count as it approaches the target
                // distance)...

                int heading = sensorGyro.getHeading(); // ...the current heading (degree measure) of the gyro sensor is
                // established as the integer heading...

                if (heading >= 180) { // ...if heading is equal to or above 180 degrees...

                    heading = 360 - heading; // ...heading is now equal to 360 degrees minus the original heading. This ensures
                    // the heading measure does not exceed 180 degrees. For example, a measure of 270
                    // degrees would become a measure of 90 degrees in the opposite direction...
                }

                if (Math.abs(sensorGyro.rawX()) > 1000 || Math.abs(sensorGyro.rawY()) > 1000) {ignore = true;} // ...if the gyro
                // sensor reads that the robot has
                // tipped up over a block, ignore is
                // true...

                if (Math.abs(heading) > 5 && !ignore) { // ...if the absolute value of heading is greater than 5 degrees (if the
                    // robot has veered of to either direction) and ignore is false (this means
                    // that if the robot has tipped up over a block, this will not happen)...

                    if (heading > 0) { // ...if heading is greater than 0 degrees (the robot has veered to the right)...

                        setDriveTrainPower(-power, power); // ...the robot will turn back to the left until the gyro sensor reads
                        // that its heading has returned to normal...
                    }
                    if (heading < 0) { // ...else if the heading is less than 0 degrees (the robot has veered to the left)...

                        setDriveTrainPower(power, -power); // ...the robot will turn back to the right until the gyro sensor reads
                        // that its heading has returned to normal...
                    }
                }
                else { // ...else if the robot is driving straight or ignore is true (or both)...

                    setDriveTrainPower(power, power); // ...the robot will move backward as normal...
                }
            }

            setDriveTrainPower(0, 0); // ...once the program has exited the while loop (the robot has reached its target position)
            // the power of the motors is set to 0."
        }

        if (teamColor == 1) { // This if statement sets the color of the LEDs depending on which value teamColor
            // is declared as in each FacingCrater op mode.

            blueLED.setPower(1);
        }
        else if (teamColor == 2) {

            redLED.setPower(1);
        }
    }



    public void gyroTurnRight (int degrees, double power) { // This public void is called in the FacingCrater op modes
        // whenever the robot has to turn right using the gyro
        // sensor. It has two parameters: the number of degrees
        // the robot should rotate and the power the motors should
        // run at. These can be changed with each implementation of
        // the public void. Whatever is inputted into each parameter
        // is then substituted into its corresponding spots in the
        // public void.

        sensorGyro.resetZAxisIntegrator(); // This line resets the heading of the gyro sensor (its orientation
        // measured in degrees) to 0.

        int heading = sensorGyro.getHeading(); // This line establishes the current heading (degree measure) of the
        // gyro sensor as the integer heading.

        if (heading >= 180) { // "If heading is equal to or above 180 degrees...

            heading = 360 - heading; // ...heading is now equal to 360 degrees minus the original heading. This ensures
            // the heading measure does not exceed 180 degrees. For example, a measure of 270
            // degrees would become a measure of 90 degrees in the opposite direction."
        }

        setDriveTrainPower(power, -power); // This line sets the power of the motors to the inputted value and modifies
        // it so that the robot turns right.

        while (heading < degrees) { // "While heading is less than degrees (or while the robot is still turning right,
            // increasing the degree measure the gyro sensor is reading until it reaches
            // its target value...

            heading = sensorGyro.getHeading(); // ...heading is established as the current heading of the gyro sensor
            // again, but because it is in the while loop, the current heading and
            // therefore this integer is continually refreshed...

            if (heading >= 180) { // ...if heading is equal to or above 180 degrees...

                heading = 360 - heading; // ...heading is now equal to 360 degrees minus the original heading. This ensures
                // the heading measure does not exceed 180 degrees. For example, a measure of 270
                // degrees would become a measure of 90 degrees in the opposite direction...
            }
        }

        setDriveTrainPower(0, 0); // ...once the program has exited the while loop (the rotation of the robot has reached
        // its target value) the power of the motors is set to 0."
    }

    public void gyroTurnLeft (int degrees, double power) { // This public void is called in the FacingCrater op modes
        // whenever the robot has to turn left using the gyro
        // sensor. It has two parameters: the number of degrees
        // the robot should rotate and the power the motors should
        // run at. These can be changed with each implementation of
        // the public void. Whatever is inputted into each parameter
        // is then substituted into its corresponding spots in the
        // public void.

        sensorGyro.resetZAxisIntegrator(); // This line resets the heading of the gyro sensor (its orientation
        // measured in degrees) to 0.

        int heading = sensorGyro.getHeading(); // This line establishes the current heading (degree measure) of the
        // gyro sensor as the integer heading.

        if (heading >= 180) { // "If heading is equal to or above 180 degrees...

            heading = 360 - heading; // ...heading is now equal to 360 degrees minus the original heading. This ensures
            // the heading measure does not exceed 180 degrees. For example, a measure of 270
            // degrees would become a measure of 90 degrees in the opposite direction."
        }

        setDriveTrainPower(-power, power); // This line sets the power of the motors to the inputted value and modifies
        // it so that the robot turns left.

        while (heading < degrees) { // "While heading is less than degrees (or while the robot is still turning left,
            // increasing the degree measure the gyro sensor is reading until it reaches
            // its target value...

            heading = sensorGyro.getHeading(); // ...heading is established as the current heading of the gyro sensor
            // again, but because it is in the while loop, the current heading and
            // therefore this integer is continually refreshed...

            if (heading >= 180) { // ...if heading is equal to or above 180 degrees...

                heading = 360 - heading; // ...heading is now equal to 360 degrees minus the original heading. This ensures
                // the heading measure does not exceed 180 degrees. For example, a measure of 270
                // degrees would become a measure of 90 degrees in the opposite direction...
            }
        }

        setDriveTrainPower(0, 0); // ...once the program has exited the while loop (the rotation of the robot has reached
        // its target value) the power of the motors is set to 0."
    }

    public void setDriveTrainPower (double left, double right) { // This public void, when implemented above,
        // sets the power of the motors. Whatever
        // is inputted into each parameter above is
        // then substituted into its corresponding spot
        // in the public void.

        motorBackRight.setPower(right);
        motorBackLeft.setPower(left);
        motorFrontRight.setPower(right * 0.75); // Multiplying the values in these two lines by 0.75 accounts
        motorFrontLeft.setPower(left * 0.75);   // for the different gear ratios.
    }
}