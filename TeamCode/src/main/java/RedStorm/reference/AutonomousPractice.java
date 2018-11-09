package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Katelin Zichittella on 6/7/2017.
 */

@Autonomous
@Disabled

public class AutonomousPractice extends AutonomousHeaderPractice {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        stopAtDistance(0.2, 0.3);

        moveForwardWhenRed(0.3);
    }
}