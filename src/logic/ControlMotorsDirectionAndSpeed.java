package logic;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.wiringpi.SoftPwm;

/**
 * Controls motor speed and direction of two DC motors
 * @author https://javatutorial.net
 */
public class ControlMotorsDirectionAndSpeed {
    private static int MOTOR_1_PIN_A = 1;
    private static int MOTOR_1_PIN_B = 5;
    private final GpioController gpio = GpioFactory.getInstance();

    public ControlMotorsDirectionAndSpeed() {
        SoftPwm.softPwmCreate(MOTOR_1_PIN_A, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_1_PIN_B, 0, 100);
    }

    public void motorDirectionAndSpeed(boolean pinA, boolean pinB, int speed) {

        //move forward
        if(pinA) {
            SoftPwm.softPwmWrite(MOTOR_1_PIN_B, 0);
            SoftPwm.softPwmWrite(MOTOR_1_PIN_A, speed);
            System.out.println("direction: forward, speed: " + speed);
        }
        //move backward
        if(pinB) {
            SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 0);
            SoftPwm.softPwmWrite(MOTOR_1_PIN_B, speed);
            System.out.println("direction: backward, speed: " + speed);
        }
        //stop motors
        if (!pinA && !pinB) {
            SoftPwm.softPwmWrite(MOTOR_1_PIN_B, 0);
            SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 0);
            System.out.println("stop motors");
        }

    }

    public static void main(String[] args) throws InterruptedException {
/*
        // get a handle to the GPIO controller
        final GpioController gpio = GpioFactory.getInstance();
        // init soft PWM pins
        // softPwmCreate(int pin, int value, int range)
        // the range is set like (min=0 ; max=100)
        SoftPwm.softPwmCreate(MOTOR_1_PIN_A, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_1_PIN_B, 0, 100);

        // init GPIO pins
//        final GpioPinDigitalOutput motor1pinE = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, "m1E");
//        final GpioPinDigitalOutput motor2pinE = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "m2E");
        System.out.println("rotate motor 1 clockwise at 15% speed for 5 seconds");
//        motor1pinE.high();
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 15);
        // wait 2 seconds
        Thread.sleep(5000);

        System.out.println("rotate motor 1 in opposite direction at 50% speed for 10 seconds");
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 0);
        SoftPwm.softPwmWrite(MOTOR_1_PIN_B, 50);
        // wait 3 seconds
        Thread.sleep(10000);
        // disable motor 1
        SoftPwm.softPwmWrite(MOTOR_1_PIN_B, 0);
//        motor1pinE.low();

        gpio.shutdown();
*/
//        ControlMotorsDirectionAndSpeed controlMotorsDirectionAndSpeed = new ControlMotorsDirectionAndSpeed();
//        controlMotorsDirectionAndSpeed.motorDirectionAndSpeed(true, false, 50);
//        Thread.sleep(5000);
//
//        controlMotorsDirectionAndSpeed.motorDirectionAndSpeed(false, true, 50);
//        Thread.sleep(5000);
//
//        controlMotorsDirectionAndSpeed.motorDirectionAndSpeed(false, false, 0);

    }
}