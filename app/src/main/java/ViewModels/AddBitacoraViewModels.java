package ViewModels;

import android.app.Activity;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iscsantoscastillo.ventapp.databinding.BitacoraBinding;
import com.iscsantoscastillo.ventapp.databinding.RetirosBinding;

import java.util.ArrayList;
import java.util.List;

import Interface.IonClick;
import Library.BO.Cliente;
import Library.BO.Empresa;
import Library.BO.VentaBO;
import Library.MemoryData;
import Models.BitacoraModels;

public class AddBitacoraViewModels extends BitacoraModels implements IonClick {

    private BitacoraBinding _binding;
    private VentaBO ventaBO;
    private Empresa empresaBO;
    private Activity _activity;
    private FirebaseFirestore _db;
    private DocumentReference _documentRef;
    private final List<Cliente> lstCliente = new ArrayList<>();
    private MemoryData memoryData;
    private boolean bScrollEnable = true;

    public AddBitacoraViewModels(Activity activity, BitacoraBinding binding){
        _activity = activity;
        _binding = binding;
        memoryData = MemoryData.getInstance(activity);
    }

    @Override
    public void onClick(View view) {

    }
}
