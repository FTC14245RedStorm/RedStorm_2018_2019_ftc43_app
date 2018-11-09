package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by skocik on 9/16/2017.
 */


public abstract class RevColorSensorNew extends OpMode {

  ColorSensor color_sensor;

  public void init() {
    color_sensor = hardwareMap.colorSensor.get("Color");
  }


  @Override
  public void loop() {

    color_sensor.red();
    color_sensor.blue();

    telemetry.addData("Color", color_sensor.red());
    telemetry.addData("Color", color_sensor.blue());
    telemetry.update();


  }
}