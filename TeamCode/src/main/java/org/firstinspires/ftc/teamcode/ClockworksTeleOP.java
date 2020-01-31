package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import clockworks.robot.Robot;

//TODO add set-glyph position button
//TODO talk to drivers about control ideas
//TODO test drive to glyph function
//TODO give gamepad 2 control when near the block?

@TeleOp(name = "ClockworksTeleOP")
public class ClockworksTeleOP extends OpMode {
    private Robot robot;
    private boolean drivingToGlyph;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry,false);
        drivingToGlyph = false;
    }

    @Override
    public void loop() {
        if(drivingToGlyph) {
            robot.driveToGlyphPosition();
        } else {
            robot.drive(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

        if(robot.gamepad2CanDrive()) {
            robot.drive(0.5 * -gamepad2.left_stick_x, 0.3 * -gamepad2.left_stick_y,
                    0.3 * gamepad2.right_stick_x);
        }

        if(gamepad2.y) {
            robot.toggleGamepad2Drive();
        }

        if(gamepad2.left_bumper) {
            robot.setGlyphPosition();
        }
        if(gamepad2.x) {
            robot.driveToGlyphPosition();
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
