package Library;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    private static Pattern pat = null;
    private static Matcher mat = null;

    public static boolean isMail(String email){
        pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
        mat = pat.matcher(email);
        if(mat.find()){
            return true;
        }else{
            return false;
        }
    }

    public static String generarIDDocumento() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format( new Date()   );
    }

    public static String generarIDColeccion() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return format.format( new Date()   );
    }

    public static String generarIDColeccionRetiro() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return format.format( new Date()   ) + "R";
    }

    public static String generarIDColeccionConsignaciones() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return format.format( new Date()   ) + "C";
    }

    public static String generarIDColeccionBitacora() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return format.format( new Date()   ) + "B";
    }

    public static String generarFechaDeCaptura_ddMMyyyy() {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format( new Date()   );
    }

    public static String generarHoraDeCaptura24H() {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format( new Date()   );
    }

    public static String generarHoraDeCaptura12H() {

        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");
        return format.format( new Date()   );
    }

    public static String getCurrency(double d){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(d);
    }
}
