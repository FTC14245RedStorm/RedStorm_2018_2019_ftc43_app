package RedStorm.reference; // These lines import necessary software for this program.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Katelin Zichittella on 9/24/2016.
 */

public abstract class SampleAutonomousHeader extends LinearOpMode { // This line establishes this
    // program as a public abstract class that extends the header file "LinearOpMode". This makes
    // it a header file in itself that the real FacingCrater op modes will extend. It is considered
    // abstract because it simply contains the framework for the FacingCrater op modes (creating void
    // methods and such) and does not yet translate to actual movement on the robot.

    DcMotor motorLeft, motorRight; // This line establishes the names of the motors we will be
                                   // using.

    public void initialize() { // This public void will go at the start of each FacingCrater op mode
        // and will serve as the initialization routine the robot undergoes.

        motorLeft = hardwareMap.dcMotor.get("motorLeft");   // These lines establish a link between
        motorRight = hardwareMap.dcMotor.get("motorRight"); // the code and the hardware for the
                                                            // motors. The names in quotations are
                                                            // the names of the motors we set on
                                                            // the phone.

        motorRight.setDirection(DcMotor.Direction.REVERSE); // This line reverses the right motor
                                                            // in order to negate the fact that the
                                                            // motors are placed on the robot to
                                                            // mirror each other.
    }

    public void move(double left, double right) { // This public void is called in the FacingCrater
                                                  // op modes whenever the motors on the robot need
                                                  // to be turned on. It has two parameters: the
                                                  // speed of the left motor and the speed of the
                                                  // right motor. These can be changed with each
                                                  // implementation of the public void. Whatever is
                                                  // inputted into each parameter is then
                                                  // substituted into its corresponding spots in
                                                  // the public void.

        motorLeft.setPower(left); // This line sets the power of the left motor to the desired
                                  // power.
        motorRight.setPower(right); // This line sets the power of the right motor to the desired
                                    // power.
    }
}