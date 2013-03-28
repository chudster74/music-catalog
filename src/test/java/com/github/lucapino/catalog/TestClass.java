/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lucapino.catalog;

import java.util.Locale;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
