package clockworks.robot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {

    private static final int DELAY = 300;

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    private DcMotor lift;

    private DcMotor intakeLeft;
    private DcMotor intakeRight;

    public CRServo armExtenderLeft;
    public CRServo armExtenderRight;

    public Servo leftClaw;
    public Servo rightClaw;

    private boolean isClosed;

    private double lastClawtime;

    public Robot(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        lift = hardwareMap.get(DcMotor.class, "lift");

        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeLeft = hardwareMap.get(DcMotor.class, "intakeLeft");
        intakeRight = hardwareMap.get(DcMotor.class, "intakeRight");

        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);

        armExtenderLeft = hardwareMap.get(CRServo.class, "armExtenderLeft");
        armExtenderRight = hardwareMap.get(CRServo.class, "armExtenderRight");

        armExtenderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        leftClaw = hardwareMap.get(Servo.class, "leftClaw");
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");

        isClosed = false;

        lastClawtime = 0;
    }

    public void drive(double leftX, double leftY, double rightX) {
        /*
        flPower = + x + y + r
        frPower = + x - y - r
        blPower = + x - y + r
        brPower = + x + y - r
         */
        frontLeft.setPower(-leftX + leftY - rightX);
        backLeft.setPower(-leftX + leftY + rightX);
        frontRight.setPower(leftX + leftY + rightX);
        backRight.setPower(leftX + leftY - rightX);
    }

    public void intake(double power) {
        intakeLeft.setPower(power);
        intakeRight.setPower(power);
    }

    public void lift(double power) {
        lift.setPower(power);
    }

    public void armExtender(double power) {
        armExtenderLeft.setPower(power);
        armExtenderRight.setPower(power);
    }

    public void toggleClaw(boolean isRequested, double runTime) {
        //TODO: figure out positions
        if(runTime - lastClawtime > DELAY) {
            if (isRequested) {
                if (isClosed) {
                    leftClaw.setPosition(0.0);
                    rightClaw.setPosition(0.0);
                    isClosed = false;
                    lastClawtime = runTime;

                } else {
                    leftClaw.setPosition(0.25);
                    rightClaw.setPosition(0.25);
                    isClosed = true;
                    lastClawtime = runTime;
                }
            }
        }
    }

    public void stop() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);

        lift.setPower(0);

        intakeLeft.setPower(0);
        intakeRight.setPower(0);

        armExtenderLeft.setPower(0);
        armExtenderRight.setPower(0);
    }
}
