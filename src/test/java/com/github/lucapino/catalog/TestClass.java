/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.catalog;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 *
 * @author tagliani
 */
public class TestClass {

    static Logger logger = LoggerFactory.getLogger(TestClass.class);

    @Test
    public void test() {
        Locale locale = Locale.getDefault();
        logger.info("Default locale is " + locale.toString());
    }
}
