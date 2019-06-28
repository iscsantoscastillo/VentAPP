package Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.iscsantoscastillo.ventapp.BR;

public class Item extends BaseObservable {
    private int selectedItemPosition;

    @Bindable
    public int getSelectedItemPosition(){
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition){
        this.selectedItemPosition = selectedItemPosition;
        notifyPropertyChanged(BR.selectedItemPosition);
    }
}
