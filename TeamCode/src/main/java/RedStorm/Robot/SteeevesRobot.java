package RedStorm.Robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class SteeevesRobot {

    // Hardware map
    public HardwareMap hwMap =  null;

    // Robot Motors, sensors
    public DcMotor motorBackLeft = null;
    public DcMotor motorBackRight = null;
    public BNO055IMU imu;

    // IMU variables used to set how the IMU behaves and where it will
    //   store information
    public BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    public Orientation angles;
    public double integratedZAxis;
    public double iza_lastHeading = 0.0;
    public double iza_deltaHeading;
    public float iza_newHeading;
    public Orientation iza_angles;


    /** Constructor for Robot class, current does nothing but is needed since every class needs a constructor
     *
     */
    public SteeevesRobot (){

    }

    /**
     * This method will initialize the robot
     *
     * @param ahwMap hardware map for the robot
     */
    public void initialize(HardwareMap ahwMap) {

        // Save reference to hardware map
        hwMap = ahwMap;

        // Define and initialize motors, the names here are what appears
        // in the configuration file on the Robot Controller/Driver Station
        motorBackLeft = hwMap.get(DcMotor.class, "left_Back");
        motorBackRight = hwMap.get(DcMotor.class, "right_Back");

        // Defines the directions the motors will spin, typically motors that
        // are mounted on the left side are mounted in such a way as that the
        // the motor will spin backwards, so we set the default direction to
        // to be REVERSE so that the left motor will spin forwards with respect
        // to the ROBOT.
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);

        // Set all motors to run without encoders.
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu = hwMap.get(BNO055IMU.class, "imu");
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.mode = BNO055IMU.SensorMode.IMU;
        imu.initialize(parameters);
    }

    /**
     * This method will set the power for the drive motors
     *
     * @param leftBackMotorPower power setting for the left back motor
     * @param rightBackMotorPower power setting for the right back motor
     */
    public void setDriveMotorPower(double leftBackMotorPower, double rightBackMotorPower){

        /* Set the motor powers */
        motorBackLeft.setPower(leftBackMotorPower);
        motorBackRight.setPower(rightBackMotorPower);
    }

}
