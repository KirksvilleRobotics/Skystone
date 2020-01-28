package caleb.server;

import java.nio.ByteBuffer;

/**
 * SerializeData
 *
 * This is a static utils class that serializes a packet
 * of data to send to the debugger software
 */
public class SerializeData {
    private SerializeData() {}

    /**
     * This function serializes data into a byte array. The first 8
     * bytes correspond to the x coordinate, the next 8 correspond
     * to the y coordinate, and the last 8 correspond to the theta.
     *
     * @param robotX robot's x coordinate
     * @param robotY robot's y coordinate
     * @param robotTheta robot's theta
     * @return a byte array of size 24
     */
    public static byte[] serializeData(double robotX, double robotY, double robotTheta) {
        byte[] byteX = new byte[8];
        ByteBuffer.wrap(byteX).putDouble(robotX);

        byte[] byteY = new byte[8];
        ByteBuffer.wrap(byteY).putDouble(robotY);

        byte[] byteTheta = new byte[8];
        ByteBuffer.wrap(byteTheta).putDouble(robotTheta);

        byte[] data = new byte[24];
        System.arraycopy(byteX, 0, data, 0, 8);
        System.arraycopy(byteY, 0, data, 8, 8);
        System.arraycopy(byteTheta, 0, data, 16, 8);

        return data;
    }
}
