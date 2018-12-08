package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

/**
 * Created by Alex Snyder on 3/1/18.
 */

public abstract class IndieRoboRaidersAuto extends LinearOpMode {

    public VuforiaLocalizer vuforia;
    public VuforiaTrackable relicTemplate;
    public String pictograph = "UNKNOWN";
    public boolean cur_Y_ButtonState = false;
    public boolean prev_Y_ButtonState = false;
    public double distanceFromSideWall = 0;
    public double distanceBackSensor = 0;
    public double distanceFrontSensor = 0;

    //                                                    +----------+----------+--------+
    //                                                    |          |          |        |
    //                                                    | Alliance |Balancing | Column |
    //                                                    |          |  Stone   |        |
    //                                                    +----------+----------+--------+
    public double RED_CLOSE_LEFT_DISTANCE = 35.5;      // |  Red     |  Close   |  Left  |
    public double RED_CLOSE_CENTER_DISTANCE = 28.5;    // |  Red     |  Close   | Center |
    public double RED_CLOSE_RIGHT_DISTANCE = 22.5;     // |  Red     |  Close   |  Right |
    //                                                    +----------+----------+--------+
    public double RED_FAR_LEFT_DISTANCE = 11.00;       // |  Red     |   Far    |  Left  |
    public double RED_FAR_CENTER_DISTANCE = 4.25;      // |  Red     |   Far    | Center |
    public double RED_FAR_RIGHT_DISTANCE = 1.75;       // |  Red     |   Far    |  Right |
    //                                                    +----------+----------+--------+
    public double BLUE_CLOSE_LEFT_DISTANCE = 22.25;    // |  Blue    |  Close   |  Left  |
    public double BLUE_CLOSE_CENTER_DISTANCE = 26.5;   // |  Blue    |  Close   | Center |
    public double BLUE_CLOSE_RIGHT_DISTANCE = 35.5;    // |  Blue    |  Close   |  Right |
    //                                                    +----------+----------+--------+
    public double BLUE_FAR_LEFT_DISTANCE = 1.0;        // |  Blue    |   Far    |  Left  |
    public double BLUE_FAR_CENTER_DISTANCE = 5.0;      // |  Blue    |   Far    | Center |
    public double BLUE_FAR_RIGHT_DISTANCE = 10.75;     // |  Blue    |   Far    |  Right |
    //                                                    +----------+----------+--------+

    public double RED_FAR_BACKWARD_DISTANCE = 21.5;    // Distance robot drives backwards off of the RED, Far balance stone
    public double BLUE_FAR_FORWARD_DISTANCE = 20.5;    // Distance robot drives forward off of the BLUE, Far balance stone

    public double MAX_DISTANCE = 50.0;                      // The maximum from the wall, used to display alignment menu
    public double FINAL_JEWEL_ARM_SERVO_POSITION = 0.85;    // Position where the jewel arm needs to be to read the jewel color

    /**
     * This method will initialize Vuforia in FacingCrater op modes
     *
     * @param hwMap the hardware map we will be using
     */
    public void vuforiaInitialization(HardwareMap hwMap) {

        // Vuforia initialization
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AedUDNP/////AAAAGXH2ZpUID0KanSX9ZSR37LKFSFokxIqmy/g0BNepdA9EepixxnO00qygLnMJq3Fg9gZxnkUJaKgk14/UjhxPWVQIs90ZXJLc21NvQvOeZ3dOogagVP8yFnFQs2xCijGmC/CE30ojlAnbhAhqz1y4tZPW2QkK5Qt0xCakTTSAw3KPQX2mZxX+qMxI2ljrN0eaxaKVnKnAUl8x3naF1mez7f9c8Xdi1O5auL0ePdG6bJhWjEO1YwpSd8WkSzNDEkmw20zpQ7zaOOPw5MeUQUr9vAS0fef0GnLjlS1gb67ajUDlEcbbbIeSrLW/oyRGTil8ueQC2SWafdspSWL3SJNaQKWydies23BxJxM/FoLuYYjx";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();
    }

