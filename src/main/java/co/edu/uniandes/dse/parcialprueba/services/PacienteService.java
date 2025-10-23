package co.edu.uniandes.dse.parcialprueba.services;

import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialprueba.repositories.PacienteRepository;
import co.edu.uniandes.dse.parcialprueba.exceptions.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PacienteService {
    
    @Autowired
    PacienteRepository pacienteRepository;

    @Transactional
    public PacienteEntity createPaciente (PacienteEntity pacienteEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación de un paciente");

        if (!pacienteEntity.getTelefono().startsWith("311") && !pacienteEntity.getTelefono().startsWith("601"))
            throw new IllegalOperationException("El telefono debe comenzar con 311 o con 601");

        if (pacienteEntity.getTelefono().length() != 11)
            throw new IllegalOperationException("El telefono debe tener 11 digitos");

        log.info("Termina proceso de creación del paciente");
		return pacienteRepository.save(pacienteEntity);
	}

    @Transactional
    public PacienteEntity getPaciente(Long pacienteId) throws EntityNotFoundException {

        log.info("Inicia proceso de consultar el paciente con id ="+ pacienteId);
        Optional<PacienteEntity> pacienteEntity = pacienteRepository.findById(pacienteId);
        if (pacienteEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro el paciente con id " + pacienteId);
        log.info("Termina proceso de consultar el paciente con id =" + pacienteId);
        return pacienteEntity.get();
    }

    @Transactional
    public void asociarAcudiente(Long pacienteId, Long acudienteId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de asociar acudiente al paciente con id =" + pacienteId);

        PacienteEntity paciente = getPaciente(pacienteId);
        PacienteEntity acudiente = getPaciente(acudienteId);

        if (paciente.getAcudiente() != null)
            throw new IllegalOperationException("El paciente ya tiene un acudiente");

        if (acudiente.getAcudiente() == null)
            throw new IllegalOperationException("El acudiente no puede tener acudiente");

        if (acudiente.getHistoriasClinicas().isEmpty())
            throw new IllegalOperationException("El acudiente debe tener al menos una historia clinica");

        paciente.setAcudiente(acudiente);
        
        pacienteRepository.save(paciente);

        log.info("Termina proceso de asociar acudiente al paciente con id =" + pacienteId);
    }

}
