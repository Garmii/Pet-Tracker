package modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String nombre;
    private String correo;
    private String contra;

    public Usuario(int id, String nombre, String correo, String contra) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contra = contra;
    }

    public Usuario() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", contra='" + contra + '\'' +
                '}';
    }
}
