/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;
import Entites.AlertaEntity;
import Entites.HistorialEntity;
import Entites.PacienteEntity;
import dto.AlertaDTO;
import dto.HistorialDTO;
import dto.HospitalDTO;
import dto.PacienteDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import logica.ejb.PacienteLogic;
import logica.interfaces.IPaciente;

/**
 *
 * @author s.ardila13
 */

@Path("/paciente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PacienteResources 
{
    @Inject
    private IPaciente logic;
    
    //Requerimiento 1 Recibe info de los sensores...
    @GET
    @Path("/historial-fechas/{idPaciente:\\d+}/{fechaInicio:\\d+}/{fechaFinal:\\d+}")
    public List<AlertaDTO> getHistorialPorRango(@PathParam("idPaciente") long idPaciente, @PathParam("fechaInicio") Long fechaInicio, @PathParam("fechaFinal") Long fechaFinal)  
    {
        List<AlertaDTO> lista = new ArrayList<>();
        List<AlertaEntity> listaEntidades = logic.getHistorialPorRango(idPaciente, new Date(fechaInicio), new Date(fechaFinal));
        for(AlertaEntity a : listaEntidades){
            lista.add(new AlertaDTO(a));
        }
        return lista;
    }
    
    //CRUD
    @POST
    public void createPaciente (PacienteDTO p )
    {
        System.out.println("Paciente: " + p.toString());
        logic.crearPaciente(p.toEntity());
    }
    
    @GET
    @Path("ejemplo")
    public PacienteDTO getPaciente()
    {
        return new PacienteDTO("Brandon", 19, new HospitalDTO(logic.buscarPaciente(1L).getHospital()));
    }
    
    @GET
    @Path("/{idPaciente:\\d+}")
    public PacienteDTO getPaciente(@PathParam("idPaciente")Long idPaciente)
    {
        System.out.println("Resoruces " + idPaciente);
        return new PacienteDTO(logic.buscarPaciente(idPaciente));
    }
    
    @GET
    public List<PacienteDTO> getPacientes()
    {
        List<PacienteDTO> lista = new ArrayList<>();
        List<PacienteEntity> listaEntidades = new ArrayList<>();
        for(PacienteEntity p : listaEntidades){
            lista.add(new PacienteDTO(p));
        }
        return lista;
    }
            
    @PUT
    @Path("{idPaciente:\\d+}")
    public PacienteDTO upDatePaciente(@PathParam("idPaciente") long idPaciente, PacienteDTO p)
    {
        return new PacienteDTO(logic.modificarPaciente(idPaciente, p.toEntity()));
    }
    
    @PUT
    @Path("{idPaciente:\\d+}/historial")
    public HistorialDTO upDateHistorialPaciente(@PathParam("idPaciente") Long idPaciente, HistorialDTO h)
    {
        HistorialEntity entidad = logic.modificarHistorialPaciente(idPaciente, h.toEntity());
        return new HistorialDTO(entidad);
    }
    
    @DELETE
    @Path("{idPaciente:\\d+}/")
    public void deletePaciente(@PathParam("idPaciente")Long idPaciente)
    {
        logic.eliminarPaciente(idPaciente);
    }
    
    @GET
    @Path("{idPaciente:\\d+}/historial")
    public HistorialDTO getHistorialClinico (@PathParam("idPaciente") long idPaciente)
    {
        PacienteDTO p = new PacienteDTO(logic.buscarPaciente(idPaciente));
        List<AlertaDTO> l= new ArrayList<>();
        List<AlertaEntity> listaEntidades = logic.getHistorialPorRango(idPaciente, new Date(1287207658406l), new Date(1587207658406l));
        for(AlertaEntity e : listaEntidades){
            l.add(new AlertaDTO(e));
        }
        HistorialDTO d = p.getHistorialClinico();
        d.setAlertas(l);
        return d;
    }
    
}
