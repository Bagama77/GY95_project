package Sensors;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;


import java.io.IOException;

public class ADXL345 {

    // ADXL345 registers
    private static final int ADXL345_ADDR = 0x53;
    private static final byte Power_Register = (byte) 0x2D;
    private static final byte DATAX0_REG = (byte)0x32;
    private static final byte DATAX1_REG = (byte)0x33;
    private static final byte DATAY0_REG = (byte)0x34;
    private static final byte DATAY1_REG = (byte)0x35;
    private static final byte DATAZ0_REG = (byte)0x36;
    private static final byte DATAZ1_REG = (byte)0x37;

    private I2CDevice device;

    //result values
    private int xAccl;
    private int yAccl;
    private int zAccl;

    public ADXL345() throws IOException, I2CFactory.UnsupportedBusNumberException, InterruptedException{
        //--------------ADXL345-----------------------------------------------------------------
        // get the I2C bus to communicate on
        I2CBus i2c = GetBusClass.getBus();//I2CFactory.getInstance(I2CBus.BUS_1);
        // create an I2C device for an individual device on the bus that you want to communicate with
        this.device = i2c.getDevice(ADXL345_ADDR);
        // Select Bandwidth rate register, Normal mode, Output data rate = 100 Hz
        device.write(0x2C, (byte)0x0A);

        // Select Power control register, Auto-sleep disable
        device.write(Power_Register, (byte)0x08);//disable sleep mode

        // Select Data format register
        // Self test disabled, 4-wire interface, Full resolution, range = ±2g
        device.write(0x31, (byte)0x08);
        Thread.sleep(100);
    }

    public void readRegisters() throws InterruptedException, IOException{
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
        this.xAccl = ((data[1] & 0x03) * 256 + (data[0] & 0xFF));
        if(xAccl > 511){
            xAccl -= 1024;
        }

        this.yAccl = ((data[3] & 0x03) * 256 + (data[2] & 0xFF));
        if(yAccl > 511){
            yAccl -= 1024;
        }

        this.zAccl = ((data[5] & 0x03) * 256 + (data[4] & 0xFF));
        if(zAccl > 511){
            zAccl -= 1024;
        }
    }

    public int getxAccl() {
        return xAccl;
    }

    public void setxAccl(int xAccl) {
        this.xAccl = xAccl;
    }

    public int getyAccl() {
        return yAccl;
    }

    public void setyAccl(int yAccl) {
        this.yAccl = yAccl;
    }

    public int getzAccl() {
        return zAccl;
    }

    public void setzAccl(int zAccl) {
        this.zAccl = zAccl;
    }
}