    /**
     * This method will to push the jewel off the platform that is not the current alliance color
     *
     * @param bot           the bot currently being worked on
     * @param allianceColor the color of your alliance
     */
    public void selectJewel(IndieRobot bot, String allianceColor) throws InterruptedException {

        if (allianceColor.equals("red")) { //red alliance

            if (bot.getColorIntensity("red") > bot.getColorIntensity("blue"))  { //if the ball on the right is red

                telemetry.addLine().addData("Red", bot.getColorIntensity("red"));
                telemetry.addLine().addData("Blue", bot.getColorIntensity("blue"));
                telemetry.update();

                flickLeft(bot);
                Thread.sleep(250);

                bot.setJewelServoPosition(0.4); //move arm back to initialization position
                Thread.sleep(250);

                returnFromLeft(bot);
                Thread.sleep(250);
            }
            else { //the ball on the right is blue

                telemetry.addLine().addData("Red", bot.getColorIntensity("red"));
                telemetry.addLine().addData("Blue", bot.getColorIntensity("blue"));
                telemetry.update();

                flickRight(bot);
                Thread.sleep(250);

                bot.setJewelServoPosition(0.4); //move arm back to initialization position
                Thread.sleep(250);

                returnFromRight(bot);
                Thread.sleep(250);
            }
        }
        else if (allianceColor.equals("blue")) { //not red alliance (blue alliance)

            if (bot.getColorIntensity("blue") > bot.getColorIntensity("red")) { //if the ball on the right is blue

                telemetry.addLine().addData("Red", bot.getColorIntensity("red"));
                telemetry.addLine().addData("Blue", bot.getColorIntensity("blue"));
                telemetry.update();

                flickLeft(bot);
                Thread.sleep(250);

                bot.setJewelServoPosition(0.4); //move arm back to initialization position
                Thread.sleep(250);

                returnFromLeft(bot);
                Thread.sleep(250);
            }
            else { //the ball on the left is blue

                telemetry.addLine().addData("Red", bot.getColorIntensity("red"));
                telemetry.addLine().addData("Blue", bot.getColorIntensity("blue"));
                telemetry.update();

                flickRight(bot);
                Thread.sleep(250);

                bot.setJewelServoPosition(0.4); //move arm back to initialization position
                Thread.sleep(250);

                returnFromRight(bot);
                Thread.sleep(250);
            }
        }

        Thread.sleep(500);
    }

