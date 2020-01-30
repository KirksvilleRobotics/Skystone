package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import clockworks.robot.Robot;
import clockworks.util.Alliance;

@Autonomous(name = "Blue Build Zone")
public class BlueBuildZone extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry, true);
        robot.setAlliance(Alliance.BLUE);

        waitForStart();
        resetStartTime();

        robot.encoderDrive(36, 36, 0.5);
        robot.dropFoundationMovers();
        robot.encoderDrive(-36, -36, 0.5);
        robot.raiseFoundationMovers();
        // Change to drive right
        robot.turn(90);
        robot.encoderDrive(18, 18, 0.5);
        robot.turn(-90);
        //
        robot.encoderDrive(24, 24, 0.5);
        robot.turn(90);
        robot.encoderDrive(18, 18, 0.5);
        robot.stop();
    }
}
