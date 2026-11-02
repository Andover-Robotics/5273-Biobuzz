package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.auto.roadrunner.miscRR.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Aimer;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

@TeleOp(name = "Turret Auto Aim Tester", group = "Testing")
public class TurretAutoAimTester extends LinearOpMode {
    private static final double MANUAL_DEADZONE = 0.1;
    private static final double MANUAL_SPEED_DEGREES = 4.0;

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(
                hardwareMap,
                new Pose2d(0, 0, Math.toRadians(0))
        );
        Turret turret = new Turret(hardwareMap);
        Aimer aimer = new Aimer(drive, false, null);
        GamepadEx gamepad = new GamepadEx(gamepad1);

        turret.initialize();
        aimer.setRightTarget();

        boolean autoAim = false;
        double manualServoAngle = Math.min(
                turret.getActualRangeOfMotion(),
                180 + turret.getServoOffset()
        );
        double[] aimData = {0, 0, 0, 0};

        waitForStart();
        while (opModeIsActive()) {
            gamepad.readButtons();
            drive.updatePoseEstimate();

            if (gamepad.wasJustPressed(GamepadKeys.Button.A)) {
                autoAim = !autoAim;
            }
            if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                aimer.setLeftTarget();
            }
            if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                aimer.setRightTarget();
            }

            if (autoAim) {
                aimData = aimer.calculateLocalizedData();
                turret.rotate(aimData[2]);
            } else {
                double stick = gamepad.getRightX();
                if (Math.abs(stick) > MANUAL_DEADZONE) {
                    manualServoAngle += stick * MANUAL_SPEED_DEGREES;
                    manualServoAngle = Math.max(
                            0,
                            Math.min(turret.getActualRangeOfMotion(), manualServoAngle)
                    );
                    turret.setServos(manualServoAngle);
                }
            }

            telemetry.addData("Mode", autoAim ? "AUTO AIM" : "MANUAL");
            telemetry.addData("Target", aimer.getGoal());
            telemetry.addData("Turret angle", turret.getCurrentAngle());
            telemetry.addData("Bearing", aimData[2]);
            telemetry.addData("Range", aimData[1]);
            telemetry.addData("Controls", "A: auto aim, right stick: manual, D-pad: target");
            telemetry.update();
        }
    }
}
