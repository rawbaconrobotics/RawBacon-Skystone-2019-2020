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
public class BDGrabber extends RobotComponentImplBase{
    private final static String GRABBER_SERVO_NAME = "tank_grabber";
    double GRABBER_OPEN = 0.9;
    double GRABBER_CLOSED = 0.2;


    public static boolean grabberButton = false;


    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private Servo grabberServo = null;

    public BDGrabber(LinearOpMode opMode) {
        super(opMode);
    }


    @Override
    public void init() {

        grabberServo = hardwareMap.servo.get(GRABBER_SERVO_NAME);

    }


    @Override
    public void initAutonomous() {

    }

    public void grabber() {



        // Wait for the game to start (driver presses PLAY)
        //waitForStart();

        // run until the end of the match (driver presses STOP)
        //while (opModeIsActive()) {

        // Setup a variable for each drive wheel to save power level for telemetry




        boolean openGrabber = gamepad1.right_bumper;
        boolean closeGrabber = gamepad1.left_bumper;


        if(openGrabber){
            grabberServo.setPosition(GRABBER_OPEN);
        }
        if (closeGrabber){
            grabberServo.setPosition(GRABBER_CLOSED);
        }



        // Show the elapsed game time and wheel power.

        //}

    }

    public void openGrabber(){
        System.out.println("GRABBER OPENING");
        grabberServo.setPosition(GRABBER_OPEN);
    }

    public void closeGrabber(){
        System.out.println("GRABBER CLOSING");
        grabberServo.setPosition(GRABBER_CLOSED);

    }
}
