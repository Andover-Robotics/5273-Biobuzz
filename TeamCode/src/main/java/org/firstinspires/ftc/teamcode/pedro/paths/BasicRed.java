package org.firstinspires.ftc.teamcode.pedro.paths;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class BasicRed {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose startShoot = poseFactory.of(56, 8, 90);
    private final Pose point2Start = poseFactory.of(56, 8, 90);
    private final Pose prepIntake = poseFactory.of(24.6724, 23.9294, 240);
    private final Pose intakeStart = poseFactory.of(24.6724, 23.9294, 240);
    private final Pose intake = poseFactory.of(9.454, 6.6306, -90);
    private final Pose shoot2 = poseFactory.of(62.2042, 124.5585, -168.6861);
    private final Pose shoot2control = poseFactory.of(16.6293, 115.4402, 0);
    private final Pose prepFlower1 = poseFactory.of(47.2838, 125.951, 90);
    private final Pose flower1 = poseFactory.of(46.9099, 133.9372, 90);
    private final Pose shoot3 = poseFactory.of(37.1373, 120.1624, 90);
    private final Pose flower2PrepStart = poseFactory.of(37.1373, 120.1624, 90);
    private final Pose flower2Prep = poseFactory.of(17.1964, 46.7456, 180);
    private final Pose flower2 = poseFactory.of(6.3814, 46.5563, -178.9973);
    private final Pose finalShootStart = poseFactory.of(6.3814, 46.5563, 180);
    private final Pose finalShoot = poseFactory.of(16.4753, 7.4784, 90);


    public Pose getStartPose() {
    return start;
    }

    public Path startShoot() {
        return Paths.line(start, startShoot).linear(start, startShoot);
    }

    public Path path2() {
        return Paths.line(point2Start, prepIntake).linear(point2Start, prepIntake);
    }

    public Path path3() {
        return Paths.line(intakeStart, intake).linear(intakeStart, intake);
    }

    public Path path4() {
        return Paths.curve(intake, shoot2control, shoot2).reverseTangent();
    }

    public Path path5() {
        return Paths.line(shoot2, prepFlower1).constant(prepFlower1);
    }

    public Path path6() {
        return Paths.line(prepFlower1, flower1).constant(flower1);
    }

    public Path path7() {
        return Paths.line(flower1, shoot3).constant(shoot3);
    }

    public Path path8() {
        return Paths.line(flower2PrepStart, flower2Prep).linear(flower2PrepStart, flower2Prep);
    }

    public Path path9() {
        return Paths.line(flower2Prep, flower2).tangent();
    }

    public Path startShoot0() {
        return Paths.line(finalShootStart, finalShoot).linear(finalShootStart, finalShoot);
    }
}