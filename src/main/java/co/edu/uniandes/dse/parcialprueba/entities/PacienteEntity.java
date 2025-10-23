package co.edu.uniandes.dse.parcialprueba.entities;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class PacienteEntity  extends BaseEntity{
  
    private String nombre;
    private String correo;
    private String telefono;
    //en caso de que el paciente no tenga acudiente, el valor sera nulo
    @PodamExclude
    @OneToOne
    private PacienteEntity acudiente = null;

    @PodamExclude
    @OneToMany(mappedBy="paciente", cascade=CascadeType.PERSIST, orphanRemoval= true)
    private Collection<HistoriaClinicaEntity> historiasClinicas = new ArrayList<>(); 
   
}
