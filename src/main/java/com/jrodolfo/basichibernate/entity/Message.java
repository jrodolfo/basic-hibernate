package com.jrodolfo.basichibernate.entity;

import javax.persistence.*;

/**
 * Main entity of this app
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05
 */
@Entity @Table(name = "message")
public class Message {

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












