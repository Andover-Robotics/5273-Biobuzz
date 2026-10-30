package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;

//only test shooting
@Config
public class ShooterBot {
    public static double targetRPM = 3000.0;
    public static double rpmStep = 50.0;
    public static double triggerDeadzone = 0.05;

    private final Outtake outtake;
    private final GamepadEx gamepad;
    private final Telemetry telemetry;

    public ShooterBot(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry) {
        outtake = new Outtake(hardwareMap, Outtake.Mode.RPM);
        this.gamepad = new GamepadEx(gamepad);
        this.telemetry = telemetry;
    }

    public void init() {
        outtake.stop();
    }

    public void tick() {
        gamepad.readButtons();

        if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
            targetRPM += rpmStep;
        } else if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
            targetRPM = Math.max(0.0, targetRPM - rpmStep);
        }

        if (gamepad.wasJustPressed(GamepadKeys.Button.A)) {
            outtake.stop();
        }

        boolean spinning = gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > triggerDeadzone;
        if (spinning) {
            outtake.set(targetRPM);
        } else {
            outtake.stop();
        }

        outtake.periodic();

        telemetry.addData("Shooter enabled", spinning);
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Measured RPM", outtake.getMeasuredRPM());
        telemetry.addData("Motor power", outtake.getPower());
        telemetry.update();
    }

    public void stop() {
        outtake.stop();
    }
}
