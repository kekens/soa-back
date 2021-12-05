package com.kekens.soaback1.util;

import com.kekens.soaback1.service.DisciplineService;

import com.kekens.soaback1.service.LabWorkService;
import org.wildfly.naming.client.WildFlyInitialContextFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteBeanLookupUtil {

    public static LabWorkService lookupLabWorkBean() {
        //TODO ENV VAR
        try {
            return (LabWorkService) getContext().lookup("java:global/ejb-1/LabWorkServiceImpl");
        } catch (NamingException e) {
            System.out.println("--------");
            System.out.println(e.getMessage());
            System.out.println("-----------");
        }
        // TODO RETURN NOT NULL
        return null;
    }

    public static DisciplineService lookupDisciplineBean() {
        try {
            return (DisciplineService) getContext().lookup("java:global/ejb-1/DisciplineServiceImpl");
        } catch (NamingException e) {
            System.out.println("--------");
            System.out.println(e.getMessage());
            System.out.println("-----------");
        }
        return null;
    }

    private static Context getContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
        return new InitialContext(props);
    }


}
