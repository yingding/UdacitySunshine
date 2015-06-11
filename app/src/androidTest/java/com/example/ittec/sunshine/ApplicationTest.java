package com.example.ittec.sunshine;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.json.JSONException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 * http://stackoverflow.com/questions/19891564/how-to-run-unit-tests-with-android-studio
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testParserBasis() {
        WeatherDataParserTest parserTest = new WeatherDataParserTest();
        try {
            parserTest.testMountainViewThirdDay();
            parserTest.testFremontLastDay();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}