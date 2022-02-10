package modelo;

import java.io.Serializable;

public class Animal implements Serializable {

    private int id;
    private int idUsuario;
    private String nombre;
    private int anyo;
    private int mes;
    private String especie;
    private String raza;
    private String sexo;

    public Animal(int id, int idUsuario, String nombre, int anyo, int mes, String raza, String sexo,String especie) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.anyo = anyo;
        this.mes = mes;
        this.raza = raza;
        this.sexo = sexo;
        this.especie = especie;
    }

    public Animal() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", anyo=" + anyo +
                ", mes=" + mes +
                ", especie='" + especie + '\'' +
                ", raza='" + raza + '\'' +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}
