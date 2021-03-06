package srv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openhab.binding.mysensors.MySensorsBindingConstants;
import org.openhab.binding.mysensors.internal.MySensorsMessage;
import org.openhab.binding.mysensors.internal.MySensorsMessageParser;

import conf.Conf;
import main.Node;
import main.NodeList;

public class MockGatewayServer implements Runnable {

    private static MockGatewayServer singelton = null;

    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Future<?> future = null;

    protected boolean stopServer = false;

    private ServerSocket serverSock = null;
    private PrintStream outStream = null;

    private boolean connected = false;

    public static MockGatewayServer getMockGatewayServer() {
        if (singelton == null) {
            singelton = new MockGatewayServer();
        }
        return singelton;
    }

    private MockGatewayServer() {
        try {
            serverSock = new ServerSocket(Conf.MOCKGATEWAY_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Socket sock = null;
            try {
                sock = serverSock.accept();
                System.out.println("Connection from client successful!");
                connected = true;
                reader(sock);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (sock != null) {
                    try {
                        connected = false;
                        sock.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void reader(Socket sock) {
        try {
            BufferedReader inStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            outStream = new PrintStream(sock.getOutputStream());

            String line = "";

            while ((line = inStream.readLine()) != null) {
                System.out.println(line);

                MySensorsMessage msg = MySensorsMessageParser.parse(line);
                if (msg == null) {
                    continue;
                }

                // Did we receive an ACK?
                if (msg.getAck() == 1) {
                    Node node = NodeList.getNodes().getNode(msg.getNodeId(), msg.getChildId());
                    if (node != null) {
                        if (node.getAnswerAck() == 1) {
                            writeMessage(msg);
                        }
                    }
                }

                if (msg.getNodeId() == 0) {
                    if (msg.getChildId() == 0) {
                        if (msg.getMsgType() == MySensorsBindingConstants.MYSENSORS_MSG_TYPE_INTERNAL) {
                            if (msg.getAck() == 0) {
                                if (msg.getSubType() == MySensorsBindingConstants.MYSENSORS_SUBTYPE_I_VERSION) {

                                    msg.setMsg("2.0");
                                    writeMessage(msg);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeMessage(MySensorsMessage msg) {
        if (outStream != null) {
            outStream.print(MySensorsMessageParser.generateAPIString(msg));
        }
    }

    public boolean isConnected() {
        return connected;
    }

}
