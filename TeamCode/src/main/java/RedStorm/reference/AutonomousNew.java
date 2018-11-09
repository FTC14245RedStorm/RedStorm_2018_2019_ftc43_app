package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/**
 * Created by Katelin Zichittella on 6/7/2017.
 */

@Autonomous
@Disabled

public class AutonomousNew extends AutonomousHeaderNew {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        cosineEncodersForward(40, 1.0);
        Thread.sleep(1000);

        halfCosineEncodersForward(40, 1.0);
        Thread.sleep(1000);

        cosineEncodersForward(40, 0.5);
        Thread.sleep(1000);

        halfCosineEncodersForward(40, 0.5);
        Thread.sleep(1000);

        cosineEncodersForward(6, 1.0);
        Thread.sleep(1000);

        halfCosineEncodersForward(6, 1.0);
        Thread.sleep(1000);

        cosineEncodersForward(6, 0.5);
        Thread.sleep(1000);

        halfCosineEncodersForward(6, 0.5);
        Thread.sleep(1000);

        cosineEncodersBackward(40, 1.0);
        Thread.sleep(1000);

        halfCosineEncodersBackward(40, 1.0);
        Thread.sleep(1000);

        cosineEncodersBackward(40, 0.5);
        Thread.sleep(1000);

        halfCosineEncodersBackward(40, 0.5);
        Thread.sleep(1000);

        cosineEncodersBackward(6, 1.0);
        Thread.sleep(1000);

        halfCosineEncodersBackward(6, 1.0);
        Thread.sleep(1000);

        cosineEncodersBackward(6, 0.5);
        Thread.sleep(1000);

        halfCosineEncodersBackward(6, 0.5);
        Thread.sleep(1000);
    }
}
