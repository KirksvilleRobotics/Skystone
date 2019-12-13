package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import clockworks.robot.Robot;

@TeleOp(name = "ClockworksTeleOP")
public class ClockworksTeleOP extends OpMode {
    private Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Trigger", gamepad2.left_trigger);
        telemetry.addData("Right Trigger", gamepad2.right_trigger);
        robot.drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
        robot.intake(gamepad1.right_trigger - gamepad1.left_trigger);

        robot.lift(.5 * gamepad2.right_stick_y);
        robot.armExtender(gamepad2.right_trigger - gamepad2.left_trigger);

        telemetry.addData("CRServo Left power: ", robot.armExtenderLeft.getPower());
        telemetry.addData("CRServo Right power: ", robot.armExtenderRight.getPower());

        robot.toggleClaw(gamepad2.a, getRuntime());
        if(gamepad2.b) {
            telemetry.addData("Claw Servo Position", robot.rightClaw.getPosition());
        }
    }
}
