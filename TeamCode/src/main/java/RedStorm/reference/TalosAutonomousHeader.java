package RedStorm.reference; // These lines import necessary software for this program.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Katelin Zichittella on 11/20/2016.
 */

public abstract class TalosAutonomousHeader extends LinearOpMode { // This line establishes this
    // program as a public abstract class that extends the header file "LinearOpMode". This makes
    // it a header file in itself that the real FacingCrater op modes will extend. It is considered
    // abstract because it simply contains the framework for the FacingCrater op modes (creating void
    // methods and such) and does not yet translate to actual movement on the robot.

    DcMotor motorBackLeft, motorBackRight, motorFrontLeft, motorFrontRight, // These lines establish
            motorShooter, motorSweeper, motorLift1, motorLift2;             // the names of the
    Servo servoBeacon, servoGate, servoLiftLeft, servoLiftRight;            // motors, servos, and
    GyroSensor sensorGyro;                                                  // sensors we will be
                                                                            // using.

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

    public void initialize() { // This public void will go at the start of each FacingCrater op mode
        // and will serve as the initialization routine the robot undergoes.

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

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);  // These lines reverse the
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE); // necessary motors and set all of
        motorShooter.setDirection(DcMotor.Direction.REVERSE);    // the servos to their desired
        servoBeacon.setPosition(0.0);                            // starting positions.
        servoGate.setPosition(0.0);
        servoLiftLeft.setPosition(0.70);
        servoLiftRight.setPosition(0.67);
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

    public void lineFollowerTwoSensors (double distance) { // This public void is called in the
                                                           // FacingCrater op modes whenever the robot
                                                           // has to follow a white line and stop a
                                                           // certain distance away from a barrier.
                                                           // It only has one parameter, which is
                                                           // the distance from a barrier that once
                                                           // reached the robot should stop. This
                                                           // can be changed with each
                                                           // implementation of the public void.
                                                           // Whatever is inputted into the
                                                           // parameter is then substituted into its
                                                           // corresponding spot in the public void.

        if (opModeIsActive()) { // "As long as Stop is never pressed on the Driver Station...

            setMotorPower(0.24, 0.24); // ...set all of the motors to a positive speed of 0.24...

            rangeSensorLeftCache = rangeSensorLeftReader.read(0x04, 1);   // ...set up the range
            rangeSensorRightCache = rangeSensorRightReader.read(0x04, 1); // sensors so that they
                                                                          // read ultrasonic
                                                                          // values (this is
                                                                          // indicated by this
                                                                          // particular hexadecimal
                                                                          // number)...

            while (((rangeSensorLeftCache[0] & 0xFF) > distance || (rangeSensorRightCache[0] & 0xFF) > distance) && opModeIsActive()) {
                // ...while the robot is moving forward and the range sensor values are decreasing,
                // but either one is still above the desired distance (the || denotes the word "or")
                // and Stop still has not been pressed on the Driver Station...

                rangeSensorLeftCache = rangeSensorLeftReader.read(0x04, 1);   // ...continue to
                rangeSensorRightCache = rangeSensorRightReader.read(0x04, 1); // update the range
                                                                              // sensor values with
                                                                              // real time data
                                                                              // (this is SUPER
                                                                              // IMPORTANT - putting
                                                                              // this line inside
                                                                              // the while loop
                                                                              // makes this
                                                                              // operation
                                                                              // continuous)...

                colorSensorLeftCache = colorSensorLeftReader.read(0x08, 1);   // ...set up the two
                colorSensorRightCache = colorSensorRightReader.read(0x08, 1); // bottom color
                                                                              // sensors so that
                                                                              // they read white
                                                                              // values (this is
                                                                              // indicated by this
                                                                              // particular
                                                                              // hexadecimal
                                                                              // number)...

                if ((colorSensorLeftCache[0] & 0xFF) < 45 && (colorSensorRightCache[0] & 0xFF) < 45) {
                    // ...if both bottom color sensors are reading black (a number less than 45)
                    // (the && denotes the word "and")...

                    setMotorPower(0.24, 0.24); // ...continue moving straight forward...
                }

                else if ((colorSensorLeftCache[0] & 0xFF) >= 45) { // ...else if the left bottom
                    // color sensor is reading white (a number greater than or equal to 45) (the
                    // robot has veered too far to the right and is beginning to cross the white
                    // line)...

                    setMotorPower(0, 0.2); // ...turn slightly left while this continues to be the
                    // case...
                }

                else if ((colorSensorRightCache[0] & 0xFF) >= 45) { // ...else if the right bottom
                    // color sensor is reading white (a number greater than or equal to 45) (the
                    // robot has veered too far to the left and is beginning to cross the white
                    // line)...

                    setMotorPower(0.2, 0); // ...turn slightly right while this continues to be the
                    // case...
                }

                else { // ...else if any other condition occurs...

                    setMotorPower(0.24, 0.24); // ...continue moving straight forward."
                }
            }

            setMotorPower(0.0, 0.0); // "Once the desired distance away from the barrier is
            // reached, stop the robot."
        }
    }

    public void moveUntilWhiteLineStraight (double power) { // This public void is called in the
                                                            // FacingCrater op modes whenever the
                                                            // robot has to move until one of the
                                                            // bottom color sensors detects it is
                                                            // over a white line. It only has one
                                                            // parameter, which is the power the
                                                            // motors should run at. This can be
                                                            // changed with each implementation of
                                                            // the public void. Whatever is inputted
                                                            // into the parameter is then
                                                            // substituted into its corresponding
                                                            // spot in the public void.

        if (opModeIsActive()) { // "As long as Stop is never pressed on the Driver Station...

            setMotorPower(power, power); // ...set all of the motors to the desired power...

            colorSensorLeftCache = colorSensorLeftReader.read(0x08, 1);   // ...set up the two
            colorSensorRightCache = colorSensorRightReader.read(0x08, 1); // bottom color
                                                                          // sensors so that
                                                                          // they read white
                                                                          // values (this is
                                                                          // indicated by this
                                                                          // particular
                                                                          // hexadecimal
                                                                          // number)...

            while (((colorSensorLeftCache[0] & 0xFF) < 45 && (colorSensorRightCache[0] & 0xFF) < 45) && opModeIsActive()) {
                // ...while the robot is moving, both of the bottom color sensors are still
                // reading black, and Stop still has not been pressed on the Driver Station...

                colorSensorLeftCache = colorSensorLeftReader.read(0x08, 1);   // ...continue to
                colorSensorRightCache = colorSensorRightReader.read(0x08, 1); // update the color
                                                                              // sensor values with
                                                                              // real time data
                                                                              // (this is SUPER
                                                                              // IMPORTANT - putting
                                                                              // this line inside
                                                                              // the while loop
                                                                              // makes this
                                                                              // operation
                                                                              // continuous)."
            }

            setMotorPower(0.0, 0.0); // "Once the white line is reached, stop the robot."
        }
    }

    public void encodersForward (int distance, double power) { // This public void is called in the
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

        if (opModeIsActive()) { // If stop is not pressed on the Driver Station

            motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);   // Because we will be using
            motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // encoders in this method, we
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // need to set the motors to
            motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // this mode so that they are
            // able to utilize them.

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

            COUNTS = COUNTS + Math.abs(motorBackRight.getCurrentPosition()); // This line makes it so that
            // the target encoder count is the desired count plus the current count and the method can
            // be run over and over again without resetting the encoders.

            setMotorPower(power, power); // This line sets the power of the motors to the desired power
            // specified in the parameters.

            while (motorBackRight.getCurrentPosition() < COUNTS && opModeIsActive()) { // "While the
                // robot is moving and the encoder
                // count is increasing, but still lower
                // than the desired count, and Stop
                // still has not been pressed on the
                // Driver Station...

                // ...do nothing else in the meantime."
            }

            setMotorPower(0.0, 0.0); // "Once the desired count is reached, stop the robot."
        }
    }

    public void encodersBackward (int distance, double power) { // This public void is called in the
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

        if (opModeIsActive()) { // If Stop is not pressed on the Driver Station

            motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);   // Because we will be using
            motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // encoders in this method, we
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);  // need to set the motors to
            motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // this mode so that they are
            // able to utilize them.

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

            COUNTS = Math.abs(motorBackRight.getCurrentPosition()) - COUNTS; // This line makes it so that
            // the target encoder count is the current count minus the desired count and the method can
            // be run over and over again without resetting the encoders.

            setMotorPower(-power, -power); // This line sets the power of the motors to the negative of
            // the desired power specified in the parameters.

            while (motorBackRight.getCurrentPosition() > COUNTS && opModeIsActive()) { // "While the
                // robot is moving and the encoder
                // count is decreasing, but still higher
                // than the desired count, and Stop
                // still has not been pressed on the
                // Driver Station...

                // ...do nothing else in the meantime."
            }

            setMotorPower(0.0, 0.0); // "Once the desired count is reached, stop the robot."
        }
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

        if (opModeIsActive()) { // If Stop is not pressed on the Driver Station

            sensorGyro.resetZAxisIntegrator(); // This line resets whatever the gyro sensor is
            // currently reading in degrees to zero.

            int heading = sensorGyro.getHeading(); // This line establishes the current heading (degree
            // measure) of the gyro sensor as an integer called heading.

            setMotorPower(power, -power); // This line sets the power of the motors to the desired power
            // specified in the parameters and negates the power of the right side of the robot so that
            // it begins to turn right.

            while (heading < degrees && opModeIsActive()) { // "While the robot is turning and the
                // degree measure is increasing, but still lower than the desired amount, and Stop
                // still has not been pressed on the Driver Station...

                heading = sensorGyro.getHeading(); // ...continue to update the variable heading with
                // real time heading data (this is SUPER IMPORTANT - putting this line inside the while
                // loop makes this operation continuous)...

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

        if (opModeIsActive()) { // If Stop is not pressed on the Driver Station

            sensorGyro.resetZAxisIntegrator(); // This line resets whatever the gyro sensor is
            // currently reading in degrees to zero.

            int heading = sensorGyro.getHeading(); // This line establishes the current heading (degree
            // measure) of the gyro sensor as an integer called heading.

            setMotorPower(-power, power); // This line sets the power of the motors to the desired power
            // specified in the parameters and negates the power of the left side of the robot so that
            // it begins to turn left.

            while (heading < degrees && opModeIsActive()) { // "While the robot is turning and the
                // degree measure is increasing, but still lower than the desired amount, and Stop
                // still has not been pressed on the Driver Station...

                heading = sensorGyro.getHeading(); // ...continue to update the variable heading with
                // real time heading data (this is SUPER IMPORTANT - putting this line inside the while
                // loop makes this operation continuous)...

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
    }

    public void shoot () { // This public void is called in the FacingCrater op modes whenever the
                           // robot has to shoot the first ball. It does not have any parameters.

        if (opModeIsActive()) { // If Stop is not pressed on the Driver Station

            motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // This line resets the
                                                                          // encoder count of the
                                                                          // shooter motor as a
                                                                          // precautionary measure.
            motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Because we will be using
                                                                     // an encoder in this method,
                                                                     // we need to set the shooter
                                                                     // motor to this mode so that
                                                                     // it is able to utilize it.

            double GEAR_RATIO = 1.456; // The gear ratio of the shooter motor
            int PULSES = 1680; // The number of encoder counts per full rotation of a Neverest 60
            // motor
            double COUNTS = PULSES * GEAR_RATIO; // Calculating the number of encoder counts that
            // when reached the shooter motor will stop (based on the number of encoder counts per
            // full rotation and the gear ratio) (it will make one full rotation)

            COUNTS = (COUNTS + Math.abs((double)motorShooter.getCurrentPosition())) / 16.0; // This
            // line makes it so that the target encoder count is the desired count plus the current
            // count and the method can be run over and over again without resetting the encoders.
            // Dividing it all by 16 makes the shooter motor only rotate one sixteenth of a full
            // rotation.

            motorShooter.setPower(1.0); // This line sets the power of the shooter motor to full
                                        // power.

            while ((double)motorShooter.getCurrentPosition() < COUNTS && opModeIsActive()) {
                // "While the shooter motor is rotating and the encoder count is increasing, but
                // still lower than the desired count, and Stop still has not been pressed on the
                // Driver Station...

                // ...do nothing else in the meantime."
            }

            motorShooter.setPower(0.0); // "Once the desired encoder count is reached, stop the
                                        // shooter motor."
        }
    }

    public void shoot2 () { // This public void is called in the FacingCrater op modes whenever the
        // robot has to reset between shooting the first ball and the second ball. It does not have
        // any parameters.

        if (opModeIsActive()) { // If Stop is not pressed on the Driver Station

            motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // This line resets the
            // encoder count of the
            // shooter motor as a
            // precautionary measure.
            motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Because we will be using
            // an encoder in this method,
            // we need to set the shooter
            // motor to this mode so that
            // it is able to utilize it.

            double GEAR_RATIO = 1.456; // The gear ratio of the shooter motor
            int PULSES = 1680; // The number of encoder counts per full rotation of a Neverest 60
            // motor
            double COUNTS = PULSES * GEAR_RATIO; // Calculating the number of encoder counts that
            // when reached the shooter motor will stop (based on the number of encoder counts per
            // full rotation and the gear ratio) (it will make one full rotation)

            COUNTS = (COUNTS + Math.abs((double)motorShooter.getCurrentPosition())) * (3.0/4.0);
            // This line makes it so that the target encoder count is the desired count plus the
            // current count and the method can be run over and over again without resetting the
            // encoders. Multiplying it all by 3/4 makes the shooter motor only rotate three-fourths
            // of a full rotation.

            motorShooter.setPower(1.0); // This line sets the power of the shooter motor to full
            // power.

            while ((double)motorShooter.getCurrentPosition() < COUNTS && opModeIsActive()) {
                // "While the shooter motor is rotating and the encoder count is increasing, but
                // still lower than the desired count, and Stop still has not been pressed on the
                // Driver Station...

                // ...do nothing else in the meantime."
            }

            motorShooter.setPower(0.0); // "Once the desired encoder count is reached, stop the
            // shooter motor."
        }
    }

    public void shoot3 () { // This public void is called in the FacingCrater op modes whenever the
        // robot has to shoot the second ball. It does not have any parameters.

        if (opModeIsActive()) { // If Stop is not pressed on the Driver Station

            motorShooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // This line resets the
            // encoder count of the
            // shooter motor as a
            // precautionary measure.
            motorShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Because we will be using
            // an encoder in this method,
            // we need to set the shooter
            // motor to this mode so that
            // it is able to utilize it.

            double GEAR_RATIO = 1.456; // The gear ratio of the shooter motor
            int PULSES = 1680; // The number of encoder counts per full rotation of a Neverest 60
            // motor
            double COUNTS = PULSES * GEAR_RATIO; // Calculating the number of encoder counts that
            // when reached the shooter motor will stop (based on the number of encoder counts per
            // full rotation and the gear ratio) (it will make one full rotation)

            COUNTS = (COUNTS + Math.abs((double)motorShooter.getCurrentPosition())) / 6.0; // This
            // line makes it so that the target encoder count is the desired count plus the current
            // count and the method can be run over and over again without resetting the encoders.
            // Dividing it all by 6 makes the shooter motor only rotate one sixth of a full
            // rotation.

            motorShooter.setPower(1.0); // This line sets the power of the shooter motor to full
            // power.

            while ((double)motorShooter.getCurrentPosition() < COUNTS && opModeIsActive()) {
                // "While the shooter motor is rotating and the encoder count is increasing, but
                // still lower than the desired count, and Stop still has not been pressed on the
                // Driver Station...

                // ...do nothing else in the meantime."
            }

            motorShooter.setPower(0.0); // "Once the desired encoder count is reached, stop the
            // shooter motor."
        }
    }

    public void setMotorPower (double left, double right) { // This public void, when implemented
                                                            // above, sets the power of the motors.
                                                            // Whatever is inputted into each
                                                            // parameter above is then substituted
                                                            // into its corresponding spot in the
                                                            // public void.

        if (opModeIsActive()) { // If Stop is not pressed on the Driver Station

            motorBackLeft.setPower(left);    // These lines set the power of the motors to the
            motorBackRight.setPower(right);  // desired left and right powers.
            motorFrontLeft.setPower(left);
            motorFrontRight.setPower(right);
        }
    }
}