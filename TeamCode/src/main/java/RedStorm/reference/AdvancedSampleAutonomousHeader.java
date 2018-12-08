package RedStorm.reference; // These lines import necessary software for this program.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Katelin Zichittella on 10/16/2016.
 */

public abstract class AdvancedSampleAutonomousHeader extends LinearOpMode { // This line
    // establishes this program as a public abstract class that extends the header file
    // "LinearOpMode". This makes it a header file in itself that the real FacingCrater op modes
    // will extend. It is considered abstract because it simply contains the framework for the
    // FacingCrater op modes (creating void methods and such) and does not yet translate to actual
    // movement on the robot.

    DcMotor motorLeft, motorRight; // These lines establish the names of the motors and sensor we
    GyroSensor sensorGyro;         // will be using.

    public void initialize() { // This public void will go at the start of each FacingCrater op mode
        // and will serve as the initialization routine the robot undergoes.

        motorLeft = hardwareMap.dcMotor.get("motorLeft");      // These lines establish a link
        motorRight = hardwareMap.dcMotor.get("motorRight");    // between the code and the hardware
                                                               // for the motors and sensor. The
        sensorGyro = hardwareMap.gyroSensor.get("sensorGyro"); // names in quotations are the names
                                                               // of the motors and sensor we set
                                                               // on the phone.

        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // Because we will be using encoders
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // in this program, we need to set
                                                               // the motors to this mode so that
                                                               // they are able to utilize them.

        motorRight.setDirection(DcMotor.Direction.REVERSE); // This line reverses the right motor
                                                            // in order to negate the fact that the
                                                            // motors are placed on the robot to
                                                            // mirror each other.
    }

    public void calibrateGyro () throws InterruptedException { // This public void goes right after
        // "initialize" but before the line
        // "waitForStart", as it is a
        // separate part of the
        // initialization routine. It
        // calibrates the gyro sensor before
        // FacingCrater so it works optimally.

        sensorGyro.calibrate(); // This line calibrates the gyro sensor.

        while (sensorGyro.isCalibrating()) { // "While the gyro sensor is calibrating...

            Thread.sleep(50); // ...wait 50 milliseconds for it to calibrate...

            telemetry.addData("Calibrated", false); // ...the fact that calibration is false
            // (the gyro sensor is not done calibrating yet) is returned to the Driver Station."
            telemetry.update(); // This line is always needed after telemetry in order to update it
            // in real time.
        }

        telemetry.addData("Calibrated", true); // Once the program has exited the while loop (the
        // gyro sensor is done calibrating), this line returns the fact that calibration is true to
        // the Driver Station so that the coach can start the main program.
        telemetry.update(); // This line is always needed after telemetry in order to update it in
        // real time.
    }

    public void encodersForward(int distance, double power) { // This public void is called in the
                                                              // FacingCrater op modes whenever the
                                                              // robot has to move forward using
                                                              // encoders. It has two parameters:
                                                              // distance in inches the robot
                                                              // should travel and the power the
                                                              // motors should run at. These can be
                                                              // changed with each implementation
                                                              // of the public void. Whatever is
                                                              // inputted into each parameter is
                                                              // then substituted into its
                                                              // corresponding spots in the public
                                                              // void.

        int DIAMETER = 4; // The diameter of the specific wheels we are using on this robot
        int GEAR_RATIO = 1; // The gear ratio for the wheels we are using on this robot
        int PULSES = 1120; // The number of encoder counts per full rotation of a Neverest 40 motor
        double CIRCUMFERENCE = Math.PI * DIAMETER; // Calculating the circumference of the wheels
        double ROTATIONS = (distance / CIRCUMFERENCE) * GEAR_RATIO; // Depending on the inputted
                                                                    // desired distance for the
                                                                    // robot to travel in inches,
                                                                    // this line calculates exactly
                                                                    // how many rotations the wheel
                                                                    // should turn in order to go
                                                                    // that distance.
        double COUNTS = PULSES * ROTATIONS; // Calculating the number of encoder counts that when
                                            // reached the robot will stop (based on the number of
                                            // encoder counts per full rotation and the desired
                                            // number of rotations)

        COUNTS = COUNTS + Math.abs(motorLeft.getCurrentPosition()); // This line makes it so that
        // the target encoder count is the desired count plus the current count and the method can
        // be run over and over again without resetting the encoders.

        setMotorPower(power, power); // This line sets the power of the motors to the desired power
        // specified in the parameters.

        while (motorLeft.getCurrentPosition() < COUNTS) { // "While the robot is moving and the
                                                          // encoder count is increasing, but
                                                          // still lower than the desired count...

            setMotorPower(power, power); // ...keep moving."
        }

        setMotorPower(0.0, 0.0); // "Once the desired count is reached, stop the robot."
    }

