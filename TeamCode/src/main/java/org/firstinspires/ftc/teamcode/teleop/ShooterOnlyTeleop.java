package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

//shooter only test
@Utility(name = "Shooter Only RPM Test")
public class ShooterOnlyTeleop extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        ShooterBot shooter = new ShooterBot(hardwareMap, gamepad1, telemetry);
        shooter.init();

        telemetry.addLine("RT: spin at target RPM");
        telemetry.addLine("D-pad up/down: adjust target RPM");
        telemetry.addLine("A: stop");
        telemetry.update();

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            shooter.tick();
        }

        shooter.stop();
    }
}
