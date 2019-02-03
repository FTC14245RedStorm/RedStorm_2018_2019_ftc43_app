package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;
import com.qualcomm.robotcore.hardware.DigitalChannelImpl;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.hardware.DragonboardGPIOPin;

import java.util.List;

import RedStorm.Robot.Robot;

@Autonomous(name="SamplingAu`tonomousFacingDepot", group="FacingCrater")


public class  SamplingAutonomousFacingDepot extends LinearOpMode {

    public Robot robot = new Robot();
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "ASP5Bz//////AAABmQpXQrmR/EGhouIfX6bxE+QBXEzQhHkUF4EXvYLMlpAalmmuiQbsJJH9E1kF88zP9D4TH9IovzC83G9uVx1Td+/ei7Yy0ej+PZt0w7QgnMHUGBy140Ma9IPr+fsgDTDcUjtbpqp7GvBTBa3cGlZ1t/NPjzwzhL74HWO4ibMucuEIaC0qkQ7pBQzLEx4ZhDteI98SYo096qruhWPtA1mY0PwpZ3CkceWVQfMsuWfsDc4ZtxsoezvYTrXoasoWTAv0r/S/TJ26E/SUo200knbh4qnz510paJYlMKRJFx7GOXIm543osLJj3og4XYFdircW9sI386gpTsRrSqeD2tvDMAHjLDzag6ggyPrnI/eQOCeY";

    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;
    private int goldLocation = -1;

    private int goldMineralX = -1;
    private int silverMineral1X = -1;
    private int silverMineral2X = -1;
    private double deployStartTime = 0;
    double currentHeading = 0;
    double finalHeading = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status ", "Initialized");
        telemetry.update();

        // waitForStart();


        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        //

        robot.setLiftMotorPower(1.0);


        double deployStartTime = System.currentTimeMillis();

        while (opModeIsActive() &&
                robot.sensorTouch.isPressed() == false &&
                System.currentTimeMillis() - deployStartTime < 5000) {

        }

        robot.setLiftMotorPower(0);

        telemetry.addData("Status ", "Deployed");
        telemetry.update();


        //Thread.sleep(1000);
        robot.setTeamMarkerArm(.1);

