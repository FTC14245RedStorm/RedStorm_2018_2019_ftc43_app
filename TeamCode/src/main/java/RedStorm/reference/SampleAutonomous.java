package RedStorm.reference; // These lines import necessary software for this op mode.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Katelin Zichittella on 9/24/2016.
 */

@Autonomous // This line establishes this op mode as an FacingCrater op mode and allows for it to be
// displayed in the drop down list on the Driver Station phone to be chosen to run.
@Disabled // This line temporarily takes this op mode off of the drop down list until it is
// commented out.

public class SampleAutonomous extends SampleAutonomousHeader { // This line establishes the name of
    // the op mode and extends the header file "SampleAutonomousHeader", which in turn extends the
    // header file "LinearOpMode", in order to access all of the information and public voids in
    // "SampleAutonomousHeader" and to create an FacingCrater op mode.

    @Override
    public void runOpMode() throws InterruptedException { // This section of the code has both the
        // initialization routine the robot undergoes and the main FacingCrater program that runs
        // in a linear fashion.

        initialize(); // This line implements a public void created in the header file that
        // initializes all of the necessary parts of the robot.

        waitForStart(); // Everything before this line is the initialization routine the robot
        // undergoes, while everything after it is the main FacingCrater program.

        move(1.0, 1.0); // This line implements a public void created in the header file with two
        // parameters (the numbers in the parentheses) that we get to choose here. Here we tell the
        // robot to turn on both of its motors to a speed of 1.0 (full power) in the positive
        // direction, or to start moving forward in other words.
        Thread.sleep(1000); // This line pauses the program for 1000 milliseconds, or one second.
        // Because the last command was to turn on both of the motors to a speed of 1.0 in the
        // positive direction, the motors remain on for this length of time and the robot moves a
        // distance forward.
        move(0.0, 0.0); // This line turns off the motors once the robot has moved forward for the
        // desired length of time.
        Thread.sleep(500); // The robot rests for a half of a second.

        move(-1.0, -1.0); // Same thing, but the robot moves backward.
        Thread.sleep(1000);
        move(0.0, 0.0);
        Thread.sleep(500);

        move(1.0, -1.0); // Same thing, but the robot turns right.
        Thread.sleep(1000);
        move(0.0, 0.0);
        Thread.sleep(500);

        move(-1.0, 1.0); // Same thing, but the robot turns left.
        Thread.sleep(1000);
        move(0.0, 0.0);
        Thread.sleep(500);
    }
}