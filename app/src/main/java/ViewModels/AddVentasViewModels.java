package ViewModels;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.api.client.util.IOUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iscsantoscastillo.ventapp.Correo;
import com.iscsantoscastillo.ventapp.CustomScrollView;
import com.iscsantoscastillo.ventapp.R;
import com.iscsantoscastillo.ventapp.TemplatePDF;
import com.iscsantoscastillo.ventapp.databinding.VentasBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interface.IonClick;
import Library.BO.Cliente;
import Library.BO.Empresa;
import Library.BO.VentaBO;
import Library.MemoryData;
import Library.Validate;
import Models.VentaModels;


public class AddVentasViewModels extends VentaModels implements IonClick {
    private VentaBO ventaBO;
    private Empresa empresaBO;
    private Activity _activity;
    private VentasBinding _binding;
    private FirebaseFirestore _db;
    private DocumentReference _documentRef;
    private final List<Cliente> lstCliente = new ArrayList<>();
    private MemoryData memoryData;
    private boolean bScrollEnable = true;

    //public static String networkErrorMessage = "Network not available";
    //public static boolean checkInternetConnection = true;
    //public static boolean showErrorMessage = true;
    //String mailID = "jaisonfdo@gmail.com";
    //String mailSubject = "Attachment Sample";
    //String TAG = "HomeActivity";

