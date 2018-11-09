package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Katelin Zichittella on 7/7/2017.
 */

@Autonomous
@Disabled

public class VuforiaAutonomous extends VuforiaAutonomousHeader {

    @Override
    public void runOpMode() throws InterruptedException {

        double TARGET_DISTANCE =  400.0;

        initialize();

        setupVuforia();

        waitForStart();

        visionTargets.activate();

        activateTracking();

        while (opModeIsActive()) {

            if (targetsAreVisible() && !cruiseControl(TARGET_DISTANCE)) {

                cruiseControl(TARGET_DISTANCE);
                moveRobot();
            }
            else {

                setMotorPower(0, 0);
            }
        }
    }
}