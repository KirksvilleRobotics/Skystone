package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import clockworks.robot.Robot;
import clockworks.util.Direction;

@Autonomous(name = "Test Autonomous")
public class AutonomousTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry, true);

        waitForStart();
        resetStartTime();

        robot.encoderDrive(24, Direction.STANDARD, 0.5);
        sleep(5000);
        robot.encoderDrive(24, Direction.SIDEWAYS, 0.5);
        sleep(5000);
        robot.turn(90);
    }
}
