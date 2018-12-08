package RedStorm.reference; // These lines import necessary software for this op mode.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Katelin Zichittella on 10/22/2016.
 */

@Autonomous // This line establishes this op mode as an FacingCrater op mode and allows for it to be
// displayed in the drop down list on the Driver Station phone to be chosen to run.
@Disabled // This line temporarily takes this op mode off of the drop down list until it is
// commented out.

public class AdvancedSampleAutonomous extends AdvancedSampleAutonomousHeader { // This line
    // establishes the name of the op mode and extends the header file
    // "AdvancedSampleAutonomousHeader", which in turn extends the header file "LinearOpMode",
    // in order to access all of the information and public voids in
    // "AdvancedSampleAutonomousHeader" and to create an FacingCrater op mode.

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

        encodersForward(12, 1.0); // This line implements a public void created in the header file
        // with two parameters (the numbers in the parentheses) that we get to choose here. Here we
        // tell the robot to go forward, using encoders to gauge its distance, twelve inches at a
        // speed of 1.0 (full speed).
        Thread.sleep(500); // This line pauses the program, and therefore the robot in this
        // instance (since the last command in the encodersForward method was to set the motors to
        // a power of zero) for 500 milliseconds, or a half of a second.

        encodersBackward(12, 1.0); // Same thing, but the robot moves backward instead.
        Thread.sleep(500);

        gyroTurnRight(90, 1.0); // This line implements a public void created in the header file
        // with two parameters (the numbers in the parentheses) that we get to choose here. Here we
        // tell the robot to turn right, using the gyro sensor to gauge how many degrees it is
        // turning, ninety degrees at a speed of 1.0.
        Thread.sleep(500); // This line pauses the program, and therefore the robot in this
        // instance (since the last command in the gyroTurnRight method was to set the motors to
        // a power of zero) for 500 milliseconds, or a half of a second.

        gyroTurnLeft(90, 1.0); // Same thing, but the robot turns left instead.
        Thread.sleep(500);
    }
}