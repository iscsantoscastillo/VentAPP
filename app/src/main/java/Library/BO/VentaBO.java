package Library.BO;

import java.util.HashMap;
import java.util.Map;

public class VentaBO {
    private String idVenta;
    private String fecha;
    private String hora;
    private String hora24;
    private String idCliente;
    private String idEmpleado;
    private double ventaTotal=0;
    private double ventaTotalCliente=0;
    private double gananciaCliente=0;
    private String nota;
    private String geoPos;
    private Map<String, String> movimientos;
    private String correo;
    private String porcentaje;
    private String nombreCliente;
    private double longitude;
    private double latitude;
    private String pathFirma;
    private String pathPDF;
    private String correoEnvio;
    private String urlDescargaPDF;



    private double retiro =0;//retiroDeja
    private double retiroLleva =0;
    private double consignaBase =0;
    private double consignaFinanzas =0;
    private double corte=0;
    private double cuota=0;
    private double propina=0;
    private double evento=0;
    private double baseMaquina=0;
    private double premio=0;
    private double otro=0;

    //Propiedades de Consignaciones
    private double desbanco=0;
    private double provisional=0;
    private double corteDejado=0;
    private double anticipo=0;
    private double pago=0;


    public VentaBO(){
        this.movimientos = new HashMap<>();
    }



    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public double getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(double ventaTotal) {
        this.ventaTotal = ventaTotal;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getGeoPos() {
        return geoPos;
    }

    public void setGeoPos(String geoPos) {
        this.geoPos = geoPos;
    }

    public Map<String, String> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Map<String, String> movimientos) {
        this.movimientos = movimientos;
    }

    public double getRetiro() {
        return retiro;
    }

    public void setRetiro(double retiroDeja) {
        this.retiro = retiroDeja;
    }

    public double getRetiroLleva() {
        return retiroLleva;
    }

    public void setRetiroLleva(double retiroLleva) {
        this.retiroLleva = retiroLleva;
    }

    public double getConsignaBase() {
        return consignaBase;
    }

    public void setConsignaBase(double consignaBase) {
        this.consignaBase = consignaBase;
    }

    public double getCorte() {
        return corte;
    }

    public void setCorte(double corte) {
        this.corte = corte;
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
    }

    public double getPropina() {
        return propina;
    }

    public void setPropina(double propina) {
        this.propina = propina;
    }

    public double getEvento() {
        return evento;
    }

    public void setEvento(double evento) {
        this.evento = evento;
    }

    public double getBaseMaquina() {
        return baseMaquina;
    }

    public void setBaseMaquina(double baseMaquina) {
        this.baseMaquina = baseMaquina;
    }

    public double getPremio() {
        return premio;
    }

    public void setPremio(double premio) {
        this.premio = premio;
    }

    public double getOtro() {
        return otro;
    }

    public void setOtro(double otro) {
        this.otro = otro;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getGananciaCliente() {
        return gananciaCliente;
    }

    public void setGananciaCliente(double gananciaCliente) {
        this.gananciaCliente = gananciaCliente;
    }

    public double getConsignaFinanzas() {
        return consignaFinanzas;
    }

    public void setConsignaFinanzas(double consignaFinanzas) {
        this.consignaFinanzas = consignaFinanzas;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPathFirma() {
        return pathFirma;
    }

    public void setPathFirma(String pathFirma) {
        this.pathFirma = pathFirma;
    }

    public String getPathPDF() {
        return pathPDF;
    }

    public void setPathPDF(String pathPDF) {
        this.pathPDF = pathPDF;
    }

    public String getCorreoEnvio() {
        return correoEnvio;
    }

    public void setCorreoEnvio(String correoEnvio) {
        this.correoEnvio = correoEnvio;
    }

    public double getVentaTotalCliente() {
        return ventaTotalCliente;
    }

    public void setVentaTotalCliente(double ventaTotalCliente) {
        this.ventaTotalCliente = ventaTotalCliente;
    }

    public String getUrlDescargaPDF() {
        return urlDescargaPDF;
    }

    public void setUrlDescargaPDF(String urlDescargaPDF) {
        this.urlDescargaPDF = urlDescargaPDF;
    }

    public String getHora24() {
        return hora24;
    }

    public void setHora24(String hora24) {
        this.hora24 = hora24;
    }

    public double getDesbanco() {
        return desbanco;
    }

    public void setDesbanco(double desbanco) {
        this.desbanco = desbanco;
    }

    public double getProvisional() {
        return provisional;
    }

    public void setProvisional(double provisional) {
        this.provisional = provisional;
    }

    public double getCorteDejado() {
        return corteDejado;
    }

    public void setCorteDejado(double corteDejado) {
        this.corteDejado = corteDejado;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    public double getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(double anticipo) {
        this.anticipo = anticipo;
    }
}
