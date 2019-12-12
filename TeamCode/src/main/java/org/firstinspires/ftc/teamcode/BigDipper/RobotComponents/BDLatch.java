/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.BigDipper.RobotComponents;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Tank Contrller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public class BDLatch extends RobotComponentImplBase {

    private final static String LATCH_SERVO_NAME = "tank_latch_1" ;
    //private final static String LATCH_SERVO_2_NAME = "tank_latch_2";
    double LATCH_OPEN = 0.3;
    double LATCH_CLOSED = 0;


    public static boolean latchButton = false;


    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Servo latchServo = null;
    //private Servo latchServo2 = null;

    public BDLatch(LinearOpMode opMode) {
        super(opMode);
    }


    @Override
    public void init() {

        latchServo = hardwareMap.servo.get(LATCH_SERVO_NAME);
      //  latchServo2 = hardwareMap.servo.get(LATCH_SERVO_2_NAME);

    }
    public void initAutonomous(){

        latchServo = hardwareMap.servo.get(LATCH_SERVO_NAME);
        //latchServo2 = hardwareMap.servo.get(LATCH_SERVO_2_NAME);

        latchServo.setPosition(LATCH_OPEN);
        //latchServo2.setPosition(LATCH_CLOSED);
    }


    public void latch() {



        // Wait for the game to start (driver presses PLAY)
        //waitForStart();

        // run until the end of the match (driver presses STOP)
        //while (opModeIsActive()) {

        // Setup a variable for each drive wheel to save power level for telemetry




        boolean openLatch = gamepad2.y;
        boolean closeLatch = gamepad2.x;


            if(openLatch){
                latchServo.setPosition(LATCH_OPEN);
                //latchServo2.setPosition(LATCH_CLOSED);
            }
            if (closeLatch){
                latchServo.setPosition(LATCH_CLOSED);
                //latchServo2.setPosition(LATCH_OPEN);
            }



        // Show the elapsed game time and wheel power.

        //}

    }

    public void openLatch(){
        System.out.println("LATCH OPENING");
        latchServo.setPosition(LATCH_OPEN);
        //latchServo2.setPosition(LATCH_CLOSED);
    }

    public void closeLatch(){
        System.out.println("LATCH CLOSING");
        latchServo.setPosition(LATCH_CLOSED);
        //latchServo2.setPosition(LATCH_OPEN);
    }
    public void closeLatchBIGNUMBER(){
        System.out.println("LATCH CLOSING");
        //latchServo2.setPosition(LATCH_OPEN);
    }
    public void openLatchBIGNUMBER(){
        System.out.println("LATCH CLOSING");
        //latchServo2.setPosition(LATCH_OPEN);
    }
}



