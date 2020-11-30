package com.example.imapet;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


@RunWith(JUnit4.class)
public class CalculatePetAgeJUnit {

    private Calculator mCalculator;

    @Before
    public void setUp() {
        mCalculator = new Calculator();
    }

    @Test
    public void calculatePetAge() {
        mCalculator.humanAge = 10;
        mCalculator.petWeight = 10;
        mCalculator.spnChoice = "Dog";
        assertThat(mCalculator.petYearCalc(mCalculator.humanAge, mCalculator.petWeight, mCalculator.spnChoice),
                is(equalTo(56)));
    }

    /** Ensuring database is empty if nothing has been inserted
     * should not retrieve or delete anything in this case
     */
    @Test
    public void retrieveCalc() {
        assertThat(mCalculator.retrieval, is(equalTo(null)));
    }

    @Test
    public void deleteCalc() {
        assertThat(mCalculator.deletion, is(equalTo(null)));
    }
}
