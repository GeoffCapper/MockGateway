package main;

import srv.MockGatewayServer;

public class MockGateway {

    private static MockGateway singelton = null;

    public static MockGateway getMockGateway() {
        if (singelton == null) {
            singelton = new MockGateway();
        }
        return singelton;
    }

    public static void main(String[] args) {
        if (!(args != null && args.length > 0 && !args[0].isEmpty())) {
            System.out.println("Specifiy the path to the xml definition of the nodes!");
            System.exit(1);
        }

        MockGatewayServer mv = MockGatewayServer.getMockGatewayServer();
        mv.startServer();
        NodeList.getNodes().generateNodeList(args[0]);
        // NodeList.getNodes().printNodeList();

        Sender.getSender().startServer();
    }
}
