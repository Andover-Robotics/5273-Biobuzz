package org.firstinspires.ftc.teamcode.pedro.opmodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.auto.utils.BotActions;
import org.firstinspires.ftc.teamcode.auto.utils.Hardware;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.pedro.paths.BasicRed;
import org.firstinspires.ftc.teamcode.pedro.util.PedroLocalizer;
import org.firstinspires.ftc.teamcode.pedro.util.RRCmd;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name = "Basic Red")
public class BasicRedAuto extends OpMode {

    private Follower follower;
    private BasicRed paths;
    private BotActions actions;
    private static final boolean isFarShooting = false;

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        paths = new BasicRed();

        actions = new BotActions(new Hardware(PedroLocalizer.toRR(paths.getStartPose()), hardwareMap, telemetry, isFarShooting), telemetry, this);

        follower.setPose(paths.getStartPose());
        follower.update();

        telemetry.addLine("Basic Red Initialized");
        telemetry.update();
    }

    private Command autoRoutine() {
        return sequential(
            follow(follower, paths.path2()),
            follow(follower, paths.path3()),
            follow(follower, paths.path4()),
            follow(follower, paths.path5()),
            follow(follower, paths.path6()),
            follow(follower, paths.path7()),
            follow(follower, paths.path8()),
            follow(follower, paths.path9()),
            follow(follower, paths.startShoot0())
        );
    }

    @Override
    public void start() {
        schedule(race(RRCmd.run(actions.actionPeriodic(isFarShooting)), autoRoutine()));
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData(
            "Heading",
            Math.toDegrees(follower.pose().heading())
        );
        telemetry.addData("Follower Mode", follower.mode());

        telemetry.update();
    }

    @Override
    public void stop() {
        Scheduler.reset();
    }
}
