package com.iscsantoscastillo.ventapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CanvasFirma extends AppCompatActivity {
    MyCanvas myCanvas;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCanvas = new MyCanvas(this, null);
        setContentView(myCanvas);

    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this, Ventas.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
