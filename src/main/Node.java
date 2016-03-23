package main;

public class Node {
    private int nodeId = 0;
    private int childId = 0;
    private int ack = 0;
    private int msgType = 0;
    private int subType = 0;
    private String msg = "";
    private int msg_min = 0;
    private int msg_max = 0;
    private int sendDelay = 0;
    private long lastSend = 0;
    private int answerAck = 0;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMsg_min() {
        return msg_min;
    }

    public void setMsg_min(int msg_min) {
        this.msg_min = msg_min;
    }

    public int getMsg_max() {
        return msg_max;
    }

    public void setMsg_max(int msg_max) {
        this.msg_max = msg_max;
    }

    public int getSendDelay() {
        return sendDelay;
    }

    public void setSendDelay(int sendDelay) {
        this.sendDelay = sendDelay;
    }

    public long getLastSend() {
        return lastSend;
    }

    public void setLastSend(long lastSend) {
        this.lastSend = lastSend;
    }

    public int getAnswerAck() {
        return answerAck;
    }

    public void setAnswerAck(int answerAck) {
        this.answerAck = answerAck;
    }

}
