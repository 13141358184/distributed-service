package com.xiaoheiwu.service.transmission;

import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.router.IRouterManager;
import com.xiaoheiwu.service.router.impl.JVMRouterManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        IRouterManager manager=ComponentProvider.getInstance(JVMRouterManager.class);
        System.out.println(manager);
    }
}
