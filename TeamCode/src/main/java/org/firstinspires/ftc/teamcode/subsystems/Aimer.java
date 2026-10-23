package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.auto.roadrunner.miscRR.MecanumDrive;


@Config
public class Aimer {
    //bottom left of field is (0,0) (red human player), top right (near red goal) is (144,144)
    //bottom right of field is (144,0) (blue human player), top left (near blue goal) is (0,144)
    //the bot is relocalized at the respective human player zone

    public static double kP = 0.0367;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0;
    public static double filter = 0.867;  // smoothing factor (1 = no filtering, 0 = very heavy smoothing)
    public static double maxIntegral = 1.0;
    public static double deadband = 1;
    private double integral = 0;
    private double lastDerivative = 0.0;
    private double lastError = 0;
    private long lastTimestamp = 0;
    private final MecanumDrive drive;
    private final Outtake outtake;

    public static boolean USE_FLIGHT_TIME_LUT = true;

    public static double latencyFlightTime = 0; // should be 0 latency should be handled by LUT ; seconds
    public static double latencyRobotPose = 0; // age of localization data; seconds
    public static int MAX_GOAL_ITERATION = 5; // maximum iterations of goal adjustment


    public static Pose2d targetPoseForRpmLeft = new Pose2d(60, 58, Math.toRadians(90));
    public static Pose2d targetPoseForRpmRight = new Pose2d(144-targetPoseForRpmLeft.position.x, 58, Math.toRadians(90));

    //for bearing
    public static Pose2d targetPoseForAimLeft = new Pose2d(60,58,Math.toRadians(90));
    public static Pose2d targetPoseForAimRight = new Pose2d(144- targetPoseForAimLeft.position.x,58, Math.toRadians(90));

    //for rpm power
    public static Pose2d targetPoseForRpm = targetPoseForRpmLeft;
    public static Pose2d targetPoseAim = targetPoseForAimLeft;


    public static double turretHeight = 11.0; // inches
    public static double targetHeight = 29.5; // inches

    // For Close Shooting
    //how far from the back of the field the aiming point is
    public static double goalBackAimClose = 13.0; // Only for aiming
    public static double goalBackForRpmClose = 7; // rpm depends on this // normal val: 8
    public static double goalBackAimFar = 14; // Only for aiming
    public static double goalBackForRpmFar = 10; // rpm depends on this // normal val: 8

    // For Far Shooting
    //how far from the side border of the field (where drivers stand) the aiming point is
    public static double goalOutAimClose = 20.0; //s only for aiming
    public static double goalOutForRpmClose = 10; // rpm depends on this // normal val: 15
    public static double goalOutAimFar = 10; // only for aiming
    public static double goalOutForRpmFar = 6; // rpm depends on this // normal val: 15

    public static double centerOfRotationOffsetY = -1.85; // in

    // Relative to center of rotation
    public static double turretOffsetY = -0.41; // in

    private boolean isFarShooting = false;

    public enum Goal {
        LEFT,
        RIGHT
    }

    public static Goal selectedGoal = Goal.RIGHT;

    public Aimer(MecanumDrive mecanumDrive, boolean isFarShooting, Outtake outtake) {
        this.drive = mecanumDrive;
        //defaults to red goal
        this.isFarShooting = isFarShooting;
        setRightTarget();
        this.outtake = outtake;
    }

    public void setLeftTarget() {
        selectedGoal = Goal.LEFT;

        targetPoseForRpm = targetPoseForRpmLeft;
        targetPoseAim = targetPoseForAimLeft;
    }

    public void setRightTarget(){
        selectedGoal = Goal.RIGHT;

        targetPoseForRpm = targetPoseForRpmRight;
        targetPoseAim = targetPoseForAimRight;

    }

    public Goal getGoal() {
        return selectedGoal;
    }

    public void localize(Pose2d pose) {
        drive.localizer.setPose(pose);
    }

