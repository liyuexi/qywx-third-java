package com.tobdev.qywxthird.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name = "xml")
public class MessageText {

    @XmlElement(name = "ToUserName")
    public String toUserName;
    @XmlElement(name = "FromUserName")
    public String fromUserName;
    @XmlElement(name = "CreateTime")
    public String createTime;
    @XmlElement(name = "MsgType")
    public String msgType;
    @XmlElement(name = "Content")
    public String content;
    @XmlElement(name = "MsgId")
    public String msgId;
    @XmlElement(name = "AgentID")
    public String agentID;

    @XmlTransient
    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    @XmlTransient
    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    @XmlTransient
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @XmlTransient
    public String getMsgType() {
        return msgType;
    }


    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @XmlTransient
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @XmlTransient
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @XmlTransient
    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }
}
