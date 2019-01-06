package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
@Disabled
public class InitializingVuforia {
    class CalebTensorFlowObjectDetection {
        private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
        private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
        private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

        private static final String VUFORIA_KEY = "ASP5Bz//////AAABmQpXQrmR/EGhouIfX6bxE+QBXEzQhHkUF4EXvYLMlpAalmmuiQbsJJH9E1kF88zP9D4TH9IovzC83G9uVx1Td+/ei7Yy0ej+PZt0w7QgnMHUGBy140Ma9IPr+fsgDTDcUjtbpqp7GvBTBa3cGlZ1t/NPjzwzhL74HWO4ibMucuEIaC0qkQ7pBQzLEx4ZhDteI98SYo096qruhWPtA1mY0PwpZ3CkceWVQfMsuWfsDc4ZtxsoezvYTrXoasoWTAv0r/S/TJ26E/SUo200knbh4qnz510paJYlMKRJFx7GOXIm543osLJj3og4XYFdircW9sI386gpTsRrSqeD2tvDMAHjLDzag6ggyPrnI/eQOCeY";

        private VuforiaLocalizer vuforia;

        /**
         * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
         * Detection engine.
         */
        private TFObjectDetector tfod;
        private int goldLocation = -1;
    }
}
