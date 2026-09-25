package org.firstinspires.ftc.teamcode.pedro;
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;


public class Tuning {
    // Tuners go here

    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }
}

