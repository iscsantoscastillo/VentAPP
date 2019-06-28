package Library.BO;

public class Cliente {
    private String id;
    private String nombre;
    private String porc;
    private String correoCliente;

    public Cliente(){}

    public Cliente(String id, String nombre, String porc){
        this.id = id;
        this.nombre = nombre;
        this.porc = porc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPorc() {
        return porc;
    }

    public void setPorc(String porc) {
        this.porc = porc;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }
}
