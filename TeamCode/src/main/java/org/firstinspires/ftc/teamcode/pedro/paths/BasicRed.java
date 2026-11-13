package org.firstinspires.ftc.teamcode.pedro.paths;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class BasicRed {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose path1 = poseFactory.of(56, 8, 90);
    private final Pose point2Start = poseFactory.of(56, 8, 90);
    private final Pose point2 = poseFactory.of(24.6724, 23.9294, 240);
    private final Pose point3Start = poseFactory.of(24.6724, 23.9294, 240);
    private final Pose point3 = poseFactory.of(9.454, 6.6306, -90);
    private final Pose point4 = poseFactory.of(62.2042, 124.5585, -168.6861);
    private final Pose point4Control1 = poseFactory.of(16.6293, 115.4402, 0);
    private final Pose point5 = poseFactory.of(47.2838, 125.951, 90);
    private final Pose point6 = poseFactory.of(46.9099, 133.9372, 90);
    private final Pose point7 = poseFactory.of(37.1373, 120.1624, 90);
    private final Pose point8Start = poseFactory.of(37.1373, 120.1624, 90);
    private final Pose point8 = poseFactory.of(17.1964, 46.7456, 180);
    private final Pose point9 = poseFactory.of(6.3814, 46.5563, -178.9973);
    private final Pose point10Start = poseFactory.of(6.3814, 46.5563, 180);
    private final Pose point10 = poseFactory.of(16.4753, 7.4784, 90);

    public Path path1() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);
    }

    public Path path3() {
        return Paths.line(point3Start, point3).linear(point3Start, point3);
    }

    public Path path4() {
        return Paths.curve(point3, point4Control1, point4).reverseTangent();
    }

    public Path path5() {
        return Paths.line(point4, point5).constant(point5);
    }

    public Path path6() {
        return Paths.line(point5, point6).constant(point6);
    }

    public Path path7() {
        return Paths.line(point6, point7).constant(point7);
    }

    public Path path8() {
        return Paths.line(point8Start, point8).linear(point8Start, point8);
    }

    public Path path9() {
        return Paths.line(point8, point9).tangent();
    }

    public Path path10() {
        return Paths.line(point10Start, point10).linear(point10Start, point10);
    }
}