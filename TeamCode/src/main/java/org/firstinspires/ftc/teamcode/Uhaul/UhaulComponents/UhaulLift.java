package org.firstinspires.ftc.teamcode.Uhaul.UhaulComponents;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BigDipper.RobotComponents.RobotComponentImplBase;
import org.firstinspires.ftc.teamcode.Uhaul.UhaulComponents.UhaulComponentImplBase;

import static android.os.SystemClock.sleep;

/**
 * Defines the Uhaul Lift
 * @author Raw Bacon Coders
 */
public class UhaulLift extends UhaulComponentImplBase {

    String UHAUL_LIFT_1 = "uhaul_lift_1";
    String UHAUL_LIFT_2 = "uhaul_lift_2";
    double BLOCK_HEIGHT = 5;
    double INITIAL_HEIGHT = 0; //height to get to before the first block
    double MAX_TICKS_BEFORE_OVERRIDE = (40)*(COUNTS_PER_INCH);
    double LIFT_MAX_SPEED = 1;
    double LIFT_SPEED_IN_AUTONOMOUS = 0.5;

    double kp = 1;
    double ki = 0;
    double kd = 0;
    double kf = 0;


    private ElapsedTime runtime = new ElapsedTime();

    public DcMotorEx uhaulLift = null;
    public DcMotorEx uhaulLiftTwo = null;


    private static final double COUNTS_PER_MOTOR_REV = 383.6;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 0.315;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    private static final double COUNTS_PER_DEGREE = 15;

    public int dpadBlocks = 0;


    /** Overrides the default opmode for UhaulLift */
    public UhaulLift(LinearOpMode opMode) {
        super(opMode);
    }

    /** Initializes the proccess */
    @Override
    public void init() {
        uhaulLift = (DcMotorEx) hardwareMap.dcMotor.get(UHAUL_LIFT_1);
        uhaulLiftTwo = (DcMotorEx) hardwareMap.dcMotor.get(UHAUL_LIFT_2);

        PIDFCoefficients pidNew = new PIDFCoefficients(kp, ki, kd, kf);
        uhaulLift.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);
        uhaulLiftTwo.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);

        uhaulLift.setDirection(DcMotor.Direction.FORWARD);
        uhaulLiftTwo.setDirection(DcMotor.Direction.FORWARD);

        uhaulLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        uhaulLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        uhaulLiftTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    /** Initializes the proccess for testing purposes */
public void initForTesting(){
    //we don't want it to brake on 0 power!
    uhaulLift = (DcMotorEx) hardwareMap.dcMotor.get(UHAUL_LIFT_1);
    uhaulLiftTwo = (DcMotorEx) hardwareMap.dcMotor.get(UHAUL_LIFT_2);

   // PIDFCoefficients pidNew = new PIDFCoefficients(kp, ki, kd, kf);
   // uhaulLift.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);
   // uhaulLiftTwo.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);

    uhaulLift.setDirection(DcMotor.Direction.FORWARD);
    uhaulLiftTwo.setDirection(DcMotor.Direction.FORWARD);
    uhaulLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


}
//increment encoder positions using specified dpad presses in order to go up that many blocks high
    //have joystick at .25 if a dpad value is above 0 and then at .5 when dpad = 0
    //take away driver control after encoder value reached-ish
    //display current dpad value in telemetry

    //try to find a way to check if the button goes up or down during the loop

    boolean override = false;
    int liftEncoderSetpoint = 0;

