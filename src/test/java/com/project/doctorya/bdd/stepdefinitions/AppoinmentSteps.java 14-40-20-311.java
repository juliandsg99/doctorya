package com.project.doctorya.bdd.stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.project.doctorya.dtos.AppointmentDto;
import com.project.doctorya.models.Appointment;
import com.project.doctorya.models.Doctor;
import com.project.doctorya.models.Patient;
import com.project.doctorya.services.AppointmentService;
import com.project.doctorya.services.DoctorService;
import com.project.doctorya.services.PatientService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;

@SpringBootTest
public class AppointmentSteps {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    private Appointment appointment;

    @When("I create an appointment with the following details:")
    public void createAppointment(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.get(0);

        Doctor doc = doctorService.getByIdentification(row.get("doctorIdentification"));
        Patient pat = patientService.getByIdentification(row.get("patientIdentification"));

        AppointmentDto dto = new AppointmentDto();
        dto.setDoctorId(doc.getId());
        dto.setPatientId(pat.getId());
        dto.setDate(LocalDateTime.parse(row.get("date")));
        dto.setReason(row.get("reason"));

        this.appointment = appointmentService.create(dto);
    }

    @Then("the appointment should be created successfully")
    public void appointmentCreatedSuccessfully() {
        assertNotNull(this.appointment);
    }

    @Then("the appointment doctor identification should be {string}")
    public void the_appointment_doctor_identification_should_be(String identification) {
        Doctor doctorReal = doctorService.getById(this.appointment.getDoctorId());
        assertEquals(identification, doctorReal.getIdentification());
    }

    @Then("the appointment patient identification should be {string}")
    public void the_appointment_patient_identification_should_be(String identification) {
        Patient patientReal = patientService.getById(this.appointment.getPatientId());
        assertEquals(identification, patientReal.getIdentification());
    }
}