package com.kekens.soaback2.util;

import com.kekens.soaback2.service.BarsService;
import org.wildfly.naming.client.WildFlyInitialContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteBeanLookupUtil {

    public static BarsService lookupBarsBean() {
        //TODO ENV VAR
        try {
            return (BarsService) getContext().lookup("java:global/ejb-2/BarsServiceImpl");
        } catch (NamingException e) {
            System.out.println("--------");
            System.out.println(e.getMessage());
            System.out.println("-----------");
        }
        // TODO RETURN NOT NULL
        return null;
    }

    private static Context getContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
//        props.put(Context.PROVIDER_URL, "http://localhost:8080/ejb-invoker");
        return new InitialContext(props);
    }


}
