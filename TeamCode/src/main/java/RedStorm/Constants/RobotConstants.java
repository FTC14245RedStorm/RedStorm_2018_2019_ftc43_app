package RedStorm.Constants;

/**
 * RobotConstants contains constants (variables that will never change) that are used with the
 * Robot class.
 *
 * Note: add more constants as needed
 */

public interface RobotConstants {

    /**
     * Robot drive constants
     */
    int    DRIVE_WHEEL_DIAMETER = 4;                                   // Diameter of drive wheels
    double DRIVE_WHEEL_CIRCUMFERENCE = Math.PI * DRIVE_WHEEL_DIAMETER; // Circumference of drive wheels
    double DRIVE_GEAR_RATIO = 1.0;                                     // Robot drive gear ratio

    /**
     * Andy Mark motor constants
     */
    int ANDYMARK_NEVEREST_20_PULSES = 560;     // Encoder counts for one revolution for AM Neverest 20
    int ANDYMARK_NEVEREST_40_PULSES = 1120;    // Encoder counts for one revolution for AM Neverest 40
    int ANDYMARK_NEVEREST_60_PULSES = 1680;    // Encoder counts for one revolution for AM Neverest 60
}