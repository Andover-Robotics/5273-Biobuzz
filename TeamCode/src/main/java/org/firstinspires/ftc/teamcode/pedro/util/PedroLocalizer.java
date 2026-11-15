package org.firstinspires.ftc.teamcode.pedro.util;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.auto.roadrunner.miscRR.Localizer;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class PedroLocalizer implements Localizer {
    com.pedropathing.localization.Localizer localizer;
    public PedroLocalizer(HardwareMap hw, Pose2d pose) {
        Follower follower = Constants.create(hw);
        localizer = follower.localizer;
        setPose(pose);
    }
    public static Pose2d toRR(Pose pose) {
        return new Pose2d(pose.x(), pose.y(), pose.heading());
    }
    public static Pose toPedro(Pose2d pose) {
        return new Pose(pose.position.x, pose.position.y, pose.heading.log());
    }
    @Override
    public void setPose(Pose2d pose) {
        // Do they use the same coordinate convention? (They probably do)
        localizer.setPose(toPedro(pose));
    }

    @Override
    public Pose2d getPose() {
        Pose pose = localizer.pose();
        return toRR(pose);
    }

    @Override
    public PoseVelocity2d update() {
        localizer.update();
        Velocity vel = localizer.velocity();
        return new PoseVelocity2d(new Vector2d(vel.vx, vel.vy), vel.omega);
    }
}
