package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import RedStorm.Robot.Robot;

@Autonomous(name="SamplingAutonomousFacingDepot", group="FacingCrater")


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

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        // make all of this a method called detectGoldMineralPosition
        robot.setTeamMarkerArm(.1);

        if (opModeIsActive()) {
            telemetry.update();
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            telemetry.update();

            Thread.sleep(2500);
            goldLocation = detectGoldMineralPosition();

            Thread.sleep(1000);

            // Tell update to clear the display and then update to clear the display
            telemetry.setAutoClear(true);
            telemetry.update();

            telemetry.addLine().addData("goldLocation", String.valueOf(goldLocation));

            // Raise the arm back up
            robot.setTeamMarkerArm(-.5);

            // Initialize the IMU
            robot.initializeIMU();

            telemetry.addLine().addData("IMU initialize", " complete");

            switch (goldLocation) {
                case 1: {

                    telemetry.addLine().addData("Case 1", String.valueOf(goldLocation));
                    //turn left to face mineral, knock it, turn right to face crater
                    // robot.setTeamMarkerArm(.1);
                    double encoderDistanceToTravel = robot.calculateEncoderCounts(5);
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


                    double crntHeading = robot.getHeading();
                    telemetry.addLine().addData("currentHeading", String.valueOf(crntHeading));
                    telemetry.addLine().addData("Start", "turning");
                    telemetry.update();
                    Thread.sleep(250);

                    robot.setDriveMotorPower(-0.5, 0.5);
                    telemetry.update();
                    while (opModeIsActive() &&
                            robot.getHeading() < 30.0) {
                        telemetry.addLine().addData("currentHeading", robot.getHeading());
                        telemetry.update();

                    }
                    robot.setDriveMotorPower(0,0);
                    Thread.sleep(250);
                    double encoderDistanceToTravelB = robot.calculateEncoderCounts(32);
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
                    Thread.sleep(250);
                    telemetry.update();
                    robot.initializeIMU();

                    robot.setDriveMotorPower(0.5, -0.5);
                    //double crntHeadingB = robot.getHeading();
                    while (opModeIsActive() &&
                            robot.getHeading() < 30.0) {
                        telemetry.addLine().addData("currentHeading", robot.getHeading());
                        telemetry.update();
                    }
                    Thread.sleep(250);
                    telemetry.addData("Status ", "Straight");
                    telemetry.update();
                    robot.setDriveMotorPower(0,0);
                    Thread.sleep(250);

                    double encoderDistanceToTravelD = robot.calculateEncoderCounts(30);
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
                    robot.setTeamMarkerGrip(.5);
                    //robot.setTeamMarkerArm(-.5);


                    break;
                }

                case 2: {
                    //go straight ahead, knock mineral
                    // robot.setTeamMarkerArm(.1);
                    telemetry.addLine().addData("Case 2", String.valueOf(goldLocation));

                    double encoderDistanceToTravel = robot.calculateEncoderCounts(60);
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
                    //robot.setTeamMarkerGrip(.5);
                   // robot.setTeamMarkerArm(-.5);
                    break;
                }

                case 3: {
                    telemetry.addLine().addData("Case 1", String.valueOf(goldLocation));
                    //turn left to face mineral, knock it, turn right to face crater
                    // robot.setTeamMarkerArm(.1);
                    double encoderDistanceToTravel = robot.calculateEncoderCounts(5);
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


                    double crntHeading = robot.getHeading();
                    telemetry.addLine().addData("currentHeading", String.valueOf(crntHeading));
                    telemetry.addLine().addData("Start", "turning");
                    telemetry.update();
                    Thread.sleep(250);

                    robot.setDriveMotorPower(0.5, -0.5);
                    telemetry.update();
                    while (opModeIsActive() &&
                            robot.getHeading() < 30.0) {
                        telemetry.addLine().addData("currentHeading", robot.getHeading());
                        telemetry.update();

                    }
                    robot.setDriveMotorPower(0,0);
                    Thread.sleep(250);
                    double encoderDistanceToTravelB = robot.calculateEncoderCounts(32);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(-.5,-.5);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelB) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    Thread.sleep(250);
                    telemetry.update();

                    robot.setDriveMotorPower(-0.5, 0.5);
                    Thread.sleep(250);
                    robot.initializeIMU();
                    Thread.sleep(250);
                    robot.getHeading();

                    robot.setDriveMotorPower(0.3, -0.3);
                    //telemetry.update();
                    while (opModeIsActive() &&
                            robot.getHeading() < 0.0) {
                        telemetry.addLine().addData("currentHeading", robot.getHeading());
                        telemetry.update();

                    }
                    robot.setDriveMotorPower(0,0);
                    Thread.sleep(250);

                    double encoderDistanceToTravelC = robot.calculateEncoderCounts(30);
                    robot.resetEncoders();
                    robot.runWithEncoders();
                    robot.setDriveMotorPower(-.5,-.5);
                    while (opModeIsActive() &&
                            robot.getDriveEncoderCount() < encoderDistanceToTravelC) {
                        telemetry.addLine().addData("encoder count", String.valueOf(robot.getDriveEncoderCount()));
                        telemetry.update();
                    }
                    robot.setDriveMotorPower(0, 0);
                    robot.setTeamMarkerArm(.1);
                    robot.setTeamMarkerGrip(.5);
                    //robot.setTeamMarkerArm(-.5);


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
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

            if (updatedRecognitions != null) {
                telemetry.addLine().addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 3) {

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

                    // Did we find all of the minerals
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {

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
                    }
                } //if (updatedRecognitions.size() == 3)
                telemetry.update();
            } //if (updatedRecognitions != null)
        } //if (tfod != null)

        telemetry.addLine().addData("goldLocation",String.valueOf(goldLocation));
        return goldLocation;
    }
}
