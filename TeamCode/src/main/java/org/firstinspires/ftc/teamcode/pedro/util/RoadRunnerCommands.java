
package org.firstinspires.ftc.teamcode.pedro.commands;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.dashboard.FtcDashboard;

import com.pedropathing.ivy.Command;

import java.util.function.Supplier;

public class RoadRunnerCommands {

    public static Command run(Supplier<Action> actionFactory) {
        Action[] action = new Action[1];
        boolean[] running = {true};

        return Command.build()
            .setStart(() -> {
                // fresh ahh action
                action[0] = actionFactory.get();
                running[0] = true;
            })

            .setExecute(() -> {
                TelemetryPacket packet = new TelemetryPacket();
                running[0] = action[0].run(packet);
                FtcDashboard.getInstance()
                    .sendTelemetryPacket(packet);
            })
            .setDone(() -> !running[0])

            .setEnd(endCondition -> {
                action[0] = null;
            });
    }
}
