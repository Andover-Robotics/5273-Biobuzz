package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.Storage;

//only test shooting
@Config
public class IntakeTransferBot {

    private final Intake intake;
    private final Storage storage;
    private final GamepadEx gamepad;
    private final Telemetry telemetry;

    public static double triggerDeadzone = .05;

    public IntakeTransferBot(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry) {
        intake = new Intake(hardwareMap);
        storage = new Storage(hardwareMap, intake);
        this.gamepad = new GamepadEx(gamepad);
        this.telemetry = telemetry;
    }

    public void init() {
        intake.stop();
        storage.stopTransfer();
    }

    public void tick() {
        gamepad.readButtons();


        if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > triggerDeadzone) {
            intake.run();
            storage.runTransfer();
        }
        else if (gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > triggerDeadzone) {
            intake.runBackwards();
            storage.runTransferBackwards();
        }
        else if (gamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intake.runSlow();
        }
        else if (gamepad.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intake.runBackwards();
        }
        else {
            intake.stop();
            storage.stopTransfer();
        }

        telemetry.addData("Current: ", storage.getCurrent());
        telemetry.update();
    }

    public void stop() {
        intake.stop();
        storage.stopTransfer();
    }
}