        if (opModeIsActive()) {
            telemetry.update();
            /** Activate Tensor Flow Object Detection. */

            telemetry.update();

            //Thread.sleep(1500);
            goldLocation = detectGoldMineralPosition();

            Thread.sleep(1000);

            // Tell update to clear the display and then update to clear the display
            telemetry.setAutoClear(true);
            telemetry.update();

            telemetry.addLine().addData("goldLocation", String.valueOf(goldLocation));

            // Raise the arm back up
            robot.setTeamMarkerArm(-.5);
            //robot.initializeIMU();

            robot.setDriveMotorPower(.2, -.2);

            currentHeading = robot.getHeading();

            finalHeading = currentHeading + 20;

            while (opModeIsActive() &&
                    robot.getIntegratedZAxis() < finalHeading) {
                telemetry.addData("IntegratedZAxis: ", robot.getIntegratedZAxis());
                telemetry.update();
            }
            telemetry.addData("Status ", "Turned");
            telemetry.update();

            robot.resetEncoders();
            robot.runWithEncoders();
            robot.setDriveMotorPower(0, 0);

            double distanceToTravel = robot.calculateEncoderCounts(3);
            telemetry.addData("Status ", "DistTravCalc");
            telemetry.addData("distance to travel: ", "%5.2f", distanceToTravel);
            telemetry.addData("enc count: ", "%5.2f", robot.getDriveEncoderCount());
            telemetry.update();

            robot.setDriveMotorPower(-.2, -.2);
            while (opModeIsActive() &&
                    robot.getDriveEncoderCount() < distanceToTravel) ;
            {
                telemetry.addData("Status ", "MovingAwayFromLander");
                telemetry.addData("Enc Count:", "%5.2f", robot.getDriveEncoderCount());
                telemetry.update();


            }
            telemetry.update();


            robot.setDriveMotorPower(0, 0);

            //robot.initializeIMU();
            //robot.setTeamMarkerArm(-.5);

            robot.setDriveMotorPower(-.2, .2);

            currentHeading = robot.getHeading();

            finalHeading = currentHeading - 23;

            while (opModeIsActive() &&
                    robot.getIntegratedZAxis() < finalHeading) {
                telemetry.addData("IntegratedZAxis: ", robot.getIntegratedZAxis());
                telemetry.update();

            }
            telemetry.addData("Status ", "Turned");
            telemetry.update();

            robot.resetEncoders();
            robot.runWithEncoders();
            robot.setDriveMotorPower(0, 0);


            // Initialize the IMU
            //robot.initializeIMU();

            telemetry.addLine().addData("IMU initialize", " complete");

            switch (goldLocation) {
                case 1: {

                    telemetry.addLine().addData("Case 1", String.valueOf(goldLocation));
                    //turn left to face mineral, knock it, turn right to face crater
                    // robot.setTeamMarkerArm(.1);
                    //robot.resetEncoders();
                    //robot.runWithEncoders();
                    //double encoderDistanceToTravel = robot.calculateEncoderCounts(5);
                   // telemetry.addLine().addData("encoderDistance", String.valueOf(encoderDistanceToTravel));
                    //robot.setDriveMotorPower(-.5, -.5);
                    //telemetry.update();

                   // while (opModeIsActive() &&
                          //  robot.getDriveEncoderCount() < encoderDistanceToTravel) {
                       // telemetry.addLine().addData("currentencoder", robot.getDriveEncoderCount());
                      //  telemetry.addLine().addData("encoderDistance", String.valueOf(encoderDistanceToTravel));
                   // telemetry.update();
                    //}

                    //robot.initializeIMU();


                    double crntHeading = robot.getIntegratedZAxis();
                    telemetry.addLine().addData("currentHeading", String.valueOf(crntHeading));
                    telemetry.addLine().addData("Start", "turning");
                    telemetry.update();
                    //Thread.sleep(250);

                    robot.setDriveMotorPower(-0.5, 0.5);
                    //telemetry.update();
                    while (opModeIsActive() &&
                            robot.getIntegratedZAxis() < 25.0) {
                        telemetry.addLine().addData("currentHeading", robot.getIntegratedZAxis());
                        telemetry.update();

                    }
                    robot.setDriveMotorPower(0,0);
                    //Thread.sleep(250);
                    double encoderDistanceToTravelB = robot.calculateEncoderCounts(40);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(-.5,-.5);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelB) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    double encoderDistanceToTravelC = robot.calculateEncoderCounts(10);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(.5,.5);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelC) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    //robot.setDriveMotorPower(0, 0);
                    //Thread.sleep(250);
                    telemetry.update();
                    //robot.initializeIMU();

                    robot.setDriveMotorPower(0.5, -0.5);
                    //double crntHeadingB = robot.getHeading();
                    while (opModeIsActive() &&
                            robot.getIntegratedZAxis() < 42.0) {
                        telemetry.addLine().addData("currentHeading", robot.getIntegratedZAxis());
                        telemetry.update();
                    }
                    //Thread.sleep(250);
                    telemetry.addData("Status ", "Straight");
                    telemetry.update();
                    robot.setDriveMotorPower(0,0);
                    //Thread.sleep(250);

                    double encoderDistanceToTravelD = robot.calculateEncoderCounts(35);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(-.5,-.5);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelD) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    robot.setTeamMarkerArm(.1);
                    Thread.sleep(250);
                    robot.setTeamMarkerGrip(.1);
                    Thread.sleep(750);
                    robot.setTeamMarkerArm(-.5);
                    Thread.sleep(250);

                    //now we go to back up into crater

