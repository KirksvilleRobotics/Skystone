package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import clockworks.robot.Robot;
import clockworks.util.Alliance;
import clockworks.util.Direction;

@Autonomous(name = "Blue Build Zone")
public class BlueBuildZone extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry, true);
        robot.setAlliance(Alliance.BLUE);

        waitForStart();
        resetStartTime();

        robot.encoderDrive(30, Direction.STANDARD, 0.5);
        sleep(500);
        robot.dropFoundationMovers();
        sleep(800);
        robot.encoderDrive(-32, Direction.STANDARD, 0.5);
        sleep(500);
        robot.raiseFoundationMovers();
        sleep(500);
        // Change to drive right
        robot.encoderDrive(-30, Direction.SIDEWAYS, 0.5);
        sleep(500);
        //
        robot.encoderDrive(24, Direction.STANDARD, 0.5);
        sleep(500);
        robot.turn(-90);
        robot.encoderDrive(-18, Direction.STANDARD, 0.5);
        robot.stop();
    }
}
