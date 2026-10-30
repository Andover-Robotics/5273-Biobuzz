package org.firstinspires.ftc.teamcode.testing;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

import org.firstinspires.ftc.teamcode.subsystems.Turret;

@Utility(name="turret tester")
public class TurretTester extends LinearOpMode {
    @Override
    public void runOpMode() {
        Turret turret = new Turret(hardwareMap);
        waitForStart();
        GamepadEx gamepad = new GamepadEx(gamepad1);
        while (opModeIsActive()) {
            telemetry.addData("turret position", turret.getCurrentAngle());
            telemetry.update();
        }
    }
}