                    robot.resetEncoders();
                    double encoderDistanceToTravelE = robot.calculateEncoderCounts(30);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(.8,.8);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelE) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0,0);

                    //now turn so we don't bump into the lander

                    //Thread.sleep(250);
                    telemetry.update();
                    //robot.initializeIMU();

                    robot.setDriveMotorPower(0.7, -0.7);
                    //double crntHeadingB = robot.getHeading();
                    while (opModeIsActive() &&
                            robot.getIntegratedZAxis() < 10.0) {
                        telemetry.addLine().addData("currentHeading", robot.getIntegratedZAxis());
                        telemetry.update();
                    }
                    //Thread.sleep(250);
                    telemetry.addData("Status ", "Straight");
                    telemetry.update();
                    robot.setDriveMotorPower(0,0);

                    //now back up into the crater

                    //robot.resetEncoders();
                    //robot.runWithEncoders();
                    //robot.initializeIMU();
                    double encoderDistanceToTravelF = robot.calculateEncoderCounts(47);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(.8,.8);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelF) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0,0);


                    break;
                }

                case 2: {
                    //go straight ahead, knock mineral
                    // robot.setTeamMarkerArm(.1);
                    telemetry.addLine().addData("Case 2", String.valueOf(goldLocation));

                    double encoderDistanceToTravel = robot.calculateEncoderCounts(50);
                    telemetry.addLine().addData("encoderDistance", String.valueOf(encoderDistanceToTravel));

                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(-.5, -.5);
                    telemetry.update();

                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravel) {
                        telemetry.addLine().addData("currentencoder", robot.getDriveEncoderCount());
                        telemetry.addLine().addData("encoderDistance", String.valueOf(encoderDistanceToTravel));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    //robot.setDriveMotorPower(.4,.4);
                    robot.setTeamMarkerArm(.1);
                    Thread.sleep(500);
                    robot.setTeamMarkerGrip(.1);
                    robot.setTeamMarkerArm(-.5);
                    telemetry.update();
                    //robot.initializeIMU();

                    robot.setDriveMotorPower(0.7, -0.7);
                    //double crntHeadingB = robot.getHeading();
                    while (opModeIsActive() &&
                            robot.getIntegratedZAxis() < 27.0) {
                        telemetry.addLine().addData("currentHeading", robot.getIntegratedZAxis());
                        telemetry.update();
                    }
                    //Thread.sleep(250);
                    telemetry.addData("Status ", "Straight");
                    telemetry.update();
                    robot.setDriveMotorPower(0,0);
                    double encoderDistanceToTravelF = robot.calculateEncoderCounts(37);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(.8,.8);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelF) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0,0);

                    telemetry.update();
                    //robot.initializeIMU();

                    /*
                    robot.setDriveMotorPower(-0.7, 0.7);
                    //double crntHeadingB = robot.getHeading();
                    while (opModeIsActive() &&
                            robot.getHeading() < 5.0) {
                        telemetry.addLine().addData("currentHeading", robot.getHeading());
                        telemetry.update();
                    }
                    */
                    //Thread.sleep(250);
                    telemetry.addData("Status ", "Straight");
                    telemetry.update();
                    robot.setDriveMotorPower(0,0);
                    double encoderDistanceToTravelG = robot.calculateEncoderCounts(36);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(.8,.8);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelG) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0,0);

                }

                break;
                case 3: {
                    telemetry.addLine().addData("Case 1", String.valueOf(goldLocation));
                    //turn left to face mineral, knock it, turn right to face crater
                    // robot.setTeamMarkerArm(.1);
                    //robot.resetEncoders();
                    //robot.runWithEncoders();
                    //double encoderDistanceToTravel = robot.calculateEncoderCounts(5);
                    // telemetry.addLine().addData("encoderDistance", String.valueOf(encoderDistanceToTravel));
                    //robot.setDriveMotorPower(-.5, -.5);
                    //telemetry.update();

                    // while (opModeIsActive() &&
                    //  robot.getDriveEncoderCount() < encoderDistanceToTravel) {
                    // telemetry.addLine().addData("currentencoder", robot.getDriveEncoderCount());
                    //  telemetry.addLine().addData("encoderDistance", String.valueOf(encoderDistanceToTravel));
                    // telemetry.update();
                    //}

                    //robot.initializeIMU();


                    double crntHeading = robot.getHeading();
                    telemetry.addLine().addData("currentHeading", String.valueOf(crntHeading));
                    telemetry.addLine().addData("Start", "turning");
                    telemetry.update();
                    //Thread.sleep(250);

                    robot.setDriveMotorPower(0.5, -0.5);
                    telemetry.update();
                    while (opModeIsActive() &&
                            robot.getIntegratedZAxis() < 20.0) {
                        telemetry.addLine().addData("currentHeading", robot.getIntegratedZAxis());
                        telemetry.update();

                    }
                    robot.setDriveMotorPower(0,0);
                    //Thread.sleep(250);
                    double encoderDistanceToTravelB = robot.calculateEncoderCounts(40);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(-.5,-.5);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelB) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    double encoderDistanceToTravelC = robot.calculateEncoderCounts(10);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(.5,.5);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelC) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    //robot.setDriveMotorPower(0, 0);
                    //Thread.sleep(250);
                    telemetry.update();
                    //robot.initializeIMU();

                    robot.setDriveMotorPower(-0.5, 0.5);
                    //double crntHeadingB = robot.getHeading();
                    while (opModeIsActive() &&
                            robot.getIntegratedZAxis() < 37.0) {
                        telemetry.addLine().addData("currentHeading", robot.getIntegratedZAxis());
                        telemetry.update();
                    }
                    //Thread.sleep(250);
                    telemetry.addData("Status ", "Straight");
                    telemetry.update();
                    robot.setDriveMotorPower(0,0);
                    //Thread.sleep(250);

                    double encoderDistanceToTravelD = robot.calculateEncoderCounts(35);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(-.8,-.8);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelD) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    robot.setTeamMarkerArm(.1);
                    Thread.sleep(500);
                    robot.setTeamMarkerGrip(.5);
                    Thread.sleep(250);
                    robot.setTeamMarkerArm(-.5);
                    /*

                    //Thread.sleep(250);
                    telemetry.addData("Status ", "Straight");
                    telemetry.update();
                    double encoderDistanceToTravelR = robot.calculateEncoderCounts(7);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(.8,.8);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelR) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    telemetry.update();
                    robot.initializeIMU();
                    robot.setDriveMotorPower(-0.7, 0.7);
                    while (opModeIsActive() &&
                            robot.getHeading() < 40.0) {
                        telemetry.addLine().addData("currentHeading", robot.getHeading());
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0,0);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    double encoderDistanceToTravelH = robot.calculateEncoderCounts(30);
                    robot.setDriveMotorPower(-.8,-.8);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelH) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    */

                    break;
                }
            }




        }// if (opModeIsActive())

        // shut down so it doesn't change the values anymore
        if (tfod != null) {
            tfod.shutdown();
        }

    } //runOpMode


    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    /**
     * use TensorFlow and Vuforia to detect the gold mineral
     * @return goldMineralPosition
     */


    private int detectGoldMineralPosition() {

        List<Recognition> updatedRecognitions = null;
        int numberOfRecognitions = 0;

        // Temporary set up telemtry to not clear the screen

        // Tell update to clear the display and then update to clear the display
        telemetry.setAutoClear(true);
        telemetry.update();

        // Tell update to not clear the display when update() is called
        telemetry.setAutoClear(false);

        if (tfod != null) {
            //so the red tape isn't identified as a mineral turn on the flash
            CameraDevice.getInstance().setFlashTorchMode(true);
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.

            // Loop through until we fine 3 detected objects.  The first time through, updateRecognitions
            // will be NULL, meaning there is nothing in it and the numberOfRecognitions will be 0.  The
            // code will loop until we find 3 detected items....then onto the normal processing

            while (opModeIsActive() && (updatedRecognitions == null || numberOfRecognitions < 3)) {

                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.

                    updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions == null) {
                        numberOfRecognitions = 0;
                    } else {
                        numberOfRecognitions = updatedRecognitions.size();
                    }
                }
            }

            if (updatedRecognitions != null) {
                telemetry.addLine().addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() >= 1) {
                    //was three

                    // Find the position of the minerals

                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }

                    telemetry.addLine().addData("goldMineral", goldMineralX);
                    telemetry.addLine().addData("silverMineral", silverMineral1X);
                    telemetry.addLine().addData("silverMineral2X", silverMineral2X);



                    if (goldMineralX == -1) {                 //gold mineral not found
                        if (updatedRecognitions.size() == 2) {  //two silvers found
                            if (silverMineral1X < 250 && silverMineral2X < 750) {  //silver is left and center
                                goldLocation = 3; //gold must be on the right
                            } else if ((silverMineral1X < 900 && silverMineral1X > 250) &&  //silver is center
                                    silverMineral2X > 900) {  //silver is right
                                goldLocation = 1;  //gold must be left
                            } else {
                                goldLocation = 2;  //gold must be in the center
                            }
                        }
                        else {    // didn't find enough silver minerals
                            goldLocation = 2;
                        }
                    }
                    else {  //we found the gold mineral
                        // Did we find all of the minerals
                        if (goldMineralX < 250) {
                            goldLocation = 1;
                        }
                        else if (goldMineralX > 900) {
                            goldLocation = 3;
                        }
                        else {
                            goldLocation = 2;
                        }

                    }


                    /*if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {

                        // Is the gold mineral at the leftmost position
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addLine().addData("Gold Mineral Position", "Left");
                            goldLocation = 1;
                        }
                        // Is the gold mineral at the rightmost position
                        else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addLine().addData("Gold Mineral Position", "Right");
                            goldLocation = 3;
                        }
                        // The gold mineral at the center position
                        else {
                            telemetry.addLine().addData("Gold Mineral Position", "Center");
                            goldLocation = 2;
                        }
                        //tfod.shutdown();
                    } */
                } //if (updatedRecognitions.size() == 3)
                telemetry.update();
            } //if (updatedRecognitions != null)
        } //if (tfod != null)

        telemetry.addLine().addData("goldLocation",String.valueOf(goldLocation));
        return goldLocation;
    }
}
