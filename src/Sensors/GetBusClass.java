package Sensors;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

public class GetBusClass {
    private static GetBusClass getBusClass = new GetBusClass();
    private static I2CBus i2cBus;

    private GetBusClass(){
        try {
            if (i2cBus == null) {
                i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
            }
        }catch (Exception e){
            System.out.println("EException in Sensors.GetBusClass constructor");
            e.printStackTrace();
        }
    }

    public static I2CBus getBus(){
        return i2cBus;
    }
}