//Would be nice to use state machines here and enums! Example:   https://gm0.copperforge.cc/en/latest/docs/software/fundamental-concepts.html#finite-state-machines-and-enums
    boolean comingUp = false;
    /** Defines the lift for the teleop */
    public void liftTeleOp() {

//TODO Add A function to fix encoder inconsistencies
        if ((gamepad2.dpad_up || gamepad2.dpad_down) && (Math.abs(gamepad2.right_stick_y) < 0.1)) {
            if (gamepad2.dpad_up) {
                dpadBlocks++;
                comingUp = true;

            } else {
                if(dpadBlocks > 0) {
                    dpadBlocks--;
                }
                else{
                    dpadBlocks = 0;
                }
                comingUp = false;
            }

            if((dpadBlocks == 1) && comingUp){
                liftEncoderSetpoint = (int) ((INITIAL_HEIGHT + (BLOCK_HEIGHT * dpadBlocks)) * COUNTS_PER_INCH);

            }
            else{
                liftEncoderSetpoint = (int) ((BLOCK_HEIGHT * dpadBlocks) * COUNTS_PER_INCH);

            }

            teleOpEncoderDrive();



        } else if (gamepad2.a) {
            dpadBlocks = 0;

            liftEncoderSetpoint = 0;

            teleOpEncoderDrive();


        } else if (dpadBlocks == 0) {
            if ((uhaulLift.getCurrentPosition() < MAX_TICKS_BEFORE_OVERRIDE) && (Math.abs(gamepad2.right_stick_y) > 0.1)) {
                uhaulLift.setPower(gamepad2.right_stick_y / 2);
                uhaulLiftTwo.setPower(gamepad2.right_stick_y /2);
            } else if((uhaulLift.getCurrentPosition() < MAX_TICKS_BEFORE_OVERRIDE) && (Math.abs(gamepad2.right_stick_y) <= 0.1)) {
                //do nothing, it's already at the right speed
            }
            else{
                    override = true;
                }



        } else if (override) {

            liftEncoderSetpoint = (int) ((-2) * COUNTS_PER_INCH);

            override = false;

            teleOpEncoderDrive();



        } else if(gamepad2.right_stick_y > 0.1) {
            uhaulLift.setPower(gamepad2.right_stick_y / 4);

        }

        telemetry.addData("Current Dpad Blocks Set To: ", dpadBlocks);
        telemetry.addData("Press 'A' on gamepad 2", " to reset!");
        telemetry.update();








    }




    int previousBlocks = 0;

    /** Defines the liftAuto proccess */
    public void liftAuto(int howManyBlocks, double speed, double timeoutS) {

        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();



        if ((howManyBlocks < previousBlocks) && (howManyBlocks == 0)) {
            liftFor(((-previousBlocks * BLOCK_HEIGHT) - INITIAL_HEIGHT), -speed, timeoutS);
        } else if ((howManyBlocks > previousBlocks) && (previousBlocks == 0)) {
            liftFor(((howManyBlocks * BLOCK_HEIGHT) + INITIAL_HEIGHT), speed, timeoutS);
        }
        //above are the normal situations, but what if we want to go from one block to another?
        else if (howManyBlocks != previousBlocks) {
            liftFor(((howManyBlocks - previousBlocks) * BLOCK_HEIGHT), speed, timeoutS);
        } else {
            //the current and previous blocks are the same, do nothing
        }
        previousBlocks = howManyBlocks;
    }

    public void teleOpEncoderDrive() {
        runtime.reset();

        if (liftEncoderSetpoint < uhaulLift.getCurrentPosition()) {

            uhaulLift.setTargetPosition(liftEncoderSetpoint);
            uhaulLiftTwo.setTargetPosition(liftEncoderSetpoint);

            uhaulLift.setPower(-LIFT_MAX_SPEED);
            uhaulLiftTwo.setPower(-LIFT_MAX_SPEED);

            uhaulLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            while ((gamepad2.right_stick_y < 0.1) && opModeIsActive() &&
                    (runtime.seconds() < 15) && (uhaulLift.isBusy() && uhaulLiftTwo.isBusy())) {

                telemetry.addData("Lift", " Position: %7d", uhaulLift.getCurrentPosition());
                telemetry.update();
            }

            uhaulLift.setPower(0);
            uhaulLiftTwo.setPower(0);

            uhaulLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        } else if (liftEncoderSetpoint > uhaulLift.getCurrentPosition()) {
            runtime.reset();

            if (liftEncoderSetpoint < uhaulLift.getCurrentPosition()) {

                uhaulLift.setTargetPosition(liftEncoderSetpoint);
                uhaulLiftTwo.setTargetPosition(liftEncoderSetpoint);

                uhaulLift.setPower(LIFT_MAX_SPEED);
                uhaulLiftTwo.setPower(LIFT_MAX_SPEED);

                uhaulLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                while ((gamepad2.right_stick_y < 0.1) && opModeIsActive() &&
                        (runtime.seconds() < 15) && (uhaulLift.isBusy() && uhaulLiftTwo.isBusy())) {

                    telemetry.addData("Lift", " Position: %7d", uhaulLift.getCurrentPosition());
                    telemetry.update();
                }

                uhaulLift.setPower(0);
                uhaulLiftTwo.setPower(0);

                uhaulLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
    }


    /** Initializes the proccess for the autonomous */
    @Override
    public void initAutonomous() {

        uhaulLift = (DcMotorEx) hardwareMap.dcMotor.get(UHAUL_LIFT_1);
        uhaulLiftTwo = (DcMotorEx) hardwareMap.dcMotor.get(UHAUL_LIFT_2);

        PIDFCoefficients pidNew = new PIDFCoefficients(kp, ki, kd, kf);
        uhaulLift.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);
        uhaulLiftTwo.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidNew);

        uhaulLift.setDirection(DcMotor.Direction.FORWARD);
        uhaulLiftTwo.setDirection(DcMotor.Direction.FORWARD);

        uhaulLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        uhaulLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        uhaulLiftTwo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }


    /** Defines the targets */
    public void liftFor(double distance_inches, double speed, double timeoutS) {
        //CURRENTLY ONLY USING 1 OUT OF 4 ENCODERS, COULD BE MADE MORE ACCURATE!

        uhaulLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        uhaulLiftTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        uhaulLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        uhaulLiftTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runtime.reset();


        //runUsingEncoders();

        //System.out.println("RUNUSINGENCODERS COMPLETE!");

        if (opModeIsActive()) {
            //double speedWeWant = betterDrive(speed);

            int targetDistLeft;
            int targetDistRight;
            targetDistLeft = uhaulLift.getCurrentPosition() + (int) (distance_inches * COUNTS_PER_INCH);
            targetDistRight = uhaulLiftTwo.getCurrentPosition() + (int) (distance_inches * COUNTS_PER_INCH);

          /*  leftDriveFront.setTargetPosition(targetDistLeft);
            rightDriveFront.setTargetPosition(targetDistRight);
            leftDriveBack.setTargetPosition(targetDistLeft);
            rightDriveBack.setTargetPosition(targetDistRight);

            System.out.println("SET TARGET POSITIONS");

            leftDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            System.out.println("SET MODE RUN TO POSITION");
*/


            runtime.reset();

            // drive(speedWeWant);
            //drive(speed);
            System.out.println("DRIVING AT THAT SPEED");

            uhaulLift.setPower(speed);
            uhaulLiftTwo.setPower(speed);


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    ((Math.abs((uhaulLift.getCurrentPosition() + uhaulLiftTwo.getCurrentPosition()) / 2)) < (Math.abs((distance_inches * COUNTS_PER_INCH))))) {
                telemetry.addData("UhaulLift",  " Position: %7d", uhaulLift.getCurrentPosition());
                telemetry.addData("UhaulLift 2",  " Position: %7d", uhaulLiftTwo.getCurrentPosition());

                telemetry.update();
            }

        }
        uhaulLift.setPower(0);
        uhaulLiftTwo.setPower(0);

        System.out.println("ROBOT STOPPED");



    }

    public double maxLeft, maxRight, max, ratio;

    public double[] normalizeAuto(double wheelspeeds0, double wheelspeeds1, double wheelspeeds2, double wheelspeeds3){
        /*
         * Are any of the computed wheel powers greater than 1?
         */

        if(Math.abs(wheelspeeds0) > 1
                || Math.abs(wheelspeeds1) > 1
                || Math.abs(wheelspeeds2) > 1
                || Math.abs(wheelspeeds3) > 1)
        {

            // Yeah, figure out which one

            maxLeft = Math.max(Math.abs(wheelspeeds0), Math.abs(wheelspeeds2));
            maxRight = Math.max(Math.abs(wheelspeeds1), Math.abs(wheelspeeds3));
            max = Math.max(maxLeft, maxRight);
            ratio = 1 / max; //Create a ratio to normalize them all
            double[] normalSpeeds = {(wheelspeeds0 * ratio), (wheelspeeds1 * ratio), (wheelspeeds2 * ratio), (wheelspeeds3 * ratio)};
            //leftDriveBack.setPower(wheelspeeds0 * ratio);
            //rightDriveBack.setPower(wheelspeeds1 * ratio);
            //leftDriveFront.setPower(wheelspeeds2 * ratio);
            //rightDriveFront.setPower(wheelspeeds3 * ratio);
            return normalSpeeds;
        }
        /*
         * Nothing we need to do to the raw powers
         */

        else
        {
            // leftDriveBack.setPower(wheelspeeds0);
            //  rightDriveBack.setPower(wheelspeeds1);
            // leftDriveFront.setPower(wheelspeeds2);
            // rightDriveFront.setPower(wheelspeeds3);
            double[] normalSpeedz = {(wheelspeeds0), (wheelspeeds1), (wheelspeeds2), (wheelspeeds3)};
            return normalSpeedz;
        }
    }
}

