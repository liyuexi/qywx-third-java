package com.tobdev.qywxthird.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name = "xml")
public class MessageReply {

    @XmlElement(name = "Encrypt")
    public String encrypt;
    @XmlElement(name = "MsgSignature")
    public String msgSignature;
    @XmlElement(name = "TimeStamp")
    public String timeStamp;
    @XmlElement(name = "Nonce")
    public String nonce;

    @XmlTransient
    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    @XmlTransient
    public String getMsgSignature() {
        return msgSignature;
    }

    public void setMsgSignature(String msgSignature) {
        this.msgSignature = msgSignature;
    }

    @XmlTransient
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @XmlTransient
    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
