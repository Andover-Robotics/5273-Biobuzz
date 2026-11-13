
package org.firstinspires.ftc.teamcode.pedro.paths;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import org.firstinspires.ftc.teamcode.pedro.paths.BasicRed;

@Autonomous(name = "Basic Red")
public class BasicRedAuto extends OpMode {

    private Follower follower;
    private BasicRed paths;

    private int pathState = 0;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        paths = new BasicRed();

        follower.setPose(paths.getStartPose());
        follower.update();

        telemetry.addLine("Basic Red Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        pathState = 1;
        follower.follow(paths.path2());
    }

    @Override
    public void loop() {
        follower.update();

        if (!follower.isBusy() && pathState > 0 && pathState < 10) {

            switch (pathState) {

                case 1:
                    follower.follow(paths.path3());
                    pathState = 2;
                    break;

                case 2:
                    follower.follow(paths.path4());
                    pathState = 3;
                    break;

                case 3:
                    follower.follow(paths.path5());
                    pathState = 4;
                    break;

                case 4:
                    follower.follow(paths.path6());
                    pathState = 5;
                    break;

                case 5:
                    follower.follow(paths.path7());
                    pathState = 6;
                    break;

                case 6:
                    follower.follow(paths.path8());
                    pathState = 7;
                    break;

                case 7:
                    follower.follow(paths.path9());
                    pathState = 8;
                    break;

                case 8:
                    follower.follow(paths.startShoot0());
                    pathState = 9;
                    break;

                case 9:
                    follower.stop();
                    pathState = 10;
                    break;
            }
        }

        telemetry.addData("State", pathState);
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData(
            "Heading",
            Math.toDegrees(follower.pose().heading())
        );
        telemetry.addData("Busy", follower.isBusy());
        telemetry.update();
    }
}
