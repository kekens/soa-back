package com.kekens.soaback1.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServiceDiscoveryConfiguration {

    private String id;

    private String name;

    private String address;

    private int port;

}
