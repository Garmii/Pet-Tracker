package modelo;

public class Seguimiento {

    private int id;
    private int idAnimal;
    private String tipo;
    private int peso;
    private String descripcion;
    private String fecha;
    private String sexo;

    public Seguimiento() {
    }

    public Seguimiento(int id, int idAnimal, String tipo, int peso, String descripcion, String fecha, String sexo) {
        this.id = id;
        this.idAnimal = idAnimal;
        this.tipo = tipo;
        this.peso = peso;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.sexo = sexo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Seguimiento{" +
                "id=" + id +
                ", idAnimal=" + idAnimal +
                ", tipo='" + tipo + '\'' +
                ", peso=" + peso +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}
