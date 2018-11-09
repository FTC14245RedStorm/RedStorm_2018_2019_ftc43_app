package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Katelin Zichittella on 7/30/2017.
 */

@TeleOp
@Disabled

public class TeleOpNew extends OpMode {

    DcMotor motorLeft, motorRight;

    byte[] sensorColorCache;
    I2cDevice sensorColor;
    I2cDeviceSynch sensorColorReader;

    OpticalDistanceSensor sensorODS;

    boolean startCount = false;
    boolean ballInPrevious = false;
    boolean ballInCurrent = false;
    boolean allBallsIn = false;
    int ballCount = 0;

    boolean ascentCurrent = false;
    boolean ascentPrevious = false;
    boolean startAscent = false;

    double leftFactor = 1;
    double rightFactor = 1;

    @Override
    public void init() {

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");

        sensorColor = hardwareMap.i2cDevice.get("sensorColor");
        sensorColorReader = new I2cDeviceSynchImpl(sensorColor, I2cAddr.create8bit(0x42), false);
        sensorColorReader.write8(3, 1);
        sensorColorReader.engage();

        sensorODS = hardwareMap.opticalDistanceSensor.get("sensorODS");

        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Initialized", true);
        telemetry.update();
    }

    @Override
    public void loop() {

        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        left = Range.clip(left, -1, 1);
        right = Range.clip(right, -1, 1);

        left = (float) scaleInput(left);
        right = (float) scaleInput(right);

        setMotorPower(left * leftFactor, right * rightFactor);

        telemetry.addData("startCount", startCount);
        telemetry.addData("ballInCurrent", ballInCurrent);
        telemetry.addData("allBallsIn", allBallsIn);
        telemetry.addData("ballCount", ballCount);
        telemetry.addData("startAscent", startAscent);
        telemetry.addData("Mode", motorRight.getMode());
        telemetry.addData("Current Position", motorRight.getCurrentPosition());
        telemetry.addData("left * leftFactor", left * leftFactor);
        telemetry.addData("right * rightFactor", right * rightFactor);
        telemetry.update();

        if (gamepad1.a) {

            startCount = true;
            allBallsIn = false;
            ballCount = 0;
        }

        if (gamepad1.b) {

            startCount = false;
        }

        if (startCount) {

            if (sensorODS.getLightDetected() > 0.4) {

                ballInCurrent = true;
            }
            else {

                ballInCurrent = false;
            }

            if (ballInCurrent && !ballInPrevious) { // Ball is beginning to enter

                ballCount++;
                ballInPrevious = ballInCurrent;
            }
            else if (!ballInCurrent && ballInPrevious) { // Ball is completely entered

                ballInPrevious = ballInCurrent;

                if (ballCount == 5) {

                    allBallsIn = true; // Last ball is completely entered
                }
            }

            if (allBallsIn) {

                leftFactor = 0;
            }
            else {

                leftFactor = 1;
            }
        }

        ascentCurrent = gamepad1.x;

        if (ascentCurrent && !ascentPrevious) {

            startAscent = true;
            motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            ascentPrevious = ascentCurrent;
        }
        else if (!ascentCurrent && ascentPrevious) {

            ascentPrevious = ascentCurrent;
        }

        if (gamepad1.y) {

            startAscent = false;
            motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (startAscent) {

            if (motorRight.getCurrentPosition() >= 5000) {

                if (right > 0) {

                    rightFactor = 0;
                }
                else {

                    rightFactor = 1;
                }
            }
        }
    }

    public void setMotorPower(double left, double right) {

        motorLeft.setPower(left);
        motorRight.setPower(right);
    }

    double scaleInput(double dVal) {

        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
