package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import clockworks.robot.Robot;
import clockworks.util.Alliance;

@TeleOp(name = "ClockworksTeleOP")
public class ClockworksTeleOP extends OpMode {
    private Robot robot;
    private boolean drivingToGlyph;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry,false);
        robot.setAlliance(Alliance.UNKNOWN);
        drivingToGlyph = false;

        boolean selectedAlliance = false;

        while(!selectedAlliance) {
            if(gamepad1.a) {
                robot.setAlliance(Alliance.BLUE);
                selectedAlliance = true;
            } else if(gamepad1.b) {
                robot.setAlliance(Alliance.RED);
                selectedAlliance = true;
            }
        }
    }

    @Override
    public void loop() {
        if(drivingToGlyph) {
            robot.driveToGlyphPosition();
        } else {
            robot.drive(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

        robot.intake(gamepad1.right_trigger - gamepad1.left_trigger);

        if(gamepad2.dpad_up) {
            robot.lift(1.0);
        } else if(gamepad2.dpad_down) {
            robot.lift(-1.0);
        } else {
            robot.lift(0);
        }

        if(gamepad2.dpad_right) {
            robot.rotateClaw(-1.0);
        } else if(gamepad2.dpad_left) {
            robot.rotateClaw(1.0);
        } else {
            robot.rotateClaw(0);
        }

        if(gamepad2.a) {
            robot.closeClaw();
        } else if(gamepad2.b) {
            robot.openClaw();
        }

        if(gamepad1.a) {
            robot.dropFoundationMovers();
        } else if(gamepad1.b) {
            robot.raiseFoundationMovers();
        }

        if(gamepad1.x) {
            drivingToGlyph = true;
        } else if(gamepad1.y) {
            drivingToGlyph = false;
        }
    }
}
