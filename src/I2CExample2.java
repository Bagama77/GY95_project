import java.io.IOException;


import Sensors.ITG3205;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.util.Console;

public class I2CExample2 {

    // GY-85 I2C addresses
    public static final int ADXL345_ADDR = 0x53;
    public static final int ITG3205_ADDR = 0x68;

    // ADXL345 registers
    public static final byte Power_Register = (byte) 0x2D;
    public static final byte DATAX0_REG = (byte)0x32;
    public static final byte DATAX1_REG = (byte)0x33;
    public static final byte DATAY0_REG = (byte)0x34;
    public static final byte DATAY1_REG = (byte)0x35;
    public static final byte DATAZ0_REG = (byte)0x36;
    public static final byte DATAZ1_REG = (byte)0x37;

    public static void main(String[] args) throws InterruptedException, IOException, UnsupportedBusNumberException {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate code)
        final Console console = new Console();
        // print program title/header
        console.title("<-- The Pi4J Project -->", "I2C Example");
        // allow for user to exit program using CTRL-C
        console.promptForExit();

        //--------------ADXL345-----------------------------------------------------------------
        // get the I2C bus to communicate on
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        // create an I2C device for an individual device on the bus that you want to communicate with
        I2CDevice device = i2c.getDevice(ADXL345_ADDR);
        // Select Bandwidth rate register
        // Normal mode, Output data rate = 100 Hz
        device.write(0x2C, (byte)0x0A);

        // Select Power control register
        // Auto-sleep disable
        device.write(Power_Register, (byte)0x08);//disable sleep mode

        // Select Data format register
        // Self test disabled, 4-wire interface, Full resolution, range = ±2g
        device.write(0x31, (byte)0x08);
        Thread.sleep(500);

        //--------------Sensors.ITG3205----------------------------------------------------------------
        // http://developer-blog.net/wp-content/uploads/2013/09/raspberry-pi-rev2-gpio-pinout.jpg
        // http://pi4j.com/example/control.html
        ITG3205 itg3205 = new ITG3205(I2CBus.BUS_1);

        itg3205.setup();
        if (!itg3205.verifyDeviceID()) {
            throw new IOException("Failed to verify Sensors.ITG3205 device ID");
        }

        // F_sample = F_internal / (divider + 1)
        // divider = F_internal / F_sample - 1
        itg3205.writeSampleRateDivider(2); // 2667 Hz
        itg3205.writeDLPFBandwidth(ITG3205.ITG3205_DLPF_BW_256);

        float scalingFactor = 1f / ITG3205.ITG3205_SENSITIVITY_SCALE_FACTOR;

        short[] raw = new short[3];
        float[] r = new float[3];

        //--------------------------MAIN CYCLE--------------------------------
        while(true){
            //--------------------ADXL345-------------------------------------
            // Read 6 bytes of data
            // xAccl lsb, xAccl msb, yAccl lsb, yAccl msb, zAccl lsb, zAccl msb
            byte[] data = new byte[6];
            data[0] = (byte)device.read(DATAX0_REG);
            data[1] = (byte)device.read(DATAX1_REG);
            data[2] = (byte)device.read(DATAY0_REG);
            data[3] = (byte)device.read(DATAY1_REG);
            data[4] = (byte)device.read(DATAZ0_REG);
            data[5] = (byte)device.read(DATAZ1_REG);

            // wait while the chip collects data
            Thread.sleep(500);

            // Convert the data to 10-bits
            int xAccl = ((data[1] & 0x03) * 256 + (data[0] & 0xFF));
            if(xAccl > 511){
                xAccl -= 1024;
            }

            int yAccl = ((data[3] & 0x03) * 256 + (data[2] & 0xFF));
            if(yAccl > 511){
                yAccl -= 1024;
            }

            int zAccl = ((data[5] & 0x03) * 256 + (data[4] & 0xFF));
            if(zAccl > 511){
                zAccl -= 1024;
            }

            //--------------Sensors.ITG3205--------------------------------
            itg3205.readRawRotations(raw);
            for (int i = 0; i < raw.length; i++) {
                r[i] = (float) raw[i] * scalingFactor;
            }

            //--------------print in console-----------------------
            System.out.printf("ADXL: X-Axis = %d, Y-Axis = %d, Y-Axis = %d ... Sensors.ITG3205: x= %f, y=%f, z=%f deg/s", xAccl, yAccl, zAccl, r[0], r[1], r[2]);
            Thread.sleep(500);
        }

    }
}

