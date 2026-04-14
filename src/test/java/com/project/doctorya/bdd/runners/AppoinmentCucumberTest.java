package com.project.doctorya.bdd.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features/appointment.feature",
    glue = "com.project.doctorya.bdd.steps", // Aquí deben estar TODOS los steps
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class AppoinmentCucumberTest {
}