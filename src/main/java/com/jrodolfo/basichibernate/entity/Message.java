package com.jrodolfo.basichibernate.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.invoke.MethodHandles;


/**
 * Main entity of this app
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05
 */
@Entity @Table(name = "message")
public class Message {

    @Transient
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name="id")
    private Long id;

    @Column(name="text")
    private String text;

    public Message(){}

    public Message(String text) {
        this.text = text;
    }

    public Message(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        String defaultToString = super.toString();
        String moreInfo = "identityHashCode=" + Integer.toHexString(System.identityHashCode(this));
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                "} " + defaultToString + " " + moreInfo;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (!(that instanceof Message)) return false;
        Message message = (Message) that;
        if (!getId().equals(message.getId())) return false;
        return getText().equals(message.getText());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getText().hashCode();
        return result;
    }

    public void compare(Message that) {
        checkIdentity(that);
        checkEquality(that);
    }

    public boolean checkIdentity(Message that) {
        logger.debug("\nChecking if the following objects are identical:\n" + this + '\n' + that);
        boolean areIdentical = (this == that);
        if (areIdentical) {
            logger.debug("They are identical.");
        } else {
            logger.debug("They are NOT identical.");
        }
        return areIdentical;
    }

    public boolean checkEquality(Message that) {
        logger.debug("\nChecking if the following objects are equal:\n" + this + '\n' + that);
        boolean areEqual = this.equals(that);
        if (areEqual) {
            logger.debug("They are equal.");
        } else {
            logger.debug("They are NOT equal.");
        }
        return areEqual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}