    public AddVentasViewModels(Activity activity, VentasBinding binding){
        _activity = activity;
        _binding = binding;
        memoryData = MemoryData.getInstance(activity);
        _binding.editTextNotas.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Escribe en la propiedad Nota, cuando se teclea.
                if (ventaBO == null) {
                    ventaBO = new VentaBO();
                } else {
                        ventaBO.setNota(_binding.editTextNotas.getText().toString());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }});
        if(isWriteStoragePermissionGranted()){
            existeLogoEnLocal();
            getEmpresa("0");
        }
    }

    private ArrayList<String[]>getClients(){

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        ArrayList<String[]>rows= new ArrayList<>();
        rows.add(new String[] {"1", "Total del corte", formatter.format(ventaBO.getVentaTotalCliente()) });
        rows.add(new String[] {"2", "Ganancia cliente", formatter.format(ventaBO.getGananciaCliente())});

        //rows.add(new String[] {"2", "Ret. lleva", formatter.format(ventaBO.getRetiroLleva()) });
        //rows.add(new String[] {"3", "Ret. deja", formatter.format(ventaBO.getRetiroDeja())});
        //rows.add(new String[] {"4", "Consigna base", formatter.format(ventaBO.getConsignaBase())});
        //rows.add(new String[] {"5", "Consigna finanzas", formatter.format(ventaBO.getConsignaFinanzas())});
        //rows.add(new String[] {"6", "Corte", formatter.format(ventaBO.getCorte())});
        //rows.add(new String[] {"7", "Cuota", formatter.format(ventaBO.getCuota())});
        //rows.add(new String[] {"8", "Propina", formatter.format(ventaBO.getPropina())});
        //rows.add(new String[] {"9", "Evento", formatter.format(ventaBO.getEvento())});
        //rows.add(new String[] {"10", "Base", formatter.format(ventaBO.getBaseMaquina())});
        //rows.add(new String[] {"11", "Premio", formatter.format(ventaBO.getPremio())});
        //rows.add(new String[] {"12", "Otro", formatter.format(ventaBO.getOtro())});
        //rows.add(new String[] {"13", "Venta total", formatter.format(ventaBO.getVentaTotal())});

        return rows;
    }

    @Override
    public void onClick(View view){

        switch (view.getId()){
            case R.id.buttonGuardarVenta:

                //_binding.buttonGuardarVenta.setEnabled(false);
                if (isCamposCompletos()){
                    //validar que no admita negativos
                    registrarVentas();
                }else break;



                //Guardar imagen PNG de MyCanvas (firma.png)
                _binding.myCanvas.guardarImagen();

                //Permisos de escritura/lectura
                //Generacion del PDF desde Template
                TemplatePDF templatePDF = new TemplatePDF(_activity.getApplicationContext());
                if(isWriteStoragePermissionGranted()){
                    String [] header ={"No.", "Descripción", "Importe"};
                    String shortText = "Responsable: " + ventaBO.getCorreo();
                    //String longText = "Nota: " + ventaBO.getNota();

                    templatePDF.setImageFile(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF" + File.separator + "firma.png");
                    templatePDF.setImageLogo(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF" + File.separator + "logo.png");
                    templatePDF.setNombrePDF(ventaBO.getIdVenta() + ".pdf");
                    templatePDF.setPieFirma("Firma del cliente (" + ventaBO.getNombreCliente() + ")");
                    templatePDF.openDocument();
                    templatePDF.addMetaData("Clientes","Ventas", "VentAPP");
                    templatePDF.addTitles(
                            "VIG Entretenimiento",
                            "Venta " + ventaBO.getIdVenta(),
                            ventaBO.getFecha() + " " + ventaBO.getHora(),
                            ventaBO.getIdCliente() + " - " +  ventaBO.getNombreCliente() + "(" + ventaBO.getPorcentaje() + "%)");
                    templatePDF.addParagraph(shortText);
                    //templatePDF.addParagraph(longText);
                    templatePDF.createTable(header, getClients());
                    templatePDF.closeDocument();
                    //En produccion, hay que quitar la linea de abajo
                    //templatePDF.viewPDF();
                }


                //Se sube el PDF al Storage en Firebase en la ruta: pdf/
                subirArchivoAlStorage();
                //getUrlDescarga(ventaBO.getIdVenta());


                if(Correo.enviarCorreo(
                        "" + empresaBO.getCorreo(),
                        "" + empresaBO.getContrasena(),
                        "" + empresaBO.getNombre() +" Ticket de venta #" + ventaBO.getIdVenta(),
                        "Contenido",
                        "Estimado cliente " + ventaBO.getNombreCliente() + ", reciba por parte de " + empresaBO.getNombre() + " su ticket de venta digital con folio "+ventaBO.getIdVenta()+" del día " + ventaBO.getFecha() + " " + ventaBO.getHora() + ".",
                        Environment.getExternalStorageDirectory().toString() + File.separator +
                                "PDF" + File.separator +
                                ventaBO.getIdVenta() + ".pdf",
                        "" + empresaBO.getCorreo(),
                        "" + ventaBO.getCorreoEnvio() + "," + empresaBO.getCorreoCopia())
                ) {
                    _binding.myCanvas.limpiarCanvas();
                    msg("Correo enviado exitosamente.");
                }else{
                    //msg("ERROR al enviar correo.");
                }

                //* * * Procesos de limpieza de campos y objetos

                limpiarCampos();
                ventaBO = null;
                _binding.editTextIdCliente.requestFocus();
                //* * * Procesos de limpieza de campos y objetos


                break;
            case R.id.buttonAddMovimiento:
                if(isCamposCompletos()) {
                    if (_binding.editTextImporteMovimiento.getText().toString().equals("")) {
                        msg("Se requiere capturar el importe del movimiento");
                    } else {
                        addMovimiento();
                    }
                }
                break;
            case R.id.btnBuscarCliente:
                _binding.editTextNombreCliente.setText("Consultando en la base, espere...");
                String str = idClienteUI.getValue();
                getCliente(str);
                break;
            case R.id.buttonLimpiarCanvas:
                _binding.myCanvas.limpiarCanvas();
                break;
            case R.id.buttonScroll:
                bScrollEnable = !bScrollEnable;
                ((CustomScrollView)_binding.scrollGeneral).setEnableScrolling(bScrollEnable);
                if(bScrollEnable){
                    _binding.buttonScroll.setText("Bloquear scroll");
                }else{
                    _binding.buttonScroll.setText("Desbloquear scroll");
                }
                break;
        }
    }

    public boolean existeLogoEnLocal(){
        File dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF");
        if(dir.exists()){
            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF" + File.separator + "logo.png");
            if(!file.exists()){
                descargarLogoDesdeStorage();
                return true;
            }else return true;
        }else{
            if(dir.mkdirs()){
                descargarLogoDesdeStorage();
                return true;
            }else return false;
        }
    }

    public void descargarLogoDesdeStorage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("logo/ic_logo2.png");
        File localFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF" + File.separator + "logo.png");

        File finalLocalFile = localFile;
        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                try {
                    final boolean newFile = finalLocalFile.createNewFile();
                    msg("Se descargo el logo del Storage.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                msg("Error, no se descargo el logo del Storage.");
            }
        });

        // Create a reference to a file from a Google Cloud Storage URI
        //StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");

        // Create a reference from an HTTPS URL
        // Note that in the URL, characters are URL escaped!
        //StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");
    }

    public void subirArchivoAlStorage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference spaceRef = storageRef.child("pdf" + File.separator + ventaBO.getIdVenta() + ".pdf");
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF" + File.separator +
                    ventaBO.getIdVenta() + ".pdf"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = spaceRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                //msg("Error, no se guardo pdf en storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                //msg("Se guardo pdf en storage");
            }
        });
        //Segmento que si funciona y si devuelve un url de descarga
        /*Uri file = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + File.separator + "PDF" + File.separator +
                ventaBO.getIdVenta() + ".pdf"));
        final StorageReference ref = storageRef.child("pdf" + File.separator + ventaBO.getIdVenta() + ".pdf");
        uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });*/
    }
    //este metodo no pone el archivo al final.
    public void getUrlDescarga(String strNombrePDF){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("pdf" + File.separator + strNombrePDF + ".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                storageRef.getDownloadUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


    public void msg(String _msg){
        Toast.makeText(_activity, _msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isWriteStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (_activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(_activity, new String[]{
                        android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }else{
            return true;
        }

    }






    private void registrarVentas(){
        String strCorreo = memoryData.getData("user");
        ventaBO.setCorreo(strCorreo);
        String sIdColeccion = Validate.generarIDColeccion();
        String sIdVenta = Validate.generarIDDocumento();
        _db = FirebaseFirestore.getInstance();
        _documentRef = _db.collection(sIdColeccion).document(sIdVenta);//genera algo como 201901(coleccion) 20190122152035(venta)
        ventaBO.setIdVenta(sIdVenta);
        Map<String, Object> venta = new HashMap<>();
        venta.put("idVenta", sIdVenta);
        venta.put("idCliente", ventaBO.getIdCliente());//idClienteUI.getValue()
        venta.put("correo", ventaBO.getCorreo());
        venta.put("correoEnvio", ventaBO.getCorreoEnvio());

        if(ventaTotalUI.getValue().equals("")) venta.put("ventaTotal", "0");
        else venta.put("ventaTotal", ventaTotalUI.getValue());
        ventaBO.setNota(_binding.editTextNotas.getText().toString());
        venta.put("notas", notasUI.getValue());
        venta.put("movimientos",ventaBO.getMovimientos());
        venta.put("gananciaCliente",ventaBO.getGananciaCliente());
        venta.put("ventaTotalCliente", ventaBO.getVentaTotalCliente());


        //Movimientos POSITIVOS
        venta.put("corte",ventaBO.getCorte());
        venta.put("retiro",ventaBO.getRetiro());//retiroDeja
        venta.put("retiroLleva",ventaBO.getRetiroLleva());
        venta.put("consignaBase",ventaBO.getConsignaBase());
        venta.put("consignaFinanzas",ventaBO.getConsignaFinanzas());
        venta.put("urlPDF","");
        if(ventaBO.getHora() == null) ventaBO.setHora(Validate.generarHoraDeCaptura12H());
        if(ventaBO.getHora24() == null) ventaBO.setHora24(Validate.generarHoraDeCaptura24H());
        if(ventaBO.getFecha() == null) ventaBO.setFecha(Validate.generarFechaDeCaptura_ddMMyyyy());

        venta.put("hora24",ventaBO.getHora24());



        //Movimientos NEGATIVOS
        venta.put("cuota_",ventaBO.getCuota());
        venta.put("propina_",ventaBO.getPropina());
        venta.put("evento_",ventaBO.getEvento());
        venta.put("basemaquina_",ventaBO.getBaseMaquina());
        venta.put("premio_",ventaBO.getPremio());
        venta.put("otro_",ventaBO.getOtro());
        venta.put("porc",ventaBO.getPorcentaje());
        venta.put("nombreCliente", ventaBO.getNombreCliente());
        venta.put("status", "A");//A activo, C cancelado
        //descomentar para poder enviar a la base de datos
        _documentRef.set(venta).addOnCompleteListener((task) -> {});

        //Toast.makeText(_activity, "Se guardo" + sIdVenta, Toast.LENGTH_SHORT).show();
        //estas 3 lineas deben ir despues de enviar el correo... ya están despues del envio del correo
        //limpiarCampos();
        //ventaBO = null;
        //_binding.editTextIdCliente.requestFocus();
    }

    private void limpiarCampos(){
        _binding.editTextIdCliente.setText("");
        _binding.editTextNombreCliente.setText("");
        _binding.editTextVentaTotal.setText("");
        _binding.editTextNotas.setText("");
        _binding.spinnerMovView.setAdapter(null);
        _binding.editPorc.setText("0");
        _binding.editTextGananciaCli.setText("");
        _binding.textFirma.setText("Ponga su firma por favor");
        bScrollEnable = true;
        ((CustomScrollView)_binding.scrollGeneral).setEnableScrolling(bScrollEnable);
        _binding.buttonScroll.setText("Bloquear scroll");
    }

    private void addMovimiento(){

        //Si toman la misma llave 2 veces, se sobre escribe la anterior.
        String sLlave =_activity.getResources().getStringArray(R.array.item_movimientos)[item.getSelectedItemPosition()];
        String sValue = importeMovimientoUI.getValue();
        String strEmailUsuario = memoryData.getData("user");
        double dVentaTotal;

        if(ventaBO == null) {
            ventaBO = new VentaBO();
        }

        if(sLlave.charAt(sLlave.length()-1) == '_'){
            sValue = "-" + sValue;
        }

        ventaBO.getMovimientos().put(sLlave, sValue);
        //ventaBO.setIdCliente(idClienteUI.getValue());
        ventaBO.setFecha(Validate.generarFechaDeCaptura_ddMMyyyy());
        ventaBO.setHora(Validate.generarHoraDeCaptura12H());
        ventaBO.setHora24(Validate.generarHoraDeCaptura24H());


        //Aplicando porcentaje de descuento del cliente.
        dVentaTotal = llenarSpinner(ventaBO.getMovimientos());
        //Se trata de un total visible al cliente, antes de calcular ganancias.
        ventaBO.setVentaTotalCliente(dVentaTotal);
        ventaBO.setGananciaCliente( dVentaTotal * ( Double.parseDouble(ventaBO.getPorcentaje()) / 100 ));
        dVentaTotal = dVentaTotal - dVentaTotal * ( Double.parseDouble(ventaBO.getPorcentaje()) / 100);
        ventaBO.setVentaTotal(dVentaTotal);

        ventaBO.setCorreo(strEmailUsuario);

        //ventaBO.setPorcentaje(_binding.editPorc.getText().toString());
        //ventaBO.setNombreCliente(_binding.editTextNombreCliente.getText().toString());
        //Escribiendo en el campo ventaTotal
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        _binding.editTextGananciaCli.setText("$" + String.valueOf(df.format(ventaBO.getGananciaCliente())));
        _binding.editTextVentaTotal.setText(String.valueOf(df.format(ventaBO.getVentaTotal())));
        _binding.textFirma.setText("Ganancia cliente: " + Validate.getCurrency(ventaBO.getGananciaCliente()) + " Total de corte: " + Validate.getCurrency(ventaBO.getVentaTotalCliente()));
        _binding.editTextImporteMovimiento.setText("");
        _binding.editTextImporteMovimiento.requestFocus();
    }

    private double llenarSpinner(Map map){
        double dTotal = 0;
        Object[] aObjKey = map.keySet().toArray();
        Object[] aObjVal = map.values().toArray();
        String[] arraySpinner = new String[aObjKey.length];

        for(int i = 0 ; i < aObjKey.length ; i ++){
            dTotal = dTotal + Double.parseDouble(aObjVal[i].toString());
            arraySpinner[i] = aObjKey[i] + " = " + aObjVal[i];

            switch (aObjKey[i].toString()) {
                case "Retiro":
                    ventaBO.setRetiro(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "RetiroLleva":
                    ventaBO.setRetiroLleva(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "ConsignaBase":
                    ventaBO.setConsignaBase(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "ConsignaFinanzas":
                    ventaBO.setConsignaFinanzas(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "Corte":
                    ventaBO.setCorte(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "Couta_":
                    ventaBO.setCuota(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "Propina_":
                    ventaBO.setPropina(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "Evento_":
                    ventaBO.setEvento(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "BaseMaquina_":
                    ventaBO.setBaseMaquina(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "Premio_":
                    ventaBO.setPremio(Double.parseDouble(aObjVal[i].toString()));
                    break;
                case "Otro_":
                    ventaBO.setOtro(Double.parseDouble(aObjVal[i].toString()));
                    break;
            }
        }

        Spinner s = _binding.spinnerMovView;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                _activity,
                android.R.layout.simple_spinner_item,
                arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        return dTotal;
    }

    private void getCliente(String id){

        _db = FirebaseFirestore.getInstance();//no borrar esta linea.
        _db.collection("clientes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Cliente> clientesList = task.getResult().toObjects(Cliente.class);
                                for (Cliente cliente : clientesList) {
                                    if(cliente.getId().equals(_binding.editTextIdCliente.getText().toString())){

                                        if(ventaBO == null) {
                                            ventaBO = new VentaBO();
                                        }

                                        ventaBO.setIdCliente(cliente.getId());
                                        ventaBO.setNombreCliente(cliente.getNombre());
                                        ventaBO.setPorcentaje(cliente.getPorc());
                                        ventaBO.setCorreoEnvio(cliente.getCorreoCliente());

                                        _binding.editTextNombreCliente.setText(cliente.getNombre());
                                        _binding.editPorc.setText(cliente.getPorc()+"%");
                                        return;
                                    }
                                }
                                _binding.editTextNombreCliente.setText("");
                            }
                        }else {
                            _binding.editTextNombreCliente.setText("");
                            Toast.makeText(_activity, "Error al descargar datos de Clientes, intente de nuevo.", Toast  .LENGTH_SHORT).show();
                        }
                    }});
    }

    private void getEmpresa(String id){

        _db = FirebaseFirestore.getInstance();//no borrar esta linea.
        _db.collection("empresa")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                List<Empresa> empresaList = task.getResult().toObjects(Empresa.class);
                                for (Empresa empresa : empresaList) {
                                    if(empresa.getId().equals("0")){

                                        if(empresaBO == null) {
                                            empresaBO = new Empresa();
                                        }

                                        empresaBO.setId(empresa.getId());
                                        empresaBO.setNombre(empresa.getNombre());
                                        empresaBO.setCorreo(empresa.getCorreo());
                                        empresaBO.setCorreoCopia(empresa.getCorreoCopia());
                                        empresaBO.setContrasena(empresa.getContrasena());

                                        return;
                                    }
                                }

                            }
                        }else {
                            _binding.editTextNombreCliente.setText("");
                            Toast.makeText(_activity, "Error al descargar datos de Empresa, intente de nuevo.", Toast  .LENGTH_SHORT).show();
                        }
                    }});
    }

    private boolean isCamposCompletos(){
        boolean b = true;

        if(_binding.editTextIdCliente.getText().toString().trim().equals("")){
            b = false;
            Toast.makeText(_activity, "Se requiere capturar el campo: Id cliente", Toast.LENGTH_SHORT).show();
        }else if(_binding.editTextNombreCliente.getText().toString().trim().equals("")){
            b = false;
            Toast.makeText(_activity, "Se requiere capturar el campo: Nombre Cliente", Toast.LENGTH_SHORT).show();
        }/*else if(ventaBO != null) {
            if(ventaBO.getVentaTotal() == 0) {
                b = false;
                Toast.makeText(_activity, "Se requiere capturar movimientos", Toast.LENGTH_SHORT).show();
            }
        }else{
            b = false;
            Toast.makeText(_activity, "Se requiere capturar movimientos", Toast.LENGTH_SHORT).show();
        }*/

        return b;
    }


}
