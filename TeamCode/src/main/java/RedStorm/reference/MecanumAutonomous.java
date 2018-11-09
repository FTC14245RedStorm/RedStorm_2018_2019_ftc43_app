package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Katelin Zichittella on 2/27/2017.
 */

@Autonomous
@Disabled

public class MecanumAutonomous extends MecanumAutonomousHeader {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        strafe(30, 1.0);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);

        /*strafe(150, 1.0);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);

        strafe(210, 1.0);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);

        strafe(330, 1.0);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);

        strafe(30, 0.5);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);

        strafe(150, 0.5);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);

        strafe(210, 0.5);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);

        strafe(330, 0.5);
        Thread.sleep(1000);
        pause();
        Thread.sleep(500);*/
    }
}