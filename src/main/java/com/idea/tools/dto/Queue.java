package com.idea.tools.dto;

import lombok.Data;

@Data
public class Queue {

    private Integer id;
    private String name;
    private Server server;

    public Queue(Integer id, String name, Server server) {
        this.id = id;
        this.name = name;
        this.server = server;
    }
}
