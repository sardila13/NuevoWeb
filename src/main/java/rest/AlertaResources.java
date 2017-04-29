package rest;

import Entites.AlertaEntity;
import dto.AlertaDTO;
import dto.ConfiguracionDTO;
import dto.DispositivoDTO;
import dto.HospitalDTO;
import dto.MedicoDTO;
import dto.PacienteDTO;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import logica.ejb.HospitalLogic;
import logica.ejb.MedicoLogic;
import logica.ejb.PacienteLogic;
import logica.interfaces.IAlertaLogic;
import logica.interfaces.IDispositivo;
/**
 *
 * @author s.ardila13
 */

@Path("alertas")
@Produces("application/json")
@Consumes(MediaType.APPLICATION_JSON)

public class AlertaResources
{
    @Inject
    private PacienteLogic pacienteLogic;
    
    @Inject
    private HospitalLogic hospitalLogic;
    
    @Inject MedicoLogic medicoLogic;
    
    @Inject
    private IAlertaLogic logic;
    
    @Inject
    private IDispositivo dispositivologic;
    
//    @Inject
//    private IConfiguracionlLogic configuracionlogic;
    
    //Requerimiento 1 Recibe info de los sensores...
    
//    @PostConstruct
//    public void init(){
//        logic = new AlertaLogic();
//    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{idDispositivo:\\d+}")
    public void createAlerta(AlertaDTO alerta,@PathParam("idDispositivo") Long idDispositivo)
    {
//        System.out.println("La instancia es " + logic.hashCode());
//       System.out.println("Hola " + idDispositivo);
       dispositivologic.agregarAlerta(idDispositivo, alerta.toEntity());
//        logic.crearAlerta(alerta, idDispositivo);
    }
    
    @GET
    public List<AlertaDTO> darAlertas() {
        return ListToDTO(logic.darAlertas());
    }
    
    @GET
    @Path("/ejemplo")
    public AlertaDTO ejemplo()
    {
        Integer[] presion = new Integer[2];
        presion[0] = 60;
        presion[1] = 80;
        
        Long[] ubicacion = new Long[2];
        ubicacion[0] = 60L;
        ubicacion[1] = 80L;
        
        Integer frecuencia = 85;
        Integer estres = 65;
        
        
        HospitalDTO h = new HospitalDTO("Uniandes");
        hospitalLogic.crear(h.toEntity());
        
        medicoLogic.crearMedico(new MedicoDTO("German", 0L).toEntity());
        
        h.setId(1L);
        PacienteDTO p = new PacienteDTO("Brandon", 20 , h);
        p.setId(1L);
        PacienteDTO e = new PacienteDTO(pacienteLogic.crearPaciente(p.toEntity()));
       
        
        ConfiguracionDTO config = new ConfiguracionDTO("config ");
        config.setId(1L);
        
//        configuracionlogic.crear(config);
        
        
        DispositivoDTO dispositivo = new DispositivoDTO( e, p.getHospital(), config);
//        config.setDispositivo(dispositivo);


        dispositivo.setConfiguration(config);
        dispositivo.setId(1L);
        System.out.println("Paciente en dispositivo" + dispositivo.getPaciente().getId());
        dispositivologic.crearDispositivo(dispositivo.toEntity());
        
        AlertaDTO a = new AlertaDTO(0, presion,frecuencia, estres, /*dispositivo ,*/ AlertaDTO.AMARILLO, ubicacion);
        
        return a;
    }
    
    
    public List<AlertaDTO> ListToDTO(List<AlertaEntity> listaEntidades){
        List<AlertaDTO> lista = new ArrayList<>();
        for(AlertaEntity e: listaEntidades){
            lista.add(new AlertaDTO(e));
        }
        return lista;
    }
    
}
