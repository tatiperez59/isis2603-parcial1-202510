package co.edu.uniandes.dse.parcialprueba.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class HistoriaClinicaEntity  extends BaseEntity{
    private String diagnostico;
    private String tratamiento;
    private String telefono;
    @Temporal(TemporalType.DATE)
    private Date fechaDeCreacion;
   
    @PodamExclude
    @ManyToOne
    private PacienteEntity paciente;
}
