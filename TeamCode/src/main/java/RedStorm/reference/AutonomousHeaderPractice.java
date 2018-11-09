package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Katelin Zichittella on 7/27/2017.
 */

public abstract class AutonomousHeaderPractice extends LinearOpMode {

    DcMotor motorLeft, motorRight;

    byte[] sensorColorCache;
    I2cDevice sensorColor;
    I2cDeviceSynch sensorColorReader;

    OpticalDistanceSensor sensorODS;

    public void initialize() {

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");

        sensorColor = hardwareMap.i2cDevice.get("sensorColor");
        sensorColorReader = new I2cDeviceSynchImpl(sensorColor, I2cAddr.create8bit(0x42), false);
        // I2c address of this color sensor
        sensorColorReader.write8(3, 0); // LED on (off would be 1)
        sensorColorReader.engage();

        sensorODS = hardwareMap.opticalDistanceSensor.get("sensorODS");

        motorRight.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Initialized", true);
        telemetry.update();
    }

    public void stopAtDistance(double distance, double power) { // Distance is 0 for farthest away
        // and 1 for closest
        if (opModeIsActive()) {

            motorLeft.setPower(power);
            motorRight.setPower(power);

            while (sensorODS.getLightDetected() < distance && opModeIsActive()) {

                telemetry.addData("Normal", sensorODS.getLightDetected());
                telemetry.update();
            }

            motorLeft.setPower(0.0);
            motorRight.setPower(0.0);
        }
    }

    public void moveForwardWhenRed(double power) {

        while (opModeIsActive()) {

            sensorColorCache = sensorColorReader.read(0x04, 1); // Color Number

            telemetry.addData("Color Number", sensorColorCache[0] & 0xFF);
            telemetry.update();

            if ((sensorColorCache[0] & 0xFF) > 9 && (sensorColorCache[0] & 0xFF) < 12) { // Sees Red

                motorLeft.setPower(power);
                motorRight.setPower(power);
            }
            else {

                motorLeft.setPower(0.0);
                motorRight.setPower(0.0);
            }
        }
    }
}