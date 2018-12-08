package RedStorm.reference;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous
@Disabled

public class IndieAutonomousOptions extends IndieRoboRaidersAuto {

    public IndieRobot robot = new IndieRobot();

    boolean cur_B_ButtonState;                                            // "b" button current state
    boolean cur_X_ButtonState;                                            // "x" button current state

    boolean prev_B_ButtonState;                                           // "b" button previous state
    boolean prev_X_ButtonState;                                           // "x" button previous state

    //----------------------------------------------------------------------------------------------
    // Main logic
    //----------------------------------------------------------------------------------------------

    @Override
    public void runOpMode() throws InterruptedException {

        // Array to store selected options, by design selectedOptions[0][*] is used to store the
        // response for finalizing the selected options.
        String[][] selectedOptions = new String[6][2];                    // Array to store selections

        String allianceSelPrompt = "Alliance Color";                      // Alliance Color prompt
        final String[] alliancePosResps = new String[]                    // Possible Alliance Color selections
                {"red", "blue"};

        String bsSelPrompt = "Balancing Stone Location";                  // Balancing Stone Location prompt
        final String[] bsPosResps = new String[]                          // Possible Balancing Stone Location selections
                {"close", "far"};

        String jewelSelPrompt = "Jewel";                                  // Jewel prompt
        final String[] jewelPosResps = new String[]                       // Possible Jewel selections
                {"no", "yes"};

        String parkCryptSelPrompt = "Parking and/or Cryptobox";           // Parking and/or Cryptobox prompt
        final String[] parkCryptPosResps = new String[]                   // Possible Parking and/or Cryptobox selections
                {"Just Park", "Cryptobox Vuforia"};

        String selectionsOk = "Selections Great :)";                      // Finished Selections prompt
        final String[] finishedSelPosResps = new String[]                 // Possible Finished Selections responses
                {"no", "yes"};

        // Default that the options to be selected are not finalized.  This is done since we want
        // the loop below to actually loop.  At the end of the loop, the question will be asked if
        // the selections are ok, if they are, then this will be set to "yes" and the loop will
        // exit.
        selectedOptions[0][1] = "no";

        // Configure for Indie FacingCrater while the selections are not finalized
        while (selectedOptions[0][1].equals("no")) {

            //                        Prompt            Responses    Index  Options output
            configForAuto2Options(allianceSelPrompt, alliancePosResps, 1, selectedOptions);       // Alliance Color selection
            configForAuto2Options(bsSelPrompt, bsPosResps, 2, selectedOptions);                   // Balancing Stone Location selection
            configForAuto2Options(jewelSelPrompt, jewelPosResps, 3, selectedOptions);             // Jewel selection
            configForAuto2Options(parkCryptSelPrompt, parkCryptPosResps, 4, selectedOptions);     // Parking and/or Cryptobox selection

            // Loop through all of the selections and tell user what s/he has selected
            for (int i = 1; i <= 4; i++) {

                telemetry.addLine().addData(selectedOptions[i][0], selectedOptions[i][1]);
            }

            // Are you sure about the options selected?
            //                        Prompt       Responses       Index Options output
            configForAuto2Options(selectionsOk, finishedSelPosResps, 0, selectedOptions);       // Finished Selections
        }

        robot.initialize(hardwareMap);
        alignRobot(robot, selectedOptions[1][1], selectedOptions[2][1]);

        // Initializing
        telemetry.addLine("Robot is Aligned - Press B to Initialize and Wait for Start");
        telemetry.update();

        gamepad1.reset();

        while (!gamepad1.b) {

        }

        robot.initializeServosAutonomous();
        vuforiaInitialization(hardwareMap);
        telemetry.addLine("Initialized");
        telemetry.update();

        // Wait for start to be pushed
        waitForStart();

        // Jewel
        if (selectedOptions[3][1].equals("yes")) {

            lowerArm(robot);
            selectJewel(robot, selectedOptions[1][1]);
        }

        // Parking and/or Cryptobox
        if (selectedOptions[4][1].equals("Just Park")) {

            if (selectedOptions[1][1].equals("blue") && selectedOptions[2][1].equals("close")) {

                justParkCloseBlue(robot);
            }
            else if (selectedOptions[1][1].equals("blue") && selectedOptions[2][1].equals("far")) {

                justParkFarBlue(robot);
            }
            else if (selectedOptions[1][1].equals("red") && selectedOptions[2][1].equals("close")) {

                justParkCloseRed(robot);
            }
            else if (selectedOptions[1][1].equals("red") && selectedOptions[2][1].equals("far")) {

                justParkFarRed(robot);
            }
        }
        else if (selectedOptions[4][1].equals("Cryptobox Vuforia")) {

            getRelicRecoveryVuMark();
            Thread.sleep(250);

            selectColumn(robot, selectedOptions[1][1], selectedOptions[2][1], pictograph);
        }
    }

