package com.iscsantoscastillo.ventapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.iscsantoscastillo.ventapp.databinding.ConsignacionesBinding;
import com.iscsantoscastillo.ventapp.databinding.RetirosBinding;

import ViewModels.AddConsignacionesViewModels;
import ViewModels.AddRetirosViewModels;

public class Consignaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consignaciones);
        ConsignacionesBinding binding = DataBindingUtil.setContentView(this, R.layout.consignaciones);
        binding.setAddConsignacionesModel(new AddConsignacionesViewModels(this, binding));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent= new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

