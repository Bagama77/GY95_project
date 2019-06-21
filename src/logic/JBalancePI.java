package logic;

import java.util.logging.Level;
import java.util.logging.Logger;


public class JBalancePI{

    private final Logger logger = Logger.getLogger("main");

    // These values must be adjusted for convenience
    private final float ACCEL_OFFSET = 3.7F;
    private final float GYRO_GAIN = 131F;
    private final float GYRO_OFFSET = 0.0F;

    //Set point values used in PID controller
    private final float KP = 17F;
    private final float KD = 840F;
    private final float KI = 0.1F;

    //For angle formula using complimentary filter
    private float angle_filtered = 0.0F;
    private final float PI = 3.1416F;

    //Last time in filter calculation
    long preTime = 0;
    //Last time in PID calculation
    private long lastTime = 0;

    //PID sum errors
    private float errSum = 0;
    //PID last error
    private float lastErr = 0;

    //instance of motors API
    private ControlMotorsDirectionAndSpeed motors = new ControlMotorsDirectionAndSpeed();
    private boolean pinA;//forward
    private boolean pinB;//backward
    private int speed;

    //>0 move motor forward, <0 move motor back
    private int LOutput, ROutput;

    //Define execution of read sensors thread
    private volatile boolean shouldRun = true;


    /*** Read data from accelerometer and gyroscope and apply a complimentary filter*/
    public float filter(int xAccel, int yAxxel, int zAxxel, float rX, float rY, float rZ) {
        //Data from MPU6050 Accelerometer and Gyroscope
        //Calculate angle and convert from radians to degrees
        float angle_raw = (float) (Math.atan2(yAxxel, zAxxel) * 180.00 / PI + ACCEL_OFFSET);
        float omega = (rX / GYRO_GAIN + GYRO_OFFSET);
        // Filters data to get the real value
        long now = System.currentTimeMillis();
        float dt = (float) ((now - preTime) / 1000.00);
        preTime = now;
        //Calculate error using complimentary filter
        float K = 0.8F;
        float A = K / (K + dt);
        //logger.log(Level.INFO, "A = " + A + ", omega = " + omega + ", dt = " + dt + ", angle_raw = " + angle_raw);
        angle_filtered = A * (angle_filtered + omega * dt) + (1 - A) * angle_raw;

        logger.log(Level.INFO,"filter: angle_filtered = " + angle_filtered);
        return angle_filtered;
    }

    /** Proportional, Integral, Derivative control.*/
    public void PID() {
        long now = System.currentTimeMillis();
        int timeChange = (int) (now - lastTime);
        lastTime = now;
        float error = angle_filtered;  // Proportion
        errSum += error * timeChange;  // Integration
        float dErr = (error - lastErr) / timeChange;  // Differentiation
        float output = KP * error + KI * errSum + KD * dErr;
        lastErr = error;
        LOutput = (int)output;// - Turn_Speed + Run_Speed;
//        ROutput = (int)output;// + Turn_Speed + Run_Speed;
        logger.log(Level.INFO,"PID: " + LOutput);// + ROutput);

        if(angle_filtered > 0) {
            pinA = true;
            pinB = false;
            speed = 45/100 * (int)angle_filtered;//(LOutput - 1167090000)/100;
        }

        if(angle_filtered < 0) {
            pinA = false;
            pinB = true;
            speed = 25;//45/100 * (int)(angle_filtered*-1);//((LOutput * -1) - 1167090000)/100;
        }

        motors.motorDirectionAndSpeed(pinA, pinB, 35);
    }
}

/*
 * PWM Motor control
 */
//    private void PWMControl() {
//        if (LOutput > 0) {
//            motor.moveL_Forward();
//        } else if (LOutput < 0) {
//            motor.moveL_Back();
//        } else {
//            motor.stopL();
//        }
//        if (ROutput > 0) {
//            motor.moveR_Forward();
//        } else if (ROutput < 0) {
//            motor.moveR_Back();
//        } else {
//            motor.stopR();
//        }
//        motor.motorL_PWM((short) (Math.min(4095, Math.abs(LOutput) * 4095 / 256)));
//        motor.motorR_PWM((short) (Math.min(4095, Math.abs(ROutput) * 4095 / 256)));
//
//    }

/*
 * Thread for move and balance robot
 */
//    class ControlLoop extends Thread {
//
//        @Override
//        public void run() {
//            while (shouldRun) {
//                filter();
//                Logger.getGlobal().log(Level.INFO, "Angle = " + angle_filtered);
//                // If angle > 45 or < -45 then stop the robot
//                if (Math.abs(angle_filtered) < 45) {
//                    PID();
//                    PWMControl();
//                } else {
//                    motor.stopL();
//                    motor.stopR();
//
//                    // Keep reading accelerometer and gyroscope values after falling down
//                    for (int i = 0; i < 100; i++) {
//                        filter();
//                    }
//
//                    if (Math.abs(angle_filtered) < 45) // Empty data and restart the robot automaticlly
//                    {
//                        for (int i = 0; i <= 500; i++) // Reset the robot and delay 2 seconds
//                        {
//                            angle_filtered = 0;
//                            filter();
//                            errSum = Run_Speed = Turn_Speed = 0;
//                            PID();
//                        }
//                    }
//
//                }
//            }
//            accel.close();
//            motor.close();
//        }
//    }