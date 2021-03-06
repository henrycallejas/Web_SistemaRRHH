/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitlab.manejadores;
import com.bitlab.controladores.UsuarioControlador;
import com.bitlab.entidades.Usuario;
import com.bitlab.utilidades.Utilidades;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
/**
 *
 * @author henry
 */
@ManagedBean
@SessionScoped
public class InicioSesionManejador implements Serializable {
    private Usuario usuario;
    private UsuarioControlador usuarioControlador; 
    private Usuario us;
    private String fechaConexionUltima;
    private boolean esRh; //bandera para saber si es usuario tipo rrhh o admin
    /**
     * Creates a new instance of InicioSesionManejador
     */
    public InicioSesionManejador() {
        usuario = new Usuario();
        us = new Usuario();
        usuarioControlador = new UsuarioControlador();
    }
    
    //Metodo para validar usuario y contrasena y de esta forma iniciar la sesion
    //Redirige a index
    public void iniciarSesion() {
        try {
            us = usuarioControlador.encontrarPorUsuario(usuario.getUsuNombre(), usuario.getUsuContrasena());
            if (us != null) {
                if (usuario.getUsuNombre().equals(us.getUsuNombre()) && usuario.getUsuContrasena().equals(us.getUsuContrasena())) {
                    System.out.println("Bienvenido " + us.getUsuNombre() + " usted es un usuario de tipo " + us.getRolIdFk().getRolNombrerol());
                    esRRHH();
                    Utilidades.redireccionar("index");
                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                    fechaConexionUltima = formateador.format(us.getUsuFechaConexion());
                    us.setUsuFechaConexion(new Date());
                    editarFechaConexion();
                }
            } else {
                Utilidades.lanzarMensaje(FacesMessage.SEVERITY_ERROR, "Usuario invalido", "Usuario o contrasena son invalidos");
                Utilidades.redireccionar("login");
                cerrarSesion();
            }
        } catch (Exception ex) {
            Logger.getLogger(InicioSesionManejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Valida la sexion y si es nula lo redigire al login
    public void validarSesion(){
        try {
            Usuario usValidado = usuarioControlador.encontrarPorUsuario(us.getUsuNombre(), us.getUsuContrasena());
            if(usValidado == null){
                Utilidades.redireccionar("login");
            }
        } catch (Exception ex) {
            Logger.getLogger(InicioSesionManejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Metodo para setear la fecha de conexion de cada usuario
    public void editarFechaConexion() {
        try {
            usuarioControlador.editar(this.us);
        } catch (Exception ex) {
            Logger.getLogger(InicioSesionManejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cerrarSesion(){
        us = null;
        usuario = null;
    }
    
    //Metodo para setear la variable boolean esRh si es usuario de tipo rol RRHH, sino lo setea a false
    public void esRRHH(){
        if(us.getRolIdFk().getRolNombrerol().equals("RRHH")){
            esRh = true;
        }
        else esRh = false;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public UsuarioControlador getUsuarioControlador() {
        return usuarioControlador;
    }
    public void setUsuarioControlador(UsuarioControlador usuarioControlador) {
        this.usuarioControlador = usuarioControlador;
    }
    public Usuario getUs() {
        return us;
    }
    public void setUs(Usuario us) {
        this.us = us;
    }
    public String getFechaConexionUltima() {
        return fechaConexionUltima;
    }
    public void setFechaConexionUltima(String fechaConexionUltima) {
        this.fechaConexionUltima = fechaConexionUltima;
    }

    public boolean isEsRh() {
        return esRh;
    }

    public void setEsRh(boolean esRh) {
        this.esRh = esRh;
    }
    
}