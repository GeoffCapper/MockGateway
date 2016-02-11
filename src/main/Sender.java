package main;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openhab.binding.mysensors.internal.MySensorsMessage;

import srv.MockGatewayServer;

public class Sender implements Runnable {

    private static Sender singelton = null;

    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Future<?> future = null;

    protected boolean stopServer = false;

    public static Sender getSender() {
        if (singelton == null) {
            singelton = new Sender();
        }
        return singelton;
    }

    private Sender() {

    }

    public void startServer() {
        future = executor.submit(this);
    }

    public void stopServer() {

        this.stopServer = true;

        if (future != null) {
            future.cancel(true);
        }

        if (executor != null) {
            executor.shutdown();
            executor.shutdownNow();
        }
    }

    @Override
    public void run() {
        while (!stopServer) {
            if (MockGatewayServer.getMockGatewayServer().isConnected()) {
                long currentTime = System.currentTimeMillis();
                for (Node node : NodeList.getNodes().nodes) {
                    if (currentTime > node.getLastSend() + node.getSendDelay()) {
                        MySensorsMessage msg = new MySensorsMessage();
                        msg.setNodeId(node.getNodeId());
                        msg.setChildId(node.getChildId());
                        msg.setSubType(node.getSubType());
                        msg.setMsgType(node.getMsgType());
                        msg.setAck(node.getAck());

                        if (node.getSubType() == 2) {// It's a light!
                            if (generateRandomBool()) {
                                msg.setMsg("1");
                            } else {
                                msg.setMsg("0");
                            }
                        } else {
                            msg.setMsg(generateRandomDouble(node.getMsg_min(), node.getMsg_max()));
                        }

                        node.setLastSend(currentTime);
                        MockGatewayServer.getMockGatewayServer().writeMessage(msg);
                    }
                }
            }

            // Time to sleep
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private String generateRandomDouble(int start, int end) {
        Random rand = new Random(System.currentTimeMillis());
        double value = rand.nextDouble();
        value = (end - start) * value + start;
        return String.format(Locale.US, "%.2f", value);
    }

    private boolean generateRandomBool() {
        Random rand = new Random(System.currentTimeMillis());
        if (rand.nextDouble() > 0.5) {
            return true;
        } else {
            return false;
        }
    }
}