    public void relocalizeForBack(){
        double botWidth = 14.8;
        double botLength = 17.0;
        // -2.25 because turret not centered
        if(selectedGoal == Goal.RIGHT){
            drive.localizer.setPose(new Pose2d(botWidth/2, botLength/2 + centerOfRotationOffsetY, Math.toRadians(90)));
        } else if (selectedGoal == Goal.LEFT){
            drive.localizer.setPose(new Pose2d(144-botWidth/2, botLength/2 + centerOfRotationOffsetY, Math.toRadians(90)));
        }
    }

    public void localizeForFront(){
        double botWidth = 14.8;
        double botLength = 17.0;
        // -2.25 because turret not centered
        if(selectedGoal == Goal.RIGHT){
            drive.localizer.setPose(new Pose2d(110, 131, Math.toRadians(90)));
        } else if (selectedGoal == Goal.LEFT){
            drive.localizer.setPose(new Pose2d(34, 131, Math.toRadians(90)));
        }
    }

    public double[] calculateLocalizedData() {
        Pose2d robotPose = drive.localizer.getPose();
        double headingRadians = robotPose.heading.toDouble();

        double turretX = robotPose.position.x - (turretOffsetY * Math.sin(headingRadians));
        double turretY = robotPose.position.y + (turretOffsetY * Math.cos(headingRadians));

        double dxForRpm = targetPoseForRpm.position.x - turretX;
        double dyForRpm = targetPoseForRpm.position.y - turretY;

        double dxAim = targetPoseAim.position.x - turretX;
        double dyAim = targetPoseAim.position.y - turretY;

        double horizontalDistanceForRpm = Math.hypot(dxForRpm, dyForRpm);

        double dz = targetHeight - turretHeight;

        double currentHeading = robotPose.heading.toDouble();

        // point-to-point distance, only used for outtake rpm
        double range = Math.hypot(horizontalDistanceForRpm, dz);

        // only used for aim
        double desiredHeading = Math.atan2(dyAim, dxAim);


        double bearing = Math.toDegrees(desiredHeading - currentHeading);
        bearing = angleWrapDegrees(bearing);

        double turnPower = -calculateTurnPowerFromBearing(bearing);
        return new double[]{turnPower, range, bearing, Math.toDegrees(desiredHeading)};
    }

    public double calculateRangeGivenPose(double xPos, double yPos, double headingRadians) {
        Pose2d givenPose = new Pose2d(xPos, yPos, headingRadians);

        double turretX = givenPose.position.x - (turretOffsetY * Math.sin(headingRadians));
        double turretY = givenPose.position.y + (turretOffsetY * Math.cos(headingRadians));

        double dxForRpm = targetPoseForRpm.position.x - turretX;
        double dyForRpm = targetPoseForRpm.position.y - turretY;

        double horizontalDistanceForRpm = Math.hypot(dxForRpm, dyForRpm);

        double dz = targetHeight - turretHeight;

        // point-to-point distance
        double range = Math.hypot(horizontalDistanceForRpm, dz);

        return range;
    }

    /*
    Old bad version?

    private double angleWrapDegrees(double angle) {
        return (angle + 180) % 360 - 180;
    }*/

    private double angleWrapDegrees(double angle) {
        return ((angle % 360.0) + 540.0) % 360.0 - 180.0;
    }

    public double calculateTurnPowerFromBearing(double bearing) {
        // If apriltag lost - reset PID state and return no correction
        if (Double.isNaN(bearing)) {
            integral = 0;
            lastError = 0;
            lastDerivative = 0;
            lastTimestamp = 0;
            return 0.0;
        }

        double error = angleWrapDegrees(bearing);
        if (Math.abs(error) < deadband) {
            integral = 0;
            lastError = error;
            lastDerivative = 0;
            return 0;
        }

        // Time diff in seconds
        long currentTime = System.currentTimeMillis();
        double deltaTime;

        if (lastTimestamp == 0) {
            deltaTime = 0.01;  // assume 10 ms on first loop
        } else {
            deltaTime = (currentTime - lastTimestamp) / 1000.0;
        }
        lastTimestamp = currentTime;

        // Don't consider > 100 ms or < 5ms
        deltaTime = Math.max(Math.min(deltaTime, 0.1), .005);

        // Integral accumulation
        integral += error * deltaTime;
        integral = Math.max(-maxIntegral, Math.min(maxIntegral, integral));

        // Derivative with smoothing
        double rawDerivative = (error - lastError) / deltaTime;
        double derivative = (1 - filter) * lastDerivative + filter * rawDerivative;
        lastDerivative = derivative;
        lastError = error;

        // Gets proper direction
        double feedforward = Math.signum(error) * kF;

        // output
        double power = kP * error + kI * integral + kD * derivative + feedforward;

        return Math.max(-1, Math.min(1, power));
    }


