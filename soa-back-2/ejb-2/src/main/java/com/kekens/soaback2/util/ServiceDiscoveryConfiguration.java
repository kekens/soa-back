package com.kekens.soaback2.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDiscoveryConfiguration {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Service")
    private String name;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Port")
    private int port;


}