    /**
     * This method directs the robot to place the glyph in the key column
     *
     * @param bot           the bot currently being worked on
     * @param allianceColor the color of your alliance
     * @param alliancePlacement the placement of the alliance: either close or far
     * @param pictograph    the name of the pictograph as determined by getRelicRecoveryVuMark()
     * @throws InterruptedException
     */
    public void selectColumn(IndieRobot bot, String allianceColor, String alliancePlacement, String pictograph) throws InterruptedException {

        /*
         Handle RED alliance
         */
        if (allianceColor.equals("red")) { //if we are on the red side

            /*
             Positioned on the "CLOSE" balancing stone
             */
            if (alliancePlacement.equals("close")) { //if we are close to the audience

                if (pictograph.equals("LEFT")) { //if the pictograph says that the key column is the left column

                    encodersMove(bot, RED_CLOSE_LEFT_DISTANCE, 0.5, "backward"); //move backward until in front of the left column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("CENTER")) { //else if the pictograph says that the key column is the center column

                    encodersMove(bot, RED_CLOSE_CENTER_DISTANCE, 0.5, "backward"); //move backward until in front of the center column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("RIGHT")) { //else if the pictograph says that the key column is the right column

                    encodersMove(bot, RED_CLOSE_RIGHT_DISTANCE, 0.5, "backward"); //move backward until in front of the right column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("UNKNOWN")) { //else if the pictograph cannot determine which column is the key column

                    encodersMove(bot, RED_CLOSE_CENTER_DISTANCE, 0.5, "backward"); //move backward until in front of the center column (default)
                    Thread.sleep(250);
                }

                imuTurn(bot, 90, 0.5, "right"); //turn left 90 degrees
                Thread.sleep(250);
            }

            /*
             Positioned on the "FAR" balancing stone
             */
            else if (alliancePlacement.equals("far")) { //if we are far from the audience

                encodersMove(bot, RED_FAR_BACKWARD_DISTANCE, 0.5, "backward"); //drive backward
                Thread.sleep(500);

                imuTurn(bot, 90, 0.5, "right"); //turn right 90 degrees
                Thread.sleep(250);

                if (pictograph.equals("LEFT")) { //if the pictograph says that the key column is the left column

                    encodersMove(bot, RED_FAR_LEFT_DISTANCE, 0.5, "forward"); //move forward until in front of the left column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("CENTER")) { //else if the pictograph says that the key column is the center column

                    encodersMove(bot, RED_FAR_CENTER_DISTANCE, 0.5, "forward"); //move forward until in front of the center column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("RIGHT")) { //else if the pictograph says that the key column is the right column

                    encodersMove(bot, RED_FAR_RIGHT_DISTANCE, 0.5, "forward"); //move forward until in front of the right column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("UNKNOWN")) { //else if the pictograph cannot determine which column is the key column

                    encodersMove(bot, RED_FAR_CENTER_DISTANCE, 0.5, "forward"); //move forward until in front of the center column (default)
                    Thread.sleep(250);
                }

                imuTurn(bot, 90, 0.5, "left"); //turn right 90 degrees
                Thread.sleep(250);
            }
        }

        /*
         Handle BLUE alliance
         */
        else if (allianceColor.equals("blue")) { //else if we are on the blue side

            /*
             Positioned on the "CLOSE" balancing stone
             */
            if (alliancePlacement.equals("close")) {  //if we are close to the audience

                if (pictograph.equals("LEFT")) { //if the pictograph says that the key column is the left column

                    //encodersMove(bot, BLUE_CLOSE_LEFT_DISTANCE, 0.5, "forward"); //move forward until in front of the left column
                    encodersMoveWithGyro(bot,                        // The robot we are working on
                            BLUE_CLOSE_LEFT_DISTANCE,   // The distance the robot is to travel
                            0.5,                        // The left power - its more because of the drift of the robot
                            0.5,                        // The right power - its less because of the drift of the robot
                            "forward");                 // Move "forward" until in front of the left column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("CENTER")) { //else if the pictograph 1says that the key column is the center column

                    //encodersMove(bot, BLUE_CLOSE_CENTER_DISTANCE, 0.5, "forward"); //move forward until in front of the center column
                    encodersMoveWithGyro(bot,                        // The robot we are working on
                            BLUE_CLOSE_CENTER_DISTANCE, // The distance the robot is to travel
                            0.6,                        // The left power - its more because of the drift of the robot
                            0.5,                        // The right power - its less because of the drift of the robot
                            "forward");                 // Move "forward" until in front of the left column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("RIGHT")) { //else if the pictograph says that the key column is the right column

                    //encodersMove(bot, BLUE_CLOSE_RIGHT_DISTANCE, 0.5, "forward"); //move forward until in front of the right column
                    encodersMoveWithGyro(bot,                       // The robot we are working on
                            BLUE_CLOSE_RIGHT_DISTANCE,  // The distance the robot is to travel
                            0.5,                        // The left power - its more because of the drift of the robot
                            0.5,                        // The right power - its less because of the drift of the robot
                            "forward");                 // Move "forward" until in front of the right column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("UNKNOWN")) { //else if the pictograph cannot determine which column is the key column

                    //encodersMove(bot, BLUE_CLOSE_CENTER_DISTANCE, 0.5, "forward"); //move forward until in front of the center column (default)
                    encodersMoveWithGyro(bot,                        // The robot we are working on
                            BLUE_CLOSE_CENTER_DISTANCE, // The distance the robot is to travel
                            0.6,                        // The left power - its more because of the drift of the robot
                            0.5,                        // The right power - its less because of the drift of the robot
                            "forward");                 // Move "forward" until in front of the left column
                    Thread.sleep(250);
                }

                imuTurn(bot, 90, 0.5, "right"); //turn left 90 degrees
                Thread.sleep(250);
            }

            /*
             Positioned on the "FAR" balancing stone
             */
            else if (alliancePlacement.equals("far")) { //if we are far from the audience

                encodersMove(bot, BLUE_FAR_FORWARD_DISTANCE, 0.4, "forward"); //drive forward
                Thread.sleep(250);

                imuTurn(bot, 88, 0.5, "right"); //turn right 90 degrees
                Thread.sleep(250);

                if (pictograph.equals("LEFT")) { //if the pictograph says that the key column is the left column

                    encodersMove(bot, BLUE_FAR_LEFT_DISTANCE, 0.5, "forward"); //move forward until in front of the left column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("CENTER")) { //else if the pictograph says that the key column is the center column

                    encodersMove(bot, BLUE_FAR_CENTER_DISTANCE, 0.5, "forward"); //move forward until in front of the center column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("RIGHT")) { //else if the pictograph says that the key column is the right column

                    encodersMove(bot, BLUE_FAR_RIGHT_DISTANCE, 0.5, "forward"); //move forward until in front of the right column
                    Thread.sleep(250);
                }
                else if (pictograph.equals("UNKNOWN")) { //else if the pictograph cannot determine which column is the key column

                    encodersMove(bot, BLUE_FAR_CENTER_DISTANCE, 0.5, "forward"); //move forward until in front of the center column (default)
                    Thread.sleep(250);
                }

                imuTurn(bot, 90, 0.5, "right"); //turn left 90 degrees
                Thread.sleep(250);
            }
        }

        placeGlyph(bot);
        Thread.sleep(250);
    }

