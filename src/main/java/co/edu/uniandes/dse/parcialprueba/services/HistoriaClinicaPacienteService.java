package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import java.util.List;
import co.edu.uniandes.dse.parcialprueba.entities.HistoriaClinicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.repositories.HistoriaClinicaRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HistoriaClinicaPacienteService {

    @Autowired
    HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @Transactional
    public HistoriaClinicaEntity createHistoriaClinica(HistoriaClinicaEntity historiaClinica) throws IllegalOperationException{
        if(historiaClinica == null){
            throw new IllegalOperationException("La historia clinica no puede ser nula");
        }
        return historiaClinicaRepository.save(historiaClinica);
    }

    @Transactional
    public HistoriaClinicaEntity getHistoriaClinica(Long historiaClinicaId) throws EntityNotFoundException {
        Optional<HistoriaClinicaEntity> historiaClinicaEntity = historiaClinicaRepository.findById(historiaClinicaId);
        if (historiaClinicaEntity.isEmpty())
            throw new EntityNotFoundException("No se encontro la historia clinica con id " + historiaClinicaId);
        return historiaClinicaEntity.get();
    }

    @Transactional
    public List<HistoriaClinicaEntity> getHistoriasClinicas(){
        return historiaClinicaRepository.findAll();
    }

    @Transactional
    public HistoriaClinicaEntity createHistoriaClinicaPaciente(Long pacienteId, HistoriaClinicaEntity historiaClinica) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creacion de historia clinica para paciente con id =" + pacienteId);

        Optional<PacienteEntity> pacienteOpt = pacienteRepository.findById(pacienteId);
        if (pacienteOpt.isEmpty())
            throw new EntityNotFoundException("No se encontro el paciente con id = " + pacienteId);

        PacienteEntity paciente = pacienteOpt.get();

        if (paciente.getAcudiente() != null) {
            historiaClinica.setDiagnostico("HistoriaCompartida-" + historiaClinica.getDiagnostico());
        }

        HistoriaClinicaEntity saved = historiaClinicaRepository.save(historiaClinica);

        log.info("Termina proceso de creacion de historia clinica para paciente con id =" + pacienteId);
        return saved;
    }

}
