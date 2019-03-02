import java.io.IOException;


import Sensors.ADXL345;
import Sensors.ITG3205;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.util.Console;

public class Main {

    // GY-85 I2C addresses
    public static final int ADXL345_ADDR = 0x53;
    public static final int ITG3205_ADDR = 0x68;

    public static void main(String[] args) throws InterruptedException, IOException, UnsupportedBusNumberException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();
        // print program title/header
        console.title("<-- The GY95 Project -->", "I2C Example");
        // allow for user to exit program using CTRL-C
        console.promptForExit();

        //--------------Sensors.ADXL345----------------------------------------------------------------
        ADXL345 adxl345 = new ADXL345();
        //--------------Sensors.ITG3205----------------------------------------------------------------
        ITG3205 itg3205 = new ITG3205();

        //--------------------------MAIN CYCLE--------------------------------
        while(true){
            //------------------Sensors.ADXL345------------------------------------
            adxl345.readRegisters();
            int xAccl = adxl345.getxAccl();
            int yAccl = adxl345.getyAccl();
            int zAccl = adxl345.getzAccl();

            //--------------Sensors.ITG3205--------------------------------
            itg3205.readRawRotations();
            float rX = itg3205.getR()[0];
            float rY = itg3205.getR()[1];
            float rZ = itg3205.getR()[2];
            //--------------print in console-----------------------
            System.out.printf("ADXL: X-Axis = %d, Y-Axis = %d, Y-Axis = %d /r/n", xAccl, yAccl, zAccl);
            System.out.printf("ITG3205: x= %f, y=%f, z=%f deg/s /r/n", rX, rY, rZ);
            Thread.sleep(500);
        }

    }
}

