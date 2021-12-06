package com.kekens.soaback1;

import com.kekens.soaback1.util.ServiceDiscoveryConfiguration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationPath("")
public class App extends Application {

    public App() throws Exception {
        super();

        Client client = ClientBuilder.newBuilder().build();
//        String sdUrl = System.getenv("CONSUL_URL");
        String consulUrl = "http://localhost:8500";

        ServiceDiscoveryConfiguration configuration = new ServiceDiscoveryConfiguration();
        configuration.setId("soa1");
        configuration.setName("soa-back-1");
        configuration.setAddress("https://localhost");
        configuration.setPort(18443);

        Response response = client
                .target(consulUrl + "/v1/agent/service/register")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(configuration, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());

        if (response.getStatus() != 200 && response.getStatus() != 204) {
            throw new Exception("Failed to register service in Service discovery");
        }
    }


}
