
package com.jrodolfo.basichibernate.entity;

import javax.persistence.*;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05.
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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    public void setText(String text) {
        this.text = text;
    }
}











