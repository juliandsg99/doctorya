package com.project.doctorya.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.doctorya.dtos.AppointmentDto;
import com.project.doctorya.models.Appointment;
import com.project.doctorya.models.Doctor;
import com.project.doctorya.models.Patient;
import com.project.doctorya.services.AppointmentService;
import com.project.doctorya.services.DoctorService;
import com.project.doctorya.services.PatientService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    // Usamos esta variable estática para mantener el ID de la cita entre tests
    private static UUID savedAppointmentId;

    @Test
    @Order(1)
    void testCreateAppointment() throws Exception {
        // 1. Obtenemos el doctor y paciente creados en los otros tests por su identificación
        Doctor doctor = doctorService.getByIdentification("9999999999");
        Patient patient = patientService.getByIdentification("1053847610");

        // 2. Preparamos el DTO de la cita
        AppointmentDto dto = new AppointmentDto();
        dto.setDoctorId(doctor.getId());
        dto.setPatientId(patient.getId());
        dto.setDate(LocalDateTime.now());
        dto.setReason("Consulta inicial de seguimiento");

        // 3. Creamos la cita
        Appointment appointment = appointmentService.create(dto);

        assertNotNull(appointment);
        assertNotNull(appointment.getId());
        assertEquals(dto.getReason(), appointment.getReason());
        
        // Guardamos el ID para usarlo en los siguientes métodos @Test
        savedAppointmentId = appointment.getId();
    }

    @Test
    @Order(2)
    void testGetById() throws Exception {
        // Buscamos la cita usando el ID que guardamos en el paso anterior
        Appointment found = appointmentService.getById(savedAppointmentId);

        assertNotNull(found);
        assertEquals(savedAppointmentId, found.getId());
        assertEquals("Consulta inicial de seguimiento", found.getReason());
    }

    @Test
    @Order(3)
    void testUpdateAppointment() throws Exception {
        // Preparamos los datos para actualizar
        AppointmentDto updateDto = new AppointmentDto();
        updateDto.setReason("Urgencia odontológica");

        Appointment updated = appointmentService.update(updateDto, savedAppointmentId);

        assertNotNull(updated);
        assertEquals("Urgencia odontológica", updated.getReason());
    }

    /*@Test
    @Order(4)
    void testDeleteAppointment() throws Exception {
        // Verificamos que se pueda eliminar
        appointmentService.delete(savedAppointmentId);
        
        // NOTA: Si quieres ver el registro en la base de datos al finalizar, 
        // comenta la línea del delete.
    }*/
}