    /**
     * This method will turn the robot right or left a certain angle measure using the IMU
     *
     * @param bot       the bot currently being worked on
     * @param degrees   the desired number of degrees to turn
     * @param power     the desired power the wheel motors will run at
     * @param direction the direction the robot is turning; either right or left
     */
    public void imuTurn(IndieRobot bot, float degrees, double power, String direction) { //gets hardware from
        //Robot and defines degrees as a
        //float, power as a double, and direction as a string

        bot.resetIMU(); //resets IMU angle to zero

        bot.getHeading(); //returns the current heading of the IMU

        if (direction.equals("right")) { //if the desired direction is right

            bot.setDriveMotorPower(power, -power, power, -power); //the robot will turn right
        }
        else if (direction.equals("left")) { //if the desired direction is left

            bot.setDriveMotorPower(-power, power, -power, power); //the robot will turn left
        }

        while (bot.getHeading() < (degrees - 20) && opModeIsActive()) { //while the value of getHeading is
            //less then the degree value
            //and while opMode is active continue the while loop

            telemetry.addData("Heading", bot.getHeading()); //feedback of getHeading value
            telemetry.update(); //continuous update
        }

        bot.setDriveMotorPower(0.0, 0.0, 0.0, 0.0); //stops robot
    }

    /**
     * This program will make the robot move forwards, backwards, right, or left with encoders
     *
     * @param bot       the robot currently being worked on
     * @param distance  the distance the robot should travel in inches
     * @param power     the speed the robot will travel at
     * @param direction the direction the robot will travel: either forward, backward, right, or left
     */
    public void encodersMove(IndieRobot bot, double distance, double power, String direction) { //sets the parameters

        bot.resetEncoders(); //resets encoders
        bot.runWithEncoders(); //sets the mode back to run with encoder

        double COUNTS = bot.calculateCOUNTS(distance); //COUNTS is now equal to the value calculated

        if (direction.equals("forward")) { //if the desired direction is forward

            bot.setDriveMotorPower(power, power, power, power); //start driving forward

            while (bot.getSortedEncoderCount() < COUNTS && opModeIsActive()) { //while the current count is
                //still less than the desired count and the opMode has not been stopped

                telemetry.addData("COUNTS", COUNTS);
                telemetry.addData("Encoder Count", bot.getSortedEncoderCount());
                telemetry.update();
            }

            bot.setDriveMotorPower(0, 0, 0, 0); //stop the robot
        }
        else if (direction.equals("backward")) { //if the desired direction is backward

            bot.setDriveMotorPower(-power, -power, -power, -power); //start driving backward

            while (bot.getSortedEncoderCount() < COUNTS && opModeIsActive()) { //while the current count is
                //still greater than the desired count and the opMode has not been stopped

                telemetry.addData("COUNTS", COUNTS);
                telemetry.addData("Encoder Count", bot.getSortedEncoderCount());
                telemetry.update();
            }

            bot.setDriveMotorPower(0, 0, 0, 0); //stop the robot
        }
        else if (direction.equals("right")) { //if the desired direction is right

            bot.setDriveMotorPower(power, -power, -power, power); //start strafing right

            while (bot.getSortedEncoderCount() < COUNTS && opModeIsActive()) { //while the current count is
                //still less than the desired count and the opMode has not been stopped

                telemetry.addData("COUNTS", COUNTS);
                telemetry.addData("Encoder Count", bot.getSortedEncoderCount());
                telemetry.update();
            }

            bot.setDriveMotorPower(0.0, 0.0, 0.0, 0.0); //stop the robot
        }
        else if (direction.equals("left")) { //if the desired direction is left

            bot.setDriveMotorPower(-power, power, power, -power); //start strafing left

            while (bot.getSortedEncoderCount() < COUNTS && opModeIsActive()) { //while the current count is
                //still greater than the desired count and the opMode has not been stopped

                telemetry.addData("COUNTS", COUNTS);
                telemetry.addData("Encoder Count", bot.getSortedEncoderCount());
                telemetry.update();
            }

            bot.setDriveMotorPower(0.0, 0.0, 0.0, 0.0); //stop the robot
        }

        bot.runWithoutEncoders(); //sets the mode back to run without encoder
    }

