package co.edu.uniandes.dse.parcialprueba.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.entities.HistoriaClinicaEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PacienteService.class)
class PacienteServiceTest {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PacienteEntity> pacienteList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from HistoriaClinicaEntity");
        entityManager.getEntityManager().createQuery("delete from PacienteEntity");
    }


    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PacienteEntity paciente = factory.manufacturePojo(PacienteEntity.class);
            paciente.setTelefono("3113234567" + i); 
            entityManager.persist(paciente);
            pacienteList.add(paciente);
        }
    }


    @Test
    void testCreatePaciente() throws IllegalOperationException, EntityNotFoundException {
        PacienteEntity newEntity = factory.manufacturePojo(PacienteEntity.class);
        newEntity.setTelefono("31112345678"); 

        PacienteEntity result = pacienteService.createPaciente(newEntity);

        assertNotNull(result);

        PacienteEntity entity = entityManager.find(PacienteEntity.class, result.getId());

        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getCorreo(), entity.getCorreo());
        assertEquals(newEntity.getTelefono(), entity.getTelefono());
    }


    @Test
    void testCreatePacienteInvalidPhonePrefix() {
        PacienteEntity newEntity = factory.manufacturePojo(PacienteEntity.class);
        newEntity.setTelefono("12312345678"); 

        assertThrows(IllegalOperationException.class, () -> {
            pacienteService.createPaciente(newEntity);
        });
    }

    @Test
    void testCreatePacienteInvalidPhoneLength() {
        PacienteEntity newEntity = factory.manufacturePojo(PacienteEntity.class);
        newEntity.setTelefono("3111234567"); 

        assertThrows(IllegalOperationException.class, () -> {
            pacienteService.createPaciente(newEntity);
        });
    }


    @Test
    void testAssociateAcudiente() throws EntityNotFoundException, IllegalOperationException {
        PacienteEntity paciente = pacienteList.get(0);
        PacienteEntity acudiente = pacienteList.get(1);

        
        HistoriaClinicaEntity historia = factory.manufacturePojo(HistoriaClinicaEntity.class);
        historia.setPaciente(acudiente);
        entityManager.persist(historia);
        acudiente.getHistoriasClinicas().add(historia);

        pacienteService.asociarAcudiente(paciente.getId(), acudiente.getId());

        PacienteEntity updatedPaciente = entityManager.find(PacienteEntity.class, paciente.getId());
        assertEquals(acudiente.getId(), updatedPaciente.getAcudiente().getId());
    }


    @Test
    void testAssociateAcudientePacienteAlreadyHasOne() throws EntityNotFoundException, IllegalOperationException {
        PacienteEntity paciente = pacienteList.get(0);
        PacienteEntity acudiente1 = pacienteList.get(1);
        PacienteEntity acudiente2 = pacienteList.get(2);

        
        HistoriaClinicaEntity historia1 = factory.manufacturePojo(HistoriaClinicaEntity.class);
        historia1.setPaciente(acudiente1);
        entityManager.persist(historia1);
        acudiente1.getHistoriasClinicas().add(historia1);

        HistoriaClinicaEntity historia2 = factory.manufacturePojo(HistoriaClinicaEntity.class);
        historia2.setPaciente(acudiente2);
        entityManager.persist(historia2);
        acudiente2.getHistoriasClinicas().add(historia2);

        pacienteService.asociarAcudiente(paciente.getId(), acudiente1.getId());

        assertThrows(IllegalOperationException.class, () -> {
            pacienteService.asociarAcudiente(paciente.getId(), acudiente2.getId());
        });
    }


    @Test
    void testAssociateAcudienteAcudienteHasOne() throws EntityNotFoundException, IllegalOperationException {
        PacienteEntity paciente1 = pacienteList.get(0);
        PacienteEntity paciente2 = pacienteList.get(1);
        PacienteEntity acudiente = pacienteList.get(2);

      
        HistoriaClinicaEntity historia = factory.manufacturePojo(HistoriaClinicaEntity.class);
        historia.setPaciente(acudiente);
        entityManager.persist(historia);
        acudiente.getHistoriasClinicas().add(historia);

        pacienteService.asociarAcudiente(paciente1.getId(), acudiente.getId());

        assertThrows(IllegalOperationException.class, () -> {
            pacienteService.asociarAcudiente(paciente2.getId(), acudiente.getId());
        });
    }

    @Test
    void testAssociateAcudienteNoHistorias() {
        PacienteEntity paciente = pacienteList.get(0);
        PacienteEntity acudiente = pacienteList.get(1);

        assertThrows(IllegalOperationException.class, () -> {
            pacienteService.asociarAcudiente(paciente.getId(), acudiente.getId());
        });
    }
}
