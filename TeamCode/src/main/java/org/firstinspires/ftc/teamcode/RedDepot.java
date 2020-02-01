package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import clockworks.robot.Robot;
import clockworks.util.Alliance;
import clockworks.util.Direction;

@Autonomous(name = "Red Depot")
public class RedDepot extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry, true);
        robot.setAlliance(Alliance.BLUE);

        waitForStart();
        resetStartTime();

        robot.encoderDrive(16, Direction.STANDARD, 0.5);
        sleep(300);
        robot.turn(90);
        sleep(300);
        robot.encoderDrive(70, Direction.STANDARD, 0.5);
        sleep(300);
        robot.turn(-90);
        sleep(300);
        robot.encoderDrive(10, Direction.STANDARD, 0.5);
        sleep(300);
        robot.dropFoundationMovers();
        sleep(600);
        robot.encoderDrive(-30, Direction.STANDARD, 0.5);
        sleep(300);
        robot.raiseFoundationMovers();
        sleep(600);
        //Change with drive right
        robot.encoderDrive(30, Direction.SIDEWAYS, 0.5);
        sleep(300);
        //
        robot.encoderDrive(22, Direction.STANDARD, 0.5);
        sleep(300);
        robot.turn(90);
        sleep(300);
        robot.encoderDrive(-18, Direction.STANDARD, 0.5);
        robot.stop();
    }
}
