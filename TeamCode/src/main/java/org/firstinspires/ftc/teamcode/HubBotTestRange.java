package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "HubbotTestRange", group = "Hubbot")
@Disabled

public class HubBotTestRange extends LinearOpMode {

  public ModernRoboticsI2cRangeSensor mrRange  = null;


  public double servoPos = 0.0;

  @Override
  public void runOpMode() {

    // get a reference to our Light Sensor object.
   mrRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "mrRange");

    // wait for the start button to be pressed.
    waitForStart();

    // while the op mode is active, loop and read the light levels.
    // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
    while (opModeIsActive()) {


      // send the info back to driver station using telemetry function.
      telemetry.addData("raw ultrasonic", mrRange.rawUltrasonic());
      telemetry.addData("raw optical", mrRange.rawOptical());
     // telemetry.addData("cm optical", "%4.2f cm", mrRange.cmOptical());
      telemetry.addData("in", "%4.2f in", getMrDistance(DistanceUnit.INCH));
      telemetry.update();

    }
  }

    /**
     * this is a copy of the getDistance code from ModerRoboticsI2cRangeSensor.java...i stole it to
     * debug our problem.....comments are mine and mine alone (as I show my best evil grin).
     * @param unit - units that the data should be returned as
     * @return - the distance in the passed in units
     */
    public double getMrDistance(DistanceUnit unit)
    {
        int rawOptical = mrRange.rawOptical(); // the very low readings are quite noisy

        double cm;
        if (rawOptical >= 3.0)                 // raw optical is funny when over its limit, it just returns 0...not good
        {
            cm = mrRange.rawOptical();
            telemetry.addLine("rawOptical used");
        }
        else
        {
            cm = mrRange.rawUltrasonic();
            telemetry.addLine("rawUltrasonic used");
            if (cm == 255)                     // so 255 is an error - really...i think not
            {
                return DistanceSensor.distanceOutOfRange;
            }
        }
        return unit.fromUnit(DistanceUnit.CM, cm);
    }

}
