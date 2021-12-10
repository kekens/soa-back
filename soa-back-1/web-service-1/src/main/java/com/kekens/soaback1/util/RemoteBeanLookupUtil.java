package com.kekens.soaback1.util;

import com.kekens.soaback1.service.DisciplineService;

import com.kekens.soaback1.service.LabWorkService;
import com.kekens.soaback1.validator.IntegrityError;
import com.kekens.soaback1.validator.exception.IncorrectDataException;
import org.wildfly.naming.client.WildFlyInitialContextFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Collections;
import java.util.Properties;

public class RemoteBeanLookupUtil {

    public static LabWorkService lookupLabWorkBean() throws IncorrectDataException {
        try {
            return (LabWorkService) getContext().lookup(System.getenv("LABWORK_BEAN_JNDI"));
        } catch (NamingException e) {
            System.out.println("--------");
            System.out.println(e.getMessage());
            System.out.println("-----------");
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(400, "Can't lookup LabWorkBean")));
        }
    }

    public static DisciplineService lookupDisciplineBean() throws IncorrectDataException{
        try {
            return (DisciplineService) getContext().lookup(System.getenv("DISCIPLINE_BEAN_JNDI"));
        } catch (NamingException e) {
            System.out.println("--------");
            System.out.println(e.getMessage());
            System.out.println("-----------");
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(400, "Can't lookup DisciplineBean")));
        }
    }

    private static Context getContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
        return new InitialContext(props);
    }


}
