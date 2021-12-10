package com.kekens.soaback2.util;

import com.kekens.soaback2.service.BarsService;
import com.kekens.soaback2.validator.IntegrityError;
import com.kekens.soaback2.validator.exception.IncorrectDataException;
import org.wildfly.naming.client.WildFlyInitialContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Collections;
import java.util.Properties;

public class RemoteBeanLookupUtil {

    public static BarsService lookupBarsBean() throws IncorrectDataException {
        try {
            return (BarsService) getContext().lookup(System.getenv("BARS_BEAN_JNDI"));
        } catch (NamingException e) {
            System.out.println("--------");
            System.out.println(e.getMessage());
            System.out.println("-----------");
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(500, "Cant' lookup BarsBean")));
        }
    }

    private static Context getContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
        return new InitialContext(props);
    }


}