    /**
     * configForAuto2Options will save the response (selOptions) from a set of 2 possible responses (posResps)
     * a given prompt (selPrompt)
     *
     * @param selPrompt The given configuration prompt
     * @param posResps The possible responses to a given configuration prompt
     * @param selIndex The position within selOptions array in which to store the responses
     * @param selOptions Where to store the configuration prompt and response
     */
    public void configForAuto2Options(String selPrompt, String[] posResps, int selIndex, String[][] selOptions) {

        // Let the user Select
        gamepad1.reset();

        prev_B_ButtonState = false;
        prev_X_ButtonState = false;
        cur_B_ButtonState = false;
        cur_X_ButtonState = false;

        selOptions[selIndex][0] = selPrompt;

        // Prompt User for Selection
        telemetry.addLine(selPrompt);
        telemetry.addLine(posResps[1] + " - X " + " or " + posResps[0] + " - B" );
        telemetry.update();

        // Loop until either the "b" button or the "x" button is pressed, initially we set both
        // buttons to indicate they have not been pressed.
        //
        // The logic here says OR the previous button states and when they are both false continue
        // here is a table of how this works
        //    +--------------------+------+--------------------+--------+-------------+
        //    | prev_B_ButtonState | -OR- | prev_X_ButtonState | Result | Neg. Result |
        //    +--------------------+------+--------------------+--------+-------------+
        //    |      FALSE         | -OR- |     FALSE          | FALSE  |   TRUE      |
        //    +--------------------+------+--------------------+--------+-------------+
        //    |      FALSE         | -OR- |     TRUE           | TRUE   |   FALSE     |
        //    +--------------------+------+--------------------+--------+-------------+
        //    |      TRUE          | -OR- |     FALSE          | TRUE   |   FALSE     |
        //    +--------------------+------+--------------------+--------+-------------+
        //    |      TRUE          | -OR- |     TRUE           | TRUE   |   FALSE     |
        //    +--------------------+------+--------------------+--------+-------------+

        while (!(prev_B_ButtonState | prev_X_ButtonState)) {

            cur_B_ButtonState = gamepad1.b;                           // get the current state of button b
            cur_X_ButtonState = gamepad1.x;                           // get the current state of button x

            if (cur_B_ButtonState) {                                  // when the "b" button on the gamepad is pressed
                if (!prev_B_ButtonState) {                            // when the previous "b" button was NOT pushed
                    selOptions[selIndex][1] = posResps[0];            // first response was selected, store the response
                    prev_B_ButtonState = true;                        // indicate that the previous B button state is PUSHED
                }
            }
            else if (cur_X_ButtonState) {                             // when the "x" button on the gamepad is pressed
                if (!prev_X_ButtonState) {                            // when the previous "x" button was NOT pushed
                    selOptions[selIndex][1] = posResps[1];            // second response was selected, store the response
                    prev_X_ButtonState = true;                        // indicate that the previous X button state is PUSHED
                }
            }
        }

        telemetry.addLine().addData(selOptions[selIndex][0], selOptions[selIndex][1]);
        telemetry.update();

        // Wait one second
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}