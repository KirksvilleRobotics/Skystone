package caleb.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothClient implements Runnable {

    private static final UUID SERVER_UUID = UUID.fromString("0c15f6a2-e4bb-11e9-81b4-2a2ae2dbcce4");
    private static final String DEVICE_ADDRESS = "98:01:A7:A4:11:67";

    private BluetoothAdapter adapter;

    private OutputStream outputStream;
    private Telemetry telemetry;

    private Thread thread;
    private Context context;

    public BluetoothClient(Telemetry telemetry, Context context) {
        this.telemetry = telemetry;
        this.context = context;
        telemetry.addData("debug", "initializing adapter");
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.getApplicationContext().registerReceiver(receiver, intentFilter);
        telemetry.addData("debug", "getting device");
        BluetoothDevice device = adapter.getRemoteDevice(DEVICE_ADDRESS);
        telemetry.addData("debug", "got device");

        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(SERVER_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            telemetry.addData("debug", "creating socket");
            tmp = device.createRfcommSocketToServiceRecord(SERVER_UUID);
            telemetry.addData("debug", "socket created");
            telemetry.addData("debug", "connecting to socket");
            tmp.connect();
            //outputStream = socket.getOutputStream();
        } catch (IOException e) {
            Class<?> clazz = tmp.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
            Method m = null;
            try {
                m = clazz.getMethod("createRfcommSocket", paramTypes);
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
            Object[] params = new Object[] {Integer.valueOf(1)};
            try {
                BluetoothSocket fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
                fallbackSocket.connect();
            } catch (Exception ex) {
                telemetry.addData("debug", "fallback socket failed");
            }
        }
    }

    public void sendData(double x, double y, double theta) {
        byte[] data = SerializeData.serializeData(x, y, theta);
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
