package com.kekens.soaback1;

import com.kekens.soaback1.util.CheckServiceConfiguration;
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
        String consulUrl = System.getenv("CONSUL_URL");

        ServiceDiscoveryConfiguration configuration = new ServiceDiscoveryConfiguration();
        configuration.setId(System.getenv("CONSUL_SERVICE_1_ID"));
        configuration.setName(System.getenv("CONSUL_SERVICE_1_NAME"));
        configuration.setAddress(System.getenv("CONSUL_SERVICE_1_ADDRESS"));
        configuration.setPort(Integer.parseInt(System.getenv("CONSUL_SERVICE_1_PORT")));

        CheckServiceConfiguration checkServiceConfiguration = new CheckServiceConfiguration();
        checkServiceConfiguration.setName("check " + System.getenv("CONSUL_SERVICE_1_ID"));
        checkServiceConfiguration.setTcp("localhost:" + System.getenv("CONSUL_SERVICE_1_PORT"));
        checkServiceConfiguration.setInterval("10s");
        checkServiceConfiguration.setStatus("critical");

        configuration.setCheck(checkServiceConfiguration);

        Response response = client
                .target(consulUrl + "/v1/agent/service/register")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(configuration, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));

        if (response.getStatus() != 200) {
            throw new Exception("Failed to register service in Service discovery");
        }
    }


}
