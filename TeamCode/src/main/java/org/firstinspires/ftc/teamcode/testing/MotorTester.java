package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Map;

/**
 * Manually tests each configured DC motor.
 */
@Utility(name = "Motor Tester")
public class MotorTester extends LinearOpMode {
    private static final double POWER_STEP = 0.01;

    @Override
    public void runOpMode() {
        String[] motorNames = hardwareMap.dcMotor.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        if (motorNames.length == 0) {
            telemetry.addLine("No DC motors are configured.");
            telemetry.update();
            return;
        }

        waitForStart();
        int selectedMotor = 0;
        DcMotor motor = hardwareMap.get(DcMotor.class, motorNames[selectedMotor]);
        double power = 0;
        boolean dpadRightWasPressed = false;
        boolean dpadLeftWasPressed = false;

        try {
            while (opModeIsActive()) {
                if (gamepad2.dpad_right && !dpadRightWasPressed) {
                    motor.setPower(0);
                    selectedMotor = (selectedMotor + 1) % motorNames.length;
                    motor = hardwareMap.get(DcMotor.class, motorNames[selectedMotor]);
                    power = 0;
                } else if (gamepad2.dpad_left && !dpadLeftWasPressed) {
                    motor.setPower(0);
                    selectedMotor = (selectedMotor + motorNames.length - 1) % motorNames.length;
                    motor = hardwareMap.get(DcMotor.class, motorNames[selectedMotor]);
                    power = 0;
                }

                if (gamepad2.dpad_up) {
                    power = Math.min(1.0, power + POWER_STEP);
                } else if (gamepad2.dpad_down) {
                    power = Math.max(-1.0, power - POWER_STEP);
                }
                if (gamepad2.x) {
                    power = 0;
                }

                motor.setPower(power);
                dpadRightWasPressed = gamepad2.dpad_right;
                dpadLeftWasPressed = gamepad2.dpad_left;

                telemetry.addLine("Controller 2");
                telemetry.addLine("Left/right: select motor");
                telemetry.addLine("Up/down: change power");
                telemetry.addLine("X: stop motor");
                telemetry.addData("Selected motor", motorNames[selectedMotor]);
                telemetry.addData("Power", "%.2f", power);
                telemetry.addData("Encoder", motor.getCurrentPosition());
                telemetry.update();
            }
        } finally {
            motor.setPower(0);
        }
    }
}