    public void encodersBackward(int distance, double power) { // This public void is called in the
                                                               // FacingCrater op modes whenever the
                                                               // robot has to move backward using
                                                               // encoders. It has two parameters:
                                                               // distance in inches the robot
                                                               // should travel and the power the
                                                               // motors should run at. These can be
                                                               // changed with each implementation
                                                               // of the public void. Whatever is
                                                               // inputted into each parameter is
                                                               // then substituted into its
                                                               // corresponding spots in the public
                                                               // void.

        int DIAMETER = 4; // The diameter of the specific wheels we are using on this robot
        int GEAR_RATIO = 1; // The gear ratio for the wheels we are using on this robot
        int PULSES = 1120; // The number of encoder counts per full rotation of a Neverest 40 motor
        double CIRCUMFERENCE = Math.PI * DIAMETER; // Calculating the circumference of the wheels
        double ROTATIONS = (distance / CIRCUMFERENCE) * GEAR_RATIO; // Depending on the inputted
                                                                    // desired distance for the
                                                                    // robot to travel in inches,
                                                                    // this line calculates exactly
                                                                    // how many rotations the wheel
                                                                    // should turn in order to go
                                                                    // that distance.
        double COUNTS = PULSES * ROTATIONS; // Calculating the number of encoder counts that when
                                            // reached the robot will stop (based on the number of
                                            // encoder counts per full rotation and the desired
                                            // number of rotations)

        COUNTS = Math.abs(motorLeft.getCurrentPosition()) - COUNTS; // This line makes it so that
        // the target encoder count is the current count minus the desired count and the method can
        // be run over and over again without resetting the encoders.

        setMotorPower(-power, -power); // This line sets the power of the motors to the negative of
        // the desired power specified in the parameters.

        while (motorLeft.getCurrentPosition() > COUNTS) { // "While the robot is moving and the
                                                          // encoder count is decreasing, but
                                                          // still higher than the desired count...

            setMotorPower(-power, -power); // ...keep moving."
        }

        setMotorPower(0.0, 0.0); // "Once the desired count is reached, stop the robot."
    }

    public void gyroTurnRight (int degrees, double power) { // This public void is called in the
                                                            // FacingCrater op modes whenever the
                                                            // robot has to turn right using the
                                                            // gyro sensor. It has two parameters:
                                                            // the number of degrees the robot
                                                            // should turn and the power the motors
                                                            // should run at. These can be changed
                                                            // with each implementation of the
                                                            // public void. Whatever is inputted
                                                            // into each parameter is then
                                                            // substituted into its corresponding
                                                            // spots in the public void.

        sensorGyro.resetZAxisIntegrator(); // This line resets whatever the gyro sensor is
        // currently reading in degrees to zero.

        int heading = sensorGyro.getHeading(); // This line establishes the current heading (degree
        // measure) of the gyro sensor as an integer called heading.

        setMotorPower(power, -power); // This line sets the power of the motors to the desired power
        // specified in the parameters and negates the power of the right side of the robot so that
        // it begins to turn right.

        while (heading < degrees) { // "While the robot is turning and the degree measure is
                                    // increasing, but still lower than the desired amount...

            heading = sensorGyro.getHeading(); // ...continue to update the variable heading with
            // real time heading data (this is SUPER IMPORTANT - putting this line inside the while
            // loop makes this operation continuous)...

            setMotorPower(power, -power); // ...keep turning right...

            if (heading >= 180) { // ...if the degree measure exceeds 180 degrees (which would
                                  // confuse the program)...

                heading = 360 - heading; // ...heading is now equal to 360 degrees minus the
                // original heading. This ensures the heading measure does not exceed 180 degrees.
                // For example, a measure of 270 degrees would become a measure of 90 degrees in
                // the opposite direction."
            }
        }

        setMotorPower(0, 0); // "Once the desired degree measure is reached, stop the robot."
    }

    public void gyroTurnLeft (int degrees, double power) { // This public void is called in the
                                                           // FacingCrater op modes whenever the
                                                           // robot has to turn left using the
                                                           // gyro sensor. It has two parameters:
                                                           // the number of degrees the robot
                                                           // should turn and the power the motors
                                                           // should run at. These can be changed
                                                           // with each implementation of the
                                                           // public void. Whatever is inputted
                                                           // into each parameter is then
                                                           // substituted into its corresponding
                                                           // spots in the public void.

        sensorGyro.resetZAxisIntegrator(); // This line resets whatever the gyro sensor is
        // currently reading in degrees to zero.

        int heading = sensorGyro.getHeading(); // This line establishes the current heading (degree
        // measure) of the gyro sensor as an integer called heading.

        setMotorPower(-power, power); // This line sets the power of the motors to the desired power
        // specified in the parameters and negates the power of the left side of the robot so that
        // it begins to turn left.

        while (heading < degrees) { // "While the robot is turning and the degree measure is
                                    // increasing, but still lower than the desired amount...

            heading = sensorGyro.getHeading(); // ...continue to update the variable heading with
            // real time heading data (this is SUPER IMPORTANT - putting this line inside the while
            // loop makes this operation continuous)...

            setMotorPower(-power, power); // ...keep turning left...

            if (heading >= 180) { // ...if the degree measure exceeds 180 degrees (which would
                                  // confuse the program)...

                heading = 360 - heading; // ...heading is now equal to 360 degrees minus the
                // original heading. This ensures the heading measure does not exceed 180 degrees.
                // For example, a measure of 270 degrees would become a measure of 90 degrees in
                // the opposite direction."
            }
        }

        setMotorPower(0, 0); // "Once the desired degree measure is reached, stop the robot."
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
}