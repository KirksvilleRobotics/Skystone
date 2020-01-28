package clockworks.robot;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import clockworks.util.Alliance;

import static clockworks.util.InputUtils.*;

public class Robot {
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    private DcMotor intakeLeft;
    private DcMotor intakeRight;

    private DcMotor lift;
    private DcMotor clawRotator;

    private Servo claw;

    private Servo leftFoundationMover;
    private Servo rightFoundationMover;

    private Rev2mDistanceSensor leftDistanceSensor;
    private Rev2mDistanceSensor rightDistanceSensor;
    private Rev2mDistanceSensor backDistanceSensor;

    private double leftGlyphDistance = 0.0;
    private double rightGlyphDistance = 0.0;
    private double backGlyphDistance = 0.0;
    private double distanceTolerance = 0.5;

    private Alliance alliance;

    public Robot(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeLeft = hardwareMap.get(DcMotor.class, "intakeLeft");
        intakeRight = hardwareMap.get(DcMotor.class, "intakeRight");

        lift = hardwareMap.get(DcMotor.class, "lift");
        clawRotator = hardwareMap.get(DcMotor.class, "clawRotator");

        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        clawRotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw = hardwareMap.get(Servo.class, "claw");

        leftFoundationMover = hardwareMap.get(Servo.class, "leftFoundationMover");
        rightFoundationMover = hardwareMap.get(Servo.class, "rightFoundationMover");

        leftDistanceSensor = (Rev2mDistanceSensor)(hardwareMap.get(DistanceSensor.class, "leftDistanceSensor"));
        rightDistanceSensor = (Rev2mDistanceSensor)(hardwareMap.get(DistanceSensor.class, "rightDistanceSensor"));
        backDistanceSensor = (Rev2mDistanceSensor)(hardwareMap.get(DistanceSensor.class, "backDistanceSensor"));

        alliance = Alliance.UNKNOWN;
    }

    public void drive(double leftX, double leftY, double rightX) {
        double frontLeftPower = limitValue(-leftX + leftY - rightX);
        double backLeftPower = limitValue(leftX + leftY - rightX);
        double frontRightPower = limitValue(leftX + leftY + rightX);
        double backRightPower = limitValue(-leftX + leftY + rightX);

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public void intake(double power) {
        power = limitValue(power);

        power *= 0.75;
        intakeLeft.setPower(power);
        intakeRight.setPower(power);
    }

    public void lift(double power) {
        power = limitValue(power);

        power *= 0.7;
        lift.setPower(power);
    }

    public void rotateClaw(double power) {
        power = 0.15 * limitValue(power);

        clawRotator.setPower(power);
    }

    public void closeClaw() {
        claw.setPosition(0.8);
    }

    public void openClaw() {
        claw.setPosition(0);
    }

    public void dropFoundationMovers() {
        leftFoundationMover.setPosition(0);
        rightFoundationMover.setPosition(1);
    }

    public void raiseFoundationMovers() {
        leftFoundationMover.setPosition(1);
        rightFoundationMover.setPosition(0);
    }

    public void stop() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);

        intakeLeft.setPower(0);
        intakeRight.setPower(0);

        lift.setPower(0);
        clawRotator.setPower(0);
    }

    public double getLeftDistance() {
        return leftDistanceSensor.getDistance(DistanceUnit.INCH);
    }

    public double getRightDistance() {
        return rightDistanceSensor.getDistance(DistanceUnit.INCH);
    }

    public double getBackDistance() {
        return backDistanceSensor.getDistance(DistanceUnit.INCH);
    }

    public boolean leftInRange() {
        return (Math.abs(this.getLeftDistance() - leftGlyphDistance) < distanceTolerance);
    }

    public boolean rightInRange() {
        return (Math.abs(this.getRightDistance() - rightGlyphDistance) < distanceTolerance);
    }

    public boolean backInRange() {
        return (Math.abs(this.getBackDistance() - backGlyphDistance) < distanceTolerance);
    }

    public void setGlyphPosition() {
        leftGlyphDistance = this.getLeftDistance();
        rightGlyphDistance = this.getRightDistance();
        backGlyphDistance = this.getBackDistance();
    }

    public boolean inRange() {
        if(alliance == Alliance.RED)
            return (this.leftInRange() && this.backInRange());
        else if(alliance == Alliance.BLUE)
            return (this.rightInRange() && this.backInRange());

        return false;
    }

    public void driveToGlyphPosition() {
        if(!this.inRange()) {
            if(!backInRange()) {
                if(this.getBackDistance() > backGlyphDistance) {
                    this.drive(0.0, -0.5, 0.0);
                } else if(this.getBackDistance() < backGlyphDistance) {
                    this.drive(0.0, 0.5, 0.0);
                }
            } else if(alliance == Alliance.RED) {
                if(!leftInRange()) {
                    if(this.getLeftDistance() > leftGlyphDistance) {
                        this.drive(-0.5, 0.0, 0.0);
                    } else if(this.getLeftDistance() < leftGlyphDistance) {
                        this.drive(0.5, 0.0, 0.0);
                    }
                }
            } else if(alliance == Alliance.BLUE) {
                if(!rightInRange()) {
                    if(this.getRightDistance() > rightGlyphDistance) {
                        this.drive(0.5, 0.0, 0.0);
                    } else if(this.getRightDistance() < rightGlyphDistance) {
                        this.drive(-0.5, 0.0, 0.0);
                    }
                }
            }
        } else {
            this.drive(0.0, 0.0, 0.0);
        }
    }
}
