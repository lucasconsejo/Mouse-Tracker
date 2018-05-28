package com.example.lucas.testmouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class SelectIP extends AppCompatActivity {

    private Button btn_connexion;
    private EditText saisieIp;
    static String ip;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ip);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        saisieIp= (EditText) findViewById(R.id.adress_ip);

        btn_connexion = (Button) findViewById(R.id.btn_connexion);
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connexion();
            }
        });
    }

    public void Connexion(){
        ip = String.valueOf(saisieIp.getText());
        Intent activity_souris = new Intent(this, MainActivity.class);
        startActivity(activity_souris);
    }

    public String getIp(){
        return ip;
    }

}