    /**
     * This method is "like" encodersMove method in that it will run the motors with encoders, however, it will
     * use the IMU in the REV hub to compensate the robot drift as it travels.
     *
     * @param bot - the robot we are working on
     * @param distance - the distance the robot is to travel in inches
     * @param leftPower - the left power that is to be applied to the left motors on the robot
     * @param rightPower - the right power that is to be applied to the right motors on the robot
     * @param direction - the direction to travel
     */
    public void encodersMoveWithGyro(IndieRobot bot, double distance, double leftPower, double rightPower, String direction) {

        double powerMultiplier;              // Either -1 or 1 depending on the direction, -1 for backward and 1 for forward
        double robotCurrentHeading;          // The current heading of the robot
        double INTENDED_ROBOT_HEADING = 0.0; // The intended direction of the robot, in this case 0 degrees
        double newLeftPower;
        double newRightPower;

        bot.resetEncoders();       //resets encoders
        bot.runWithEncoders();     //sets the mode back to run with encoder

        // Determine the direction the motors will spin
        if (direction.equals("forward"))
            powerMultiplier = 1.0;      // Stay positive; move forward
        else
            powerMultiplier = -1.0;     // Be negative; move backward

        double COUNTS = bot.calculateCOUNTS(distance); // Convert distance to encoder counts

        // Continue to move the robot until it reaches its destination or the op mode is stopped
        while (bot.getSortedEncoderCount() < COUNTS && opModeIsActive()) {

            // Get the current heading, then adjust the powers on the left and right side to
            // straighten the robot as it travels
//            robotCurrentHeading = bot.getIntegratedZAxis();
            robotCurrentHeading = bot.getRobotHeading();
            newLeftPower = leftPower + ((robotCurrentHeading - INTENDED_ROBOT_HEADING) / 100);
            newRightPower = rightPower - ((robotCurrentHeading - INTENDED_ROBOT_HEADING) / 100);

            // Calculate the final power and direction
            newLeftPower = powerMultiplier * Range.clip(newLeftPower, -(leftPower + 0.1), (leftPower + 0.1));
            newRightPower = powerMultiplier * Range.clip(newRightPower, -(rightPower + 0.1), (rightPower + 0.1));

            // Set the motor powers
            bot.setDriveMotorPower(newLeftPower, newRightPower, newLeftPower, newRightPower);

            // Tell the drivers what is going on
            telemetry.addData("COUNTS", COUNTS);
            telemetry.addData("Encoder Count", bot.getSortedEncoderCount());
            telemetry.addData("robotCurrentHeading", robotCurrentHeading);
            telemetry.update();
        }

        // Robot has travelled the distance so stop it
        bot.setDriveMotorPower(0, 0, 0, 0);

        // Reset the motors to run with out encoders
        bot.runWithoutEncoders();
    }

    /**
     * This method will determine the name of the pictograph the robot sees
     */
    public void getRelicRecoveryVuMark() {

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate); //the variable vuMark is now the name of
        //the pictograph the robot currently sees

        if (vuMark.equals(RelicRecoveryVuMark.LEFT)) { //if vuMark is left

            pictograph = "LEFT"; //pictograph is set equal to left
        }
        else if (vuMark.equals(RelicRecoveryVuMark.CENTER)) { //else if vuMark is center

            pictograph = "CENTER"; //pictograph is set equal to center
        }
        else if (vuMark.equals(RelicRecoveryVuMark.RIGHT)) { //else if vuMark is right

            pictograph = "RIGHT"; //pictograph is set equal to right
        }
        else if (vuMark.equals(RelicRecoveryVuMark.UNKNOWN)) { //else if the robot cannot determine the name of the pictograph

            pictograph = "UNKNOWN"; //pictograph is set equal to unknown
        }

