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

    public static double p = 0.0015;
    public static double i = 0.0;
    public static double d = 0.0;
    public static double f = 0.000176;

    private static final double TPR = 28.0;
    private static final double MAX_RPM = 6000;
    private static final double RPM_INCREMENT = 100;

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
        telemetry.addLine("D-PAD UP: +100 RPM");
        telemetry.addLine("D-PAD DOWN: -100 RPM");
        telemetry.addLine("X: Reset RPM");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            gamepadEx.readButtons();

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.DPAD_UP)) {
                targetRPM += RPM_INCREMENT;
            }

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.DPAD_DOWN)) {
                targetRPM -= RPM_INCREMENT;
            }

            if (gamepadEx.wasJustPressed(
                    GamepadKeys.Button.X)) {
                targetRPM = 0;
            }

            targetRPM = Math.max(0,
                    Math.min(MAX_RPM, targetRPM));

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
            telemetry.addData("Average Error", "%.1f", averageError);
            telemetry.addData("Maximum Error", "%.1f", maxError);
            telemetry.addData("Motor Power", "%.3f", motorPower);
            telemetry.addData("Samples", samples);

            telemetry.addLine("--- PIDF ---");
            telemetry.addData("P", "%.5f", p);
            telemetry.addData("I", "%.5f", i);
            telemetry.addData("D", "%.5f", d);
            telemetry.addData("F", "%.6f", f);

            telemetry.update();
        }
    }
}