
package org.firstinspires.ftc.teamcode.pedro.util;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.dashboard.FtcDashboard;

import com.pedropathing.ivy.Command;

public class RRCmd {
    // "Variable must be final or effectively final"
    static class RRCommandData {
        Action action;
        boolean running;
    }

    public static Command run(Action action) {
        RRCommandData data = new RRCommandData();

        return Command.build()
            .setStart(() -> {
                data.action = action;
                data.running = true;
            })

            .setExecute(() -> {
                TelemetryPacket packet = new TelemetryPacket();
                data.running = data.action.run(packet);
                FtcDashboard.getInstance()
                    .sendTelemetryPacket(packet);
            })
            .setDone(() -> !data.running)

            .setEnd(endCondition -> {
                data.action = null;
                data.running = false;
            });
    }
}
