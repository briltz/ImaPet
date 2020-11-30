package com.example.imapet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class CalculatePetAgeJUnit {

    Calculator mCalculator;

    @Before
    public void setUp() {
        mCalculator = new Calculator();
    }
    @Test
    public void calculatePetAge() {
        mCalculator.humanAge = 10;
        mCalculator.petWeight = 10;
        mCalculator.spnChoice = "Dog";
        mCalculator.calculate();
        assertThat(mCalculator.petYear, 56);
    }
}
