package com.xiaoheiwu.service.protocol;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

import com.xiaoheiwu.service.common.annotation.Componet;
import com.xiaoheiwu.service.common.component.ComponentProvider;
import com.xiaoheiwu.service.serializer.ISerializer;
import com.xiaoheiwu.service.serializer.serializer.meta.MetaSerializer;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
    
{
	ISerializer serializer=ComponentProvider.getInstance(MetaSerializer.class);
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public void testLoad(  )
    {
    System.out.println(serializer);
    	
    }

}
