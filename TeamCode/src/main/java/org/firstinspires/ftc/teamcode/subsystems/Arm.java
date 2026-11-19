package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
@Config
//they be callign this wxyz arm cause of some other team but i think we should
// just call it the alphabet arm cause its got all those letters
public class Arm {
    public static double UP_POS = 0.0;
    public static double DOWN_POS = 1.0;

    private Servo armServo;

    public Arm(HardwareMap hardwareMap)
    {
        armServo = hardwareMap.get(Servo.class, "arm");
    }
    public void down()
    {
        armServo.setPosition(DOWN_POS);
    }
    public void up()
    {
        armServo.setPosition(UP_POS);
    }
}