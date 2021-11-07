package com.kekens.soa_back_2;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

@ApplicationPath("")
public class App extends Application {

    @Override
    public Map<String, Object> getProperties() {

        Map<String, Object> properties = new HashMap<>();
        properties.put("jersey.config.server.wadl.disableWadl", "true");
        return properties;
    }

}
