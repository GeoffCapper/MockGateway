package main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import conf.Conf;

public class NodeList {
    private static NodeList singelton = null;

    public List<Node> nodes = new ArrayList<>();

    public static NodeList getNodes() {
        if (singelton == null) {
            singelton = new NodeList();
        }
        return singelton;
    }

    private NodeList() {

    }

    public void generateNodeList(String path) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        // Setup a new eventReader

        try {
            InputStream in = new FileInputStream(path);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Node node = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have an item element, we create a new item
                    if (startElement.getName().getLocalPart() == Conf.MOCKGATEWAY_XML_NODE_BEGIN) {
                        node = new Node();
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_NODEID)) {
                        event = eventReader.nextEvent();
                        node.setNodeId(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_CHILDID)) {
                        event = eventReader.nextEvent();
                        node.setChildId(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_ACK)) {
                        event = eventReader.nextEvent();
                        node.setAck(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_MSGTYPE)) {
                        event = eventReader.nextEvent();
                        node.setMsgType(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_SENDDELAY)) {
                        event = eventReader.nextEvent();
                        node.setSendDelay(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_SUBTYPE)) {
                        event = eventReader.nextEvent();
                        node.setSubType(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_MSG_MIN)) {
                        event = eventReader.nextEvent();
                        node.setMsg_min(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(Conf.MOCKGATEWAY_XML_MSG_MAX)) {
                        event = eventReader.nextEvent();
                        node.setMsg_max(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }
                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart() == ("node")) {
                        nodes.add(node);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printNodeList() {
        for (Node node : nodes) {
            System.out.println("########### Node ##############");
            System.out.println("NodeId: " + node.getNodeId());
            System.out.println("ChildId: " + node.getChildId());
            System.out.println("SubType: " + node.getSubType());
        }
    }

}
