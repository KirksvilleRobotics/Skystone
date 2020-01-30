package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import clockworks.robot.Robot;

@Autonomous(name = "Clockworks Autonomous")
public class ClockworksAutonomous extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry,  true);

        waitForStart();
        resetStartTime();

        robot.turn(90);
        robot.stop();
    }
}
