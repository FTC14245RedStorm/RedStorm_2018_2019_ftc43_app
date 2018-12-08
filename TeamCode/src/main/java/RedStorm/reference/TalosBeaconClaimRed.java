package RedStorm.reference; // These lines import necessary software for this op mode.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Katelin Zichittella on 10/31/2016.
 */

@Autonomous // This line establishes this op mode as an FacingCrater op mode and allows for it to be
// displayed in the drop down list on the Driver Station phone to be chosen to run.
@Disabled

public class TalosBeaconClaimRed extends TalosAutonomousHeader { // This line establishes the name
    // of the op mode and extends the header file "TalosAutonomousHeader", which in turn extends the
    // header file "LinearOpMode", in order to access all of the information and public voids in
    // "TalosAutonomousHeader" and to create an FacingCrater op mode.

    @Override
    public void runOpMode() throws InterruptedException { // This section of the code has both the
        // initialization routine the robot undergoes and the main FacingCrater program that runs
        // in a linear fashion.

        initialize(); // This line implements a public void created in the header file that
        // initializes all of the necessary parts of the robot.

        calibrateGyro(); // This line implements a public void created in the header file that
        // calibrates the gyro sensor before the program starts.

        waitForStart(); // Everything before this line is the initialization routine the robot
        // undergoes, while everything after it is the main FacingCrater program.

        // EVERYTHING ELSE IN THIS PROGRAM IS SIMPLY AN IMPLEMENTATION OF A METHOD OUTLINED IN THE
        // HEADER FILE.

        shoot();
        Thread.sleep(200);

        servoGate.setPosition(0.3);
        Thread.sleep(100);

        shoot2();
        Thread.sleep(600);

        servoGate.setPosition(0.0);
        Thread.sleep(100);

        shoot3();
        Thread.sleep(200);

        encodersForward(22, 0.6);
        Thread.sleep(200);

        gyroTurnLeft(70, 0.5);
        Thread.sleep(200);

        encodersForward(22, 0.6);
        Thread.sleep(200);

        gyroTurnRight(48, 0.5);
        Thread.sleep(200);

        encodersForward(5, 0.6);
        Thread.sleep(200);

        moveUntilWhiteLineStraight(0.24);
        Thread.sleep(200);

        encodersForward(1, 0.5);
        Thread.sleep(200);

        gyroTurnLeft(48, 0.5);
        Thread.sleep(200);

        lineFollowerTwoSensors(6);
        Thread.sleep(200);

        encodersBackward(2, 0.4);
        Thread.sleep(100);

        colorSensorFrontCache = colorSensorFrontReader.read(0x04, 1);
        telemetry.addData("Front", colorSensorFrontCache[0] & 0xFF);
        telemetry.update();

        if ((colorSensorFrontCache[0] & 0xFF) <= 6) { // blue

            Thread.sleep(5000);

            encodersBackward(2, 0.5);
            Thread.sleep(200);

            encodersForward(4, 0.4);
            Thread.sleep(500);

            encodersBackward(18, 0.6);
            Thread.sleep(200);

            gyroTurnLeft(70, 0.5);
            Thread.sleep(200);

            encodersForward(26, 0.4);
            Thread.sleep(200);

            gyroTurnRight(36, 0.5);
            Thread.sleep(200);

            encodersForward(8, 0.4);
            Thread.sleep(20000);
        }
        else {

            Thread.sleep(100);
        }

        encodersBackward(14, 0.5);
        Thread.sleep(200);

        gyroTurnRight(68, 0.5);
        Thread.sleep(200);

        encodersForward(26, 0.75);
        Thread.sleep(200);

        gyroTurnLeft(5, 0.5);
        Thread.sleep(200);

        moveUntilWhiteLineStraight(0.28);
        Thread.sleep(200);

        encodersForward(1, 0.5);
        Thread.sleep(200);

        gyroTurnLeft(46, 0.5);
        Thread.sleep(200);

        lineFollowerTwoSensors(6);
        Thread.sleep(200);

        encodersBackward(2, 0.4);
        Thread.sleep(100);

        colorSensorFrontCache = colorSensorFrontReader.read(0x04, 1);
        telemetry.addData("Front", colorSensorFrontCache[0] & 0xFF);
        telemetry.update();

        if ((colorSensorFrontCache[0] & 0xFF) <= 6) { // blue

            Thread.sleep(5000);

            encodersForward(4, 0.4);
            Thread.sleep(500);
        }
        else {

            Thread.sleep(100);
        }

        encodersBackward(5, 0.6);
        Thread.sleep(200);
    }
}