    /*
     * Predicts the robot's pose due to latency and a 5-iteration estimations to find range and bearing
     * using time-of-flight and robot velocity
     * robotVel: in/sec; robotVelAngular : rad/sec;
     */
    public double[] calculateLocalizedDataWithVelocityComp(double robotVelX, double robotVelY, double robotVelAngular) {
        Pose2d robotPose = drive.localizer.getPose();

        // Compensate for latency by projecting robot pose forward
        double predictedX = robotPose.position.x + (robotVelX * latencyRobotPose);
        double predictedY = robotPose.position.y + (robotVelY * latencyRobotPose);
        double currentHeading = robotPose.heading.toDouble();
        double predictedHeading = currentHeading + (robotVelAngular * latencyRobotPose);

        // turret offset relative
        double turretOffsetXField = -turretOffsetY * Math.sin(predictedHeading);
        double turretOffsetYField = turretOffsetY * Math.cos(predictedHeading);

        double turretX = predictedX + turretOffsetXField;
        double turretY = predictedY + turretOffsetYField;

        double tanVx = -robotVelAngular * turretOffsetYField;
        double tanVy = robotVelAngular * turretOffsetXField;

        double turretBaseVelX = robotVelX + tanVx;
        double turretBaseVelY = robotVelY + tanVy;

        // Iterative Virtual Goal Convergence Loop
        double virtualTargetAimX = targetPoseAim.position.x;
        double virtualTargetAimY = targetPoseAim.position.y;
        double virtualTargetRpmX = targetPoseForRpm.position.x;
        double virtualTargetRpmY = targetPoseForRpm.position.y;

        int i = 0;
        double range = 0.0;
        double finalTimeOfFlight = 0.0;

        while (true) {
            // Calculate range dynamically using the current iterative virtual RPM target
            double dxRpm = virtualTargetRpmX - turretX;
            double dyRpm = virtualTargetRpmY - turretY;

            double horizontalDistanceForRpm = Math.hypot(dxRpm, dyRpm);
            double dz = targetHeight - turretHeight;

            range = Math.hypot(horizontalDistanceForRpm, dz);

            double timeOfFlight = 0;
            if (USE_FLIGHT_TIME_LUT) {
                double targetRpm = outtake.getRegressionRPM(range);
                timeOfFlight = outtake.linearInterpolationFlightTime(targetRpm);
            }

            if (timeOfFlight <= 0.0) {
                break;
            }

            finalTimeOfFlight = latencyFlightTime + timeOfFlight;

            // Last goal iteration needs to keep time and range accurate
            if (i++ >= MAX_GOAL_ITERATION) break;

            // Shift Both targets opposite to the turret/robot velocity vector over total drift time
            virtualTargetAimX = targetPoseAim.position.x - (turretBaseVelX * finalTimeOfFlight);
            virtualTargetAimY = targetPoseAim.position.y - (turretBaseVelY * finalTimeOfFlight);

            virtualTargetRpmX = targetPoseForRpm.position.x - (turretBaseVelX * finalTimeOfFlight);
            virtualTargetRpmY = targetPoseForRpm.position.y - (turretBaseVelY * finalTimeOfFlight);
        }

        // Final Distance and Heading Calculations using the above
        double dxAim = virtualTargetAimX - turretX;
        double dyAim = virtualTargetAimY - turretY;

        // range is already finalized from the last iteration of the loop above
        double desiredHeading = Math.atan2(dyAim, dxAim);

        double bearing = Math.toDegrees(desiredHeading - predictedHeading);
        bearing = angleWrapDegrees(bearing);

        double turnPower = -calculateTurnPowerFromBearing(bearing);
        return new double[]{turnPower, range, bearing, Math.toDegrees(desiredHeading)};
    }
}
