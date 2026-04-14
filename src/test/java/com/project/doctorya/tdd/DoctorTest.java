package com.project.doctorya.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.project.doctorya.dtos.DoctorDto;
import com.project.doctorya.models.Doctor;
import com.project.doctorya.services.DoctorService;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DoctorTest {

    @Autowired
    DoctorService doctorService;

    @Test
    @Order(1)
    void testCreateDoctor() throws Exception {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setIdentification("9999999999");
        doctorDto.setName("Doctor Test");
        doctorDto.setSpecialty("General Medicine");

        Doctor doctor = doctorService.create(doctorDto);

        assertNotNull(doctor);
        assertEquals(doctorDto.getIdentification(), doctor.getIdentification());
        assertEquals(doctorDto.getName(), doctor.getName());
        assertEquals(doctorDto.getSpecialty(), doctor.getSpecialty());
    }

    @Test
    @Order(2)
    void testGetByIdentification() throws Exception {
        Doctor doctor = doctorService.getByIdentification("9999999999");

        assertNotNull(doctor);
        assertEquals("9999999999", doctor.getIdentification());
    }

    @Test
    @Order(3)
    void testGetById() throws Exception {
        Doctor doctor = doctorService.getByIdentification("9999999999");
        Doctor doctor2 = doctorService.getById(doctor.getId());

        assertEquals(doctor.getId(), doctor2.getId());
        assertEquals(doctor.getIdentification(), doctor2.getIdentification());
    }

    @Test
    @Order(4)
    void testUpdateDoctor() throws Exception {
        Doctor doctor = doctorService.getByIdentification("9999999999");

        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setName("Doctor Updated");

        Doctor updated = doctorService.update(doctorDto, doctor.getId());

        assertNotNull(updated);
        assertEquals("Doctor Updated", updated.getName());
    }

    @Test
    @Order(5)
    void testDeleteDoctor() throws Exception {
        Doctor doctor = doctorService.getByIdentification("9999999999");

        doctorService.delete(doctor.getId());
    }
}