        telemetry.addData("Pictograph", pictograph);
        telemetry.update();
    }

    /**
     * This method lowers the servoJewel arm
     *
     * @param bot the robot currently being worked on
     * @throws InterruptedException
     */
    public void lowerArm(IndieRobot bot) throws InterruptedException {

        double amountToMove;

        bot.setJewelServoPosition(0.69);
        Thread.sleep(250);

        bot.setElbowServoPosition(0.21);
        Thread.sleep(500);

        double servoJewelPosition = bot.getJewelServoPosition(); //sets getPosition() to servoPosition

        while (servoJewelPosition < FINAL_JEWEL_ARM_SERVO_POSITION && opModeIsActive()) { //while the op mode is active and while the servo position variable is less
            //than the final position

            amountToMove = FINAL_JEWEL_ARM_SERVO_POSITION - servoJewelPosition; //calculate remaining distance to rotate servo

            if (amountToMove < 0.05){

                servoJewelPosition = servoJewelPosition + amountToMove; //increment to final position
            }
            else {

                servoJewelPosition = servoJewelPosition + 0.05; //add 0.05 to the current servoPosition variable
            }

            bot.setJewelServoPosition(servoJewelPosition);
            Thread.sleep(75); //wait 0.075 seconds (75 milliseconds)
        }

        Thread.sleep(500);
    }

    /**
     * This method flicks the elbow servo left
     *
     * @param bot the robot currently being worked on
     * @throws InterruptedException
     */
    public void flickLeft(IndieRobot bot) throws InterruptedException {

        double servoElbowPosition = bot.getElbowServoPosition(); //sets getPosition() to servoPosition

        while (servoElbowPosition < 0.3 && opModeIsActive()) {   //while the op mode is active and while the servo position variable is less
            //than 0.3

            servoElbowPosition = servoElbowPosition + 0.05;      //add 0.05 to the current servoPosition variable
            bot.setElbowServoPosition(servoElbowPosition);
            Thread.sleep(75);                                    //wait 0.075 seconds (75 milliseconds)
        }
    }

    /**
     * This method returns the elbow servo from the left
     *
     * @param bot the robot currently being worked on
     * @throws InterruptedException
     */
    public void returnFromLeft(IndieRobot bot) throws InterruptedException {

        double servoElbowPosition = bot.getElbowServoPosition(); //sets getPosition() to servoPosition

        while (servoElbowPosition > 0.21 && opModeIsActive()) {  //while the op mode is active and while the servo position variable is greater
            //than 0.21

            servoElbowPosition = servoElbowPosition - 0.05;      //add 0.05 to the current servoPosition variable
            bot.setElbowServoPosition(servoElbowPosition);
            Thread.sleep(75);                                    //wait 0.075 seconds (75 milliseconds)
        }
    }

    /**
     * This method flicks the elbow servo right
     *
     * @param bot the robot currently being worked on
     * @throws InterruptedException
     */
    public void flickRight(IndieRobot bot) throws InterruptedException {

        double servoElbowPosition = bot.getElbowServoPosition(); //sets getPosition() to servoPosition

        while (servoElbowPosition > 0.1 && opModeIsActive()) {   //while the op mode is active and while the servo position variable is less
            //than 0.1

            servoElbowPosition = servoElbowPosition - 0.05;      //subtract 0.05 from the current servoPosition variable
            bot.setElbowServoPosition(servoElbowPosition);
            Thread.sleep(75);                                    //wait 0.075 seconds (75 milliseconds)
        }
    }

    /**
     * This method returns the elbow servo from the right
     *
     * @param bot the robot currently being worked on
     * @throws InterruptedException
     */
    public void returnFromRight(IndieRobot bot) throws InterruptedException {

        double servoElbowPosition = bot.getElbowServoPosition(); //sets getPosition() to servoPosition

        while (servoElbowPosition < 0.21 && opModeIsActive()) {  //while the op mode is active and while the servo position variable is less
            //than 0.21

            servoElbowPosition = servoElbowPosition + 0.05;      //add 0.05 to the current servoPosition variable
            bot.setElbowServoPosition(servoElbowPosition);
            Thread.sleep(75);                                    //wait 0.075 seconds (75 milliseconds)
        }
    }

    /**
     * This method places the pre-loaded glyph in the cryptobox
     *
     * @param bot - the bot currently being worked on
     * @throws InterruptedException
     */
    public void placeGlyph(IndieRobot bot) throws InterruptedException {

        bot.glyphPivotCarry();
        Thread.sleep(250);

        bot.glyphPivotDeposit();
        Thread.sleep(1000);

        bot.glyphPivotRest();
        Thread.sleep(250);

        encodersMove(bot, 4, 0.5, "backward");
        Thread.sleep(250);

        encodersMove(bot, 2, 0.5, "forward");
        Thread.sleep(250);
    }

    /**
     * This method will help the drive team to align the robot prior to FacingCrater using the range sensor
     *
     * @param bot the bot currently being worked on
     */
    public void alignRobot(IndieRobot bot, String allianceColor, String balancingStone) {

        String sideHdrDistPrompt = "NADA";
        String sideDistPrompt = "NADA";

        String backHdr1DistPrompt = "NADA";
        String backHdr2DistPrompt = "NADA";
        String backDistPrompt = "NADA";

        String frontHdr1DistPrompt = "NADA";
        String frontHdr2DistPrompt = "NADA";
        String frontDistPrompt = "NADA";

        gamepad1.reset();

        while (!prev_Y_ButtonState) {

            // Telemetry
            telemetry.addLine("Aligning Robot:");

            // Get distances from side, front, and back sensors
            distanceFromSideWall = bot.getSideDistance();
            distanceBackSensor = bot.getBackDistance();
            distanceFrontSensor = bot.getFrontDistance();

            /*
             Sometimes we get very large numbers displayed and it messes everything up on the display
             for a second or two.  This makes it hard for the drive team to read what is going on.  So
             if all of the distance values are less than 50.0, then display the messages.  Else skip the
             message display this time and get new values.
             */

            if ((distanceFromSideWall < MAX_DISTANCE) &&              // Distance from side wall less than max   -AND-
                    (distanceBackSensor < MAX_DISTANCE) &&            // Distance from back wall less then max   -AND-
                    (distanceFrontSensor < MAX_DISTANCE)) {           // Distance from front wall less than max

                sideHdrDistPrompt = String.format(Locale.US, "Side %.2f inches", distanceFromSideWall);
                if (distanceFromSideWall < 13.2) {

                    sideDistPrompt = "Move the robot farther away from the side wall.";
                } else if (distanceFromSideWall >= 13.2 && distanceFromSideWall <= 14.0) {

                    sideDistPrompt = "The robot is the correct distance away from the side wall.";
                } else if (distanceFromSideWall > 14.0) {

                    sideDistPrompt = "Move the robot closer to the side wall.";
                }
                else {

                    sideDistPrompt = "Please place the robot on the field.";
                }

                /*
                Alliance is BLUE and Balancing Stone is CLOSE
                */
                if (allianceColor.equals("blue") && balancingStone.equals("close")) {

                    frontHdr1DistPrompt = "IDEAL DISTANCE FROM FRONT WALL IS 14.17 INCHES";
                    frontHdr2DistPrompt = String.format(Locale.US, "Front %.2f inches", distanceBackSensor);

                    if (distanceBackSensor < 13.76) {

                        frontDistPrompt = "Move the robot farther away from the front wall.";
                    } else if (distanceBackSensor >= 13.76 && distanceBackSensor <= 14.6) {

                        frontDistPrompt = "The robot is the correct distance away from the front wall.";
                    } else if (distanceBackSensor > 14.6) {

                        frontDistPrompt = "Move the robot closer to the front wall.";
                    } else {

                        frontDistPrompt = "Please place the robot on the field.";
                    }
                }

                /*
                Alliance is BLUE and Balancing Stone is FAR
                */
                else if (allianceColor.equals("blue") && balancingStone.equals("far")) {

                    backHdr1DistPrompt = "IDEAL DISTANCE FROM BACK WALL IS 36.22 INCHES";
                    backHdr2DistPrompt = String.format(Locale.US, "Back %.2f inches", distanceFrontSensor);

                    if (distanceFrontSensor < 35.82) {

                        backDistPrompt = "Move the robot farther away from the back wall.";
                    } else if (distanceFrontSensor >= 35.82 && distanceFrontSensor <= 36.62) {

                        backDistPrompt = "The robot is the correct distance away from the back wall.";
                    } else if (distanceFrontSensor > 36.62) {

                        backDistPrompt = "Move the robot closer to the back wall.";
                    } else {

                        backDistPrompt = "Please place the robot on the field.";
                    }
                }
                /*
                Alliance is RED and Balance Stone is CLOSE
                */
                else if (allianceColor.equals("red") && balancingStone.equals("close")) {

                    frontHdr1DistPrompt = "IDEAL DISTANCE FROM FRONT WALL IS 14.57 INCHES";
                    frontHdr2DistPrompt = String.format(Locale.US, "Front %.2f inches", distanceFrontSensor);

                    if (distanceFrontSensor < 14.47) {

                        frontDistPrompt = "Move the robot farther away from the front wall.";
                    } else if (distanceFrontSensor >= 14.47 && distanceFrontSensor <= 14.67) {

                        frontDistPrompt = "The robot is the correct distance away from the front wall.";
                    } else if (distanceFrontSensor > 14.67) {

                        frontDistPrompt = "Move the robot closer to the front wall.";
                    } else {

                        frontDistPrompt = "Please place the robot on the field.";
                    }
                }

                /*
                Alliance is RED and Balance Stone is FAR
                */
                else if (allianceColor.equals("red") && balancingStone.equals("far")) {

                    backHdr1DistPrompt = "IDEAL DISTANCE FROM BACK WALL IS 36.22 INCHES";
                    backHdr2DistPrompt = String.format(Locale.US, "Back %.2f inches", distanceBackSensor);

                    if (distanceBackSensor < 36.12) {

                        backDistPrompt = "Move the robot farther away from the back wall.";
                    } else if (distanceBackSensor >= 36.12 && distanceBackSensor <= 36.32) {

                        backDistPrompt = "The robot is the correct distance away from the back wall.";
                    } else if (distanceBackSensor > 36.32) {

                        backDistPrompt = "Move the robot closer to the back wall.";
                    } else {

                        backDistPrompt = "Please place the robot on the field.";
                    }
                }
            }

            telemetry.addLine(sideHdrDistPrompt);
            telemetry.addLine(sideDistPrompt);

            if (balancingStone.equals("far")) {
                telemetry.addLine(backHdr1DistPrompt);
                telemetry.addLine(backHdr2DistPrompt);
                telemetry.addLine(backDistPrompt);
            } else {
                telemetry.addLine(frontHdr1DistPrompt);
                telemetry.addLine(frontHdr2DistPrompt);
                telemetry.addLine(frontDistPrompt);
            }

            // Button functionality
            cur_Y_ButtonState = gamepad1.y;                           // get the current state of button "y"
            if (cur_Y_ButtonState) {                                  // when the "y" button on the gamepad is pushed

                if (!prev_Y_ButtonState) {                            // when the previous "y" button was NOT pushed

                    prev_Y_ButtonState = true;                        // indicate that the previous y button state is PUSHED
                }
            }

            // More telemetry
            telemetry.addLine("Press Y when the robot is aligned.");
            telemetry.update();
        }
    }

    /**
     * This method will run the movement code in JewelCloseBlue for use in IndieAutonomousOptions
     *
     * @param bot the bot currently being worked on
     * @throws InterruptedException
     */
    public void justParkCloseBlue(IndieRobot bot) throws InterruptedException {

        encodersMove(bot, 32, 0.4, "forward");
        Thread.sleep(500);

        imuTurn(bot, 90, 0.4, "left");
        Thread.sleep(500);

        encodersMove(bot, 2, 0.4, "forward");
        Thread.sleep(500);
    }

    /**
     * This method will run the movement code in JewelFarBlue for use in IndieAutonomousOptions
     *
     * @param bot the bot currently being worked on
     * @throws InterruptedException
     */
    public void justParkFarBlue(IndieRobot bot) throws InterruptedException {

        encodersMove(bot, 22, 0.4, "forward");
        Thread.sleep(500);

        encodersMove(bot, 18, 0.4, "right");
        Thread.sleep(500);
    }

    /**
     * This method will run the movement code in JewelCloseRed for use in IndieAutonomousOptions
     *
     * @param bot the bot currently being worked on
     * @throws InterruptedException
     */
    public void justParkCloseRed(IndieRobot bot) throws InterruptedException {

        encodersMove(bot, 32, 0.4, "backward");
        Thread.sleep(500);

        imuTurn(bot, 90, 0.4, "left");
        Thread.sleep(500);

        encodersMove(bot, 2, 0.4, "forward");
        Thread.sleep(500);
    }

    /**
     * This method will run the movement code in JewelFarRed for use in IndieAutonomousOptions
     *
     * @param bot the bot currently being worked on
     * @throws InterruptedException
     */
    public void justParkFarRed(IndieRobot bot) throws InterruptedException {

        encodersMove(bot, 22, 0.4, "backward");
        Thread.sleep(500);

        encodersMove(bot, 18, 0.4, "right");
        Thread.sleep(500);
    }
}