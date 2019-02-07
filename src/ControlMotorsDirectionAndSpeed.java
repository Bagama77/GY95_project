import Interfaces.Motor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;
/**
 * Controls motor speed and direction of two DC motors
 * @author https://javatutorial.net
 */
public class ControlMotorsDirectionAndSpeed implements Motor{
    private static int MOTOR_1_PIN_A = 4;
    private static int MOTOR_1_PIN_B = 5;
    private static int MOTOR_2_PIN_A = 0;
    private static int MOTOR_2_PIN_B = 2;

    private GpioController gpioController;
    private GpioPinDigitalOutput motor1pinE;
    private GpioPinDigitalOutput motor2pinE;

    public ControlMotorsDirectionAndSpeed() {
        // get a handle to the GPIO controller
        this.gpioController = GpioFactory.getInstance();

        // init GPIO pins
        this.motor1pinE = this.gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_06, "m1E");
        this.motor2pinE = this.gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_03, "m2E");

        // init soft PWM pins
        // softPwmCreate(int pin, int value, int range)
        // the range is set like (min=0 ; max=100)
        SoftPwm.softPwmCreate(MOTOR_1_PIN_A, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_1_PIN_B, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_2_PIN_A, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_2_PIN_B, 0, 100);
    }

    @Override
    public void stopMotors() {
        SoftPwm.softPwmWrite(MOTOR_1_PIN_B, 0);
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 0);
        SoftPwm.softPwmWrite(MOTOR_2_PIN_B, 0);
        SoftPwm.softPwmWrite(MOTOR_2_PIN_A, 0);
        motor2pinE.low();
        motor1pinE.low();
    }

    @Override
    //rotate both motors forward
    public void bothMotorsForward(int speed) {
//        System.out.println("rotate motor 1 clockwise at 15% speed for 2 seconds");
        motor1pinE.high();
        motor2pinE.high();
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, speed);
        SoftPwm.softPwmWrite(MOTOR_2_PIN_A, speed);
    }

    @Override
    //rotate both motors backward
    public void bothMotorsBack(int speed) {
        motor1pinE.high();
        motor2pinE.high();
        SoftPwm.softPwmWrite(MOTOR_1_PIN_B, speed);
        SoftPwm.softPwmWrite(MOTOR_2_PIN_B, speed);
    }
}

/*
    public static void main(String[] args) throws InterruptedException {
        // get a handle to the GPIO controller
        final GpioController gpio = GpioFactory.getInstance();
        // init soft PWM pins
        // softPwmCreate(int pin, int value, int range)
        // the range is set like (min=0 ; max=100)
        SoftPwm.softPwmCreate(MOTOR_1_PIN_A, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_1_PIN_B, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_2_PIN_A, 0, 100);
        SoftPwm.softPwmCreate(MOTOR_2_PIN_B, 0, 100);
        // init GPIO pins
        final GpioPinDigitalOutput motor2pinE = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "m2E");
        final GpioPinDigitalOutput motor1pinE = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "m1E");
        System.out.println("rotate motor 1 clockwise at 15% speed for 2 seconds");
        motor1pinE.high();
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 15);
        // wait 2 seconds
        Thread.sleep(2000);
        System.out.println("rotate motor 1 clockwise at 60% speed for 2 seconds");
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 60);
        // wait 2 seconds
        Thread.sleep(2000);
        System.out.println("rotate motor 1 clockwise at full speed for 2 seconds");
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 100);
        // wait 2 seconds
        Thread.sleep(2000);
        System.out.println("rotate motor 1 in opposite direction at 50% speed for 3 seconds");
        SoftPwm.softPwmWrite(MOTOR_1_PIN_A, 0);
        SoftPwm.softPwmWrite(MOTOR_1_PIN_B, 50);
        // wait 3 seconds
        Thread.sleep(3000);
        // disable motor 1
        SoftPwm.softPwmWrite(MOTOR_1_PIN_B, 0);
        motor1pinE.low();
        System.out.println("rotate motor 2 clockwise at 30% speed for 2 seconds");
        motor2pinE.high();
        SoftPwm.softPwmWrite(MOTOR_2_PIN_A, 30);
        // wait 2 seconds
        Thread.sleep(2000);
        System.out.println("rotate motor 2 in opposite direction at 100% speed for 3 seconds");
        SoftPwm.softPwmWrite(MOTOR_2_PIN_A, 0);
        SoftPwm.softPwmWrite(MOTOR_2_PIN_B, 100);
        // wait 3 seconds
        Thread.sleep(3000);
        // disable motor 2
        SoftPwm.softPwmWrite(MOTOR_2_PIN_B, 0);
        motor2pinE.low();
        gpio.shutdown();
    }
 */

