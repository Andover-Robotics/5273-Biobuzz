package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Outtake;
import org.firstinspires.ftc.teamcode.subsystems.Storage;

//only test shooting
@Utility(name="intake transfer test")
public class IntakeTransferBot extends OpMode {
    private Intake intake;
    private Storage storage;
    private GamepadEx gamepad;

    public static double triggerDeadzone = .05;

    public void init() {
        intake = new Intake(hardwareMap);
        storage = new Storage(hardwareMap, intake);
        gamepad = new GamepadEx(gamepad1);

        intake.stop();
        storage.stopTransfer();
    }

    public void loop() {
        gamepad.readButtons();


        if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > triggerDeadzone) {
            storage.runTransfer();
        }
        else if (gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > triggerDeadzone) {
            storage.runTransferBackwards();
        }
        else {
            storage.stopTransfer();
        }
        if (gamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            intake.runSlow();
        }
        else if (gamepad.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            intake.runBackwards();
        }
        else if (gamepad.getButton(GamepadKeys.Button.A)) {
            intake.run();
        }
        else {
            intake.stop();

        }

        telemetry.addData("Current: ", storage.getCurrent());
        telemetry.update();
    }

    public void stop() {
        intake.stop();
        storage.stopTransfer();
    }
}
