package org.firstinspires.ftc.teamcode.TANK;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="TankTeleOp", group="Tank")

public class TankTeleOp extends TankLinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void run()
    {
        waitForStart();

        runtime.reset();
        telemetry.addData("Runtime Reset", "Complete");

        while (opModeIsActive())
        {
            robot.teleOpActivated();

        }
    }
}
