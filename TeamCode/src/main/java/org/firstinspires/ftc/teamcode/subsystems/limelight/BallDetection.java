package org.firstinspires.ftc.teamcode.subsystems.limelight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

import javax.annotation.Nullable;

public class BallDetection {
    private final Limelight3A limelight;
    // Height above the ground in inches
    private static final double CAMERA_HEIGHT = 5;
    // Note that this is positive when pointing downward, in degrees
    private static final double CAMERA_MOUNTING_ANGLE = 0;
    // Area in percentage of total image
    private static final double AREA_THRESHOLD = 0;
    // Radius of pollen in inches
    private static final double POLLEN_RADIUS = 1;

    BallDetection(HardwareMap map) {
        limelight = map.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(4);
        limelight.start();
    }

    // Robot-centric distance, assumes camera points in the same direction as +y
    @Nullable
    double[] getDetection() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            LLResultTypes.DetectorResult dresult = getDetectorResult(result);

            if (dresult != null) {
                double x = Math.toRadians(dresult.getTargetXDegrees());
                double y = dresult.getTargetYDegrees();

                // Downward angle
                double vertAngle = CAMERA_MOUNTING_ANGLE - y;
                // tan(vertAngle) = height / hor
                double horDistance = (CAMERA_HEIGHT - POLLEN_RADIUS) / Math.tan(Math.toRadians(vertAngle));

                return new double[]{-Math.sin(x) * horDistance, Math.cos(x) * horDistance};
            }
        }
        return null;
    }

    @Nullable
    private LLResultTypes.DetectorResult getDetectorResult(LLResult result) {
        List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();

        double large = AREA_THRESHOLD;
        LLResultTypes.DetectorResult dresult = null;
        for (LLResultTypes.DetectorResult detection: detections) {
            if (!detection.getClassName().equals("yellow_pollen")) continue;
            double area = detection.getTargetArea();
            if (area > large) {
                dresult = detection;
                large = area;
            }
        }
        return dresult;
    }
}
