package com.kekens.soaback1.util;

import lombok.Data;

@Data
public class ServiceDiscoveryConfiguration {

    private String id;

    private String name;

    private String address;

    private int port;

    private CheckServiceConfiguration check;

}
