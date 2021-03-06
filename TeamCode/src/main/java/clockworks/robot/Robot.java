package clockworks.robot;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import clockworks.util.Alliance;
import clockworks.util.Direction;

import static clockworks.util.InputUtils.*;

/**
 * This class is an abstraction of our FTC Robot
 *
 * It covers basic control of the robot as well as the inputs it has.
 */
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
    private final double DISTANCE_TOLERANCE = 0.5;
    private double clawRotatorPower = 0.0;

    private Alliance alliance;
    private boolean usingEncoders;

    private static final int COUNTS_PER_MOTOR_REV = 1440;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 3.9375;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    private final double WHEEL_BASE_WIDTH = 13.25;
    private final double WHEEL_BASE_LENGTH = 12.0;

    private boolean gamePad2CanDrive = false;

    private Telemetry telemetry;

    /**
     * Creates an instance of the Robot.
     * @param hardwareMap the opmode hardwaremap to get parts
     * @param telemetry the opmode instance of telemetry to send data
     * @param usingEncoders whether or not you are running the robot with encoders
     */
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, boolean usingEncoders) {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        if(usingEncoders) {
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } else {
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        intakeLeft = hardwareMap.get(DcMotor.class, "intakeLeft");
        intakeRight = hardwareMap.get(DcMotor.class, "intakeRight");

        lift = hardwareMap.get(DcMotor.class, "lift");
        clawRotator = hardwareMap.get(DcMotor.class, "clawRotator");

        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        clawRotator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw = hardwareMap.get(Servo.class, "claw");

        leftFoundationMover = hardwareMap.get(Servo.class, "leftFoundationMover");
        rightFoundationMover = hardwareMap.get(Servo.class, "rightFoundationMover");

        leftDistanceSensor = (Rev2mDistanceSensor)(hardwareMap.get(DistanceSensor.class, "leftDistanceSensor"));
        rightDistanceSensor = (Rev2mDistanceSensor)(hardwareMap.get(DistanceSensor.class, "rightDistanceSensor"));
        backDistanceSensor = (Rev2mDistanceSensor)(hardwareMap.get(DistanceSensor.class, "backDistanceSensor"));

        alliance = Alliance.UNKNOWN;
        this.usingEncoders = usingEncoders;
        this.telemetry = telemetry;
    }

    /**
     * Controls the robots mecanum base given x, y, and rotational powers.
     * @param leftX controls the x value of the robot. Negative values drive left
     * @param leftY controls the y value of the robot. Positive values drive forward
     * @param rightX controls the rotation of the robot. Negative values make it turn left
     */
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


    /**
     * Main enncoder drive function for overloads
     * all distances are limited from [-144, 144]
     * @param leftFront left front wheel distance in inches
     * @param leftBack left back wheel distance in inches
     * @param rightFront right front wheel distance in inches
     * @param rightBack right back wheel distance in inches
     * @param speed motor speed from [0, 1]
     */
    public void encoderDrive(double leftFront, double leftBack, double rightFront, double rightBack, double speed) {
        if(usingEncoders) {
            leftFront = limitValue(leftFront, -144, 144);
            leftBack = limitValue(leftBack, -144, 144);
            rightFront = limitValue(rightFront, -144, 144);
            rightBack = limitValue(rightBack, -144, 144);

            int leftFrontTarget = frontLeft.getCurrentPosition() + (int) (leftFront * COUNTS_PER_INCH);
            int leftBackTarget = backLeft.getCurrentPosition() + (int) (leftBack * COUNTS_PER_INCH);
            int rightFrontTarget = frontRight.getCurrentPosition() + (int) (rightFront * COUNTS_PER_INCH);
            int rightBackTarget = backRight.getCurrentPosition() + (int) (rightBack * COUNTS_PER_INCH);

            frontLeft.setTargetPosition(leftFrontTarget);
            frontRight.setTargetPosition(rightFrontTarget);
            backLeft.setTargetPosition(leftBackTarget);
            backRight.setTargetPosition(rightBackTarget);

            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            frontLeft.setPower(speed);
            frontRight.setPower(speed);
            backLeft.setPower(speed);
            backRight.setPower(speed);

            while (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {
                telemetry.addData("Left Target:", leftFrontTarget);
                telemetry.addData("Left Current:", frontLeft.getCurrentPosition());

                telemetry.addData("Right Target:", rightFrontTarget);
                telemetry.addData("Right Current:", frontRight.getCurrentPosition());

                telemetry.addData("Front Left:", frontRight.isBusy());
                telemetry.addData("Back Left:", backLeft.isBusy());
                telemetry.addData("Front Right:", frontRight.isBusy());
                telemetry.addData("Back Right:", backRight.isBusy());

                telemetry.update();
            }

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     * drives either forward / backward, or left / right
     * @param inches distance needed to travel in inches
     * @param direction enum specifying which direction to travel
     * @param speed motor speed, limited from [0, 1]
     */
    public void encoderDrive(double inches, Direction direction, double speed) {
        //TODO: figure out proportion of sideways travel distance
        if(direction == Direction.STANDARD) {
            encoderDrive(inches, inches, inches, inches, speed);
        } else if(direction == Direction.SIDEWAYS) {
            encoderDrive(-inches, inches, inches, -inches, speed);
        }
    }

    /**
     * Turns the robot the specified amount
     * @param degrees the angle that the robot should turn. Negative values turn left.
     */
    public void turn(double degrees) {
        degrees = findCoterminalAngle(degrees);

        //double leftDistance = 0.5 * Math.sqrt(WHEEL_BASE_WIDTH * WHEEL_BASE_WIDTH + WHEEL_BASE_LENGTH * WHEEL_BASE_LENGTH) * (degrees * (Math.PI / 180));
        //double rightDistance = -0.5 * Math.sqrt(WHEEL_BASE_WIDTH * WHEEL_BASE_WIDTH + WHEEL_BASE_LENGTH * WHEEL_BASE_LENGTH) * (degrees * (Math.PI / 180));

        double leftDistance = -1 * WHEEL_BASE_WIDTH * Math.PI * 2 * (degrees/360.0);
        double rightDistance = WHEEL_BASE_WIDTH * Math.PI * 2 * (degrees/360.0);

        encoderDrive(leftDistance, leftDistance, rightDistance, rightDistance, 0.5);
    }

    /**
     * Controls the intake
     * @param power the power that the intake should be run at. Limited to [-1, 1]
     */
    public void intake(double power) {
        power = limitValue(power);

        power *= 0.75;
        intakeLeft.setPower(power);
        intakeRight.setPower(power);
    }

    /**
     * Controls the power of the lift
     * @param power the power to run the lift at. Limited to [-1, 1]
     */
    public void lift(double power) {
        power = limitValue(power);
        if(power < 0) {
            power *= 0.4;
        } else {
            power *= 0.7;
        }
        lift.setPower(power);
    }

    /**
     * Controls the power of the rotation of the claw.
     * @param power the power to rotate the claw at. Limited to [-1, 1]
     */
    public void rotateClaw(double power) {
        power = 0.10 * limitValue(power);
        int direction = 1;
        if(power < 0) {
            direction = -1;
        }
        power = Math.abs(power);
        if(clawRotatorPower < power) {
            clawRotatorPower += 0.02;
        } else if(power == 0) {
            clawRotatorPower -= 0.02;
            if(clawRotatorPower < 0) clawRotatorPower = 0;
        }

        clawRotator.setPower(clawRotatorPower * direction);
    }

    /**
     * Closes the claw
     */
    public void closeClaw() {
        claw.setPosition(0.8);
    }

    /**
     * Opens the claw
     */
    public void openClaw() {
        claw.setPosition(0);
    }

    /**
     * Raises the foundation movers
     */
    public void raiseFoundationMovers() {
        leftFoundationMover.setPosition(0);
        rightFoundationMover.setPosition(1);
    }

    /**
     * Drops the foundation movers
     */
    public void dropFoundationMovers() {
        leftFoundationMover.setPosition(1);
        rightFoundationMover.setPosition(0);
    }

    /**
     * Stops all movement of the robot
     */
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

    /**
     * Gets the distance sensor value on the left side of the robot
     * @return the distance from the left side of the robot to the nearest object
     */
    public double getLeftDistance() {
        return leftDistanceSensor.getDistance(DistanceUnit.INCH);
    }

    /**
     * Gets the distance sensor value on the right side of the robot
     * @return the distance from the right side of the robot to the nearest object
     */
    public double getRightDistance() {
        return rightDistanceSensor.getDistance(DistanceUnit.INCH);
    }

    /**
     * Gets the distance sensor value on the back of the robot
     * @return the distance from the back side of the robot to the nearest object.
     */
    public double getBackDistance() {
        return backDistanceSensor.getDistance(DistanceUnit.INCH);
    }

    /**
     * Decides if the robot is in range of the target distance on the left side
     * @return whether or not the robot is in target range on the left side
     */
    public boolean leftInRange() {
        return (Math.abs(this.getLeftDistance() - leftGlyphDistance) < DISTANCE_TOLERANCE);
    }

    /**
     * Decides if the robot is in range of the target distance on the right side
     * @return whether or not the robot is in target range on the right side
     */
    public boolean rightInRange() {
        return (Math.abs(this.getRightDistance() - rightGlyphDistance) < DISTANCE_TOLERANCE);
    }

    /**
     * Decides if the robot is in range of the target distance on the back side
     * @return whether or not the robot is in target range on the back side
     */
    public boolean backInRange() {
        return (Math.abs(this.getBackDistance() - backGlyphDistance) < DISTANCE_TOLERANCE);
    }

    /**
     * Sets the target glyph position
     */
    public void setGlyphPosition() {
        leftGlyphDistance = this.getLeftDistance();
        rightGlyphDistance = this.getRightDistance();
        backGlyphDistance = this.getBackDistance();
    }

    /**
     * Checks to see if the robot is in the target range to place blocks
     * @return whether or not the robot is in the target range
     */
    public boolean inRange() {
        if(alliance == Alliance.RED)
            return (this.leftInRange() && this.backInRange());
        else if(alliance == Alliance.BLUE)
            return (this.rightInRange() && this.backInRange());

        return false;
    }

    /**
     * Drives to the the target position on the field. Used to place blocks
     */
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

    /**
     * toggles gamepad2's ability to drive slowly, meant for accurate movement when close to the
     * landing area
     */
    public void toggleGamepad2Drive() {
        gamePad2CanDrive = !gamePad2CanDrive;
    }

    public boolean gamepad2CanDrive() {
        return gamePad2CanDrive;
    }

    /**
     * Sets the alliance. Assumed to be unknown on initialization.
     * @param alliance the alliance color that the robot is on for the match. Either Blue or Red.
     */
    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }
}
