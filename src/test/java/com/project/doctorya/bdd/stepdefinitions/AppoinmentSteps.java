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

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
    public void createAppointment(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> row = rows.get(0);

        // Buscamos los objetos reales que el Background creó
        Doctor doc = doctorService.getByIdentification(row.get("doctorIdentification"));
        Patient pat = patientService.getByIdentification(row.get("patientIdentification"));

        AppointmentDto dto = new AppointmentDto();
        dto.setDoctorId(doc.getId()); // Usamos el UUID real de la DB
        dto.setPatientId(pat.getId()); // Usamos el UUID real de la DB
        dto.setDate(LocalDateTime.parse(row.get("date")));
        dto.setReason(row.get("reason"));

        appointment = appointmentService.create(dto);
    }

    @Then("the appointment should be created successfully")
    public void appointmentCreatedSuccessfully() {
        assertNotNull(appointment);
        assertNotNull(appointment.getId());
    }

    @And("the appointment doctor identification should be {string}")
public void verifyDoctorIdentification(String identification) {
    // 1. Obtenemos el UUID que tiene la cita
    java.util.UUID idDelDoctorEnCita = appointment.getDoctorId();
    
    // 2. Usamos el servicio para buscar al doctor real con ese UUID
    // Asumiendo que tu DoctorService tiene un método getById o similar
    com.project.doctorya.models.Doctor doctorReal = doctorService.getById(idDelDoctorEnCita);
    
    // 3. Ahora sí, comparamos la identificación del doctor encontrado
    assertEquals(identification, doctorReal.getIdentification());
}

@And("the appointment patient identification should be {string}")
public void verifyPatientIdentification(String identification) {
    // 1. Obtenemos el UUID del paciente en la cita
    java.util.UUID idDelPacienteEnCita = appointment.getPatientId();
    
    // 2. Buscamos al paciente real
    com.project.doctorya.models.Patient patientReal = patientService.getById(idDelPacienteEnCita);
    
    // 3. Comparamos
    assertEquals(identification, patientReal.getIdentification());

    }
}