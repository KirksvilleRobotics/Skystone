package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import caleb.server.BluetoothClient;

@TeleOp(name="Test Tele-Op")
public class TestOp extends OpMode {

    //private BluetoothClient bluetoothClient;

    /*DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;*/

    DcMotor rightIntake;
    DcMotor leftIntake;

    @Override
    public void init() {
        telemetry.addData("debug", "debug");
        //bluetoothClient = new BluetoothClient(telemetry, hardwareMap.appContext);
        //bluetoothClient.sendData(1.0, 2.0, 78.0);
/*
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
*/
        rightIntake = hardwareMap.get(DcMotor.class, "rightIntake");
        leftIntake = hardwareMap.get(DcMotor.class, "leftIntake");

        rightIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        leftIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        /*
        double lx = gamepad1.left_stick_x;
        double ly = gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;

        frontRight.setPower(-lx + ly - rx);
        frontLeft.setPower(lx + ly + rx);
        backRight.setPower(lx + ly - rx);
        backLeft.setPower(-lx + ly + rx);*/

        double intakePower = gamepad1.right_trigger;

        rightIntake.setPower(intakePower);
        leftIntake.setPower(intakePower);
    }
}
