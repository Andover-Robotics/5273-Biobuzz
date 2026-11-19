package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Outtake;

@Config
@TeleOp(name = "Outtake Regression Test", group = "Testing")
public class RegressionTest extends LinearOpMode {

    private Outtake outtake;

    private GamepadEx gamepadEx;


    private static final double MAX_RPM = 6000;

    private double targetRPM = 0;
    private double measuredRPM = 0;
    private double motorPower = 0;

    private double totalError = 0;
    private double maxError = 0;
    private int samples = 0;

    @Override
    public void runOpMode() {

        outtake = new Outtake(hardwareMap, Outtake.Mode.RPM);

        gamepadEx = new GamepadEx(gamepad1);

        telemetry.addLine("OUTTAKE REGRESSION TEST");
        telemetry.addLine("D-PAD UP: +25 RPM");
        telemetry.addLine("D-PAD DOWN: -25 RPM");
        telemetry.addLine("D-PAD RIGHT: +200 RPM");
        telemetry.addLine("D-PAD LEFT: -100 RPM");
        telemetry.addLine("X: Reset RPM");
        
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            gamepadEx.readButtons();

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.DPAD_UP)) {
                targetRPM += 25;
            }

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.DPAD_DOWN)) {
                targetRPM -= 25;
            }

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.DPAD_RIGHT)) {
                targetRPM += 200;
            }

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.DPAD_LEFT)) {
                targetRPM -= 100;
            }

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.X)) {
                targetRPM = 0;
            }

            targetRPM = Math.max(0, Math.min(MAX_RPM, targetRPM));

            outtake.set(targetRPM);
            outtake.periodic();

            measuredRPM = outtake.getMeasuredRPM();
            motorPower = outtake.getPower();

            double error = Math.abs(targetRPM - measuredRPM);

            if (targetRPM > 0) {
                totalError += error;
                maxError = Math.max(maxError, error);
                samples++;
            }

            double averageError =
                    samples > 0 ? totalError / samples : 0;

            telemetry.addLine("===== OUTTAKE REGRESSION =====");

            telemetry.addLine("--- RPM ---");
            telemetry.addData("Target RPM", "%.0f", targetRPM);
            telemetry.addData("Actual RPM", "%.1f", measuredRPM);
            telemetry.addData("RPM Error", "%.1f", error);

            telemetry.addLine("--- PERFORMANCE ---");
            telemetry.addData("Motor Power", "%.4f", motorPower);
            telemetry.addData("Samples", samples);
            telemetry.update();
        }
    }
}