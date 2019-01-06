package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import RedStorm.Robot.Robot;

@Autonomous(name="SamplingAutonomousFacingCrater", group="FacingCrater")


public class  SamplingAutonomousFacingCrater extends LinearOpMode {

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
    public void runOpMode() {
        robot.initialize(hardwareMap);             // Initialize the robot
        robot.resetEncoders();                     // Reset the encoder counts
        robot.runWithEncoders();                   // Tell the motors to run with encoders

        telemetry.addData("Status ", "Initialized");
        telemetry.update();

        waitForStart();


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

        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            while (opModeIsActive()) {

                // make all of this a method called detectGoldMineralPosition
                goldLocation = detectGoldMineralPosition();


                // Anna - add your code to do figure out your gold location

                switch (goldLocation) {
                    case 1: {
                        //turn left to face mineral, knock it, turn right to face crater
                        robot.getHeading();
                        robot.setDriveMotorPower(-0.5, 0.5);
                        while (opModeIsActive() &&
                                robot.getHeading() < 5) {
                        }
                        robot.setDriveMotorPower(0, 0);

                        robot.setDriveMotorPower(.5, .5);
                        robot.getDriveEncoderCount();
                        while (opModeIsActive() &&
                                robot.getDriveEncoderCount() < 30) {
                        }
                        robot.setDriveMotorPower(0, 0);
                        robot.getHeading();
                        robot.setDriveMotorPower(0.5, -0.5);
                        while (opModeIsActive() &&
                                robot.getHeading() > 0) {
                        }
                        robot.setDriveMotorPower(0, 0);
                        break;
                    }

                    case 2: {
                        //go straight ahead, knock mineral
                        robot.getDriveEncoderCount();
                        robot.setDriveMotorPower(.5, .5);
                        while (opModeIsActive() &&
                                robot.getDriveEncoderCount() < 30) {
                        }
                        robot.setDriveMotorPower(0, 0);
                        break;
                    }

                    case 3: {
                        //turn right to face mineral, knock it, turn left to face crater
                        robot.getHeading();
                        robot.setDriveMotorPower(0.5, -0.5);
                        while (opModeIsActive() &&
                                robot.getHeading() < 5) {
                        }
                        robot.setDriveMotorPower(0, 0);
                        robot.setDriveMotorPower(.5, .5);
                        robot.getDriveEncoderCount();
                        while (opModeIsActive() &&
                                robot.getDriveEncoderCount() < 30) {
                        }
                        robot.setDriveMotorPower(0, 0);
                        robot.getHeading();
                        robot.setDriveMotorPower(-0.5, 0.5);
                        while (opModeIsActive() &&
                                robot.getHeading() < 5) {
                        }
                        robot.setDriveMotorPower(0, 0);
                        break;
                    }
                }
                robot.setDriveMotorPower(0, 0);

            } //while (opModeIsActive())
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
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
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
                            telemetry.addData("Gold Mineral Position", "Left");
                            goldLocation = 1;
                        }
                        // Is the gold mineral at the rightmost position
                        else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            goldLocation = 3;
                        }
                        // The gold mineral at the center position
                        else {
                            telemetry.addData("Gold Mineral Position", "Center");
                            goldLocation = 2;
                        }
                        tfod.shutdown();
                    }
                } //if (updatedRecognitions.size() == 3)
                telemetry.update();
            } //if (updatedRecognitions != null)
        } //if (tfod != null)
        return goldLocation;
    }
}
