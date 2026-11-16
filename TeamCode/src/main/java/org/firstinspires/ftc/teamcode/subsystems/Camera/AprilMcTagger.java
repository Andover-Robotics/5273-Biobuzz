package org.firstinspires.ftc.teamcode.subsystems.Camera;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;


public class AprilMcTagger {
    VisionPortal visionPortal;
    AprilTagProcessor aprilTagProcessor;
    HardwareMap hardwareMap;
    public AprilMcTagger(HardwareMap hardwareMap)
    {
        aprilTagProcessor = new AprilTagProcessor.Builder().setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getBioBuzzTagLibrary())
                .build();
        this.hardwareMap = hardwareMap;
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Camera"))
                .addProcessor(aprilTagProcessor)
                .build();
    }
}
