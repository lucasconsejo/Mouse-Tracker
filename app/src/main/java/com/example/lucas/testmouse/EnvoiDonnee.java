package com.example.lucas.testmouse;


import android.content.Intent;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class EnvoiDonnee extends AsyncTask<String, String, Void>{

    Socket s;
    DataOutputStream dos;
    PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids) {

        String donnee = voids[0];
        String donnee2 = voids[1];
        String donnee3 = voids[2];

        try {
            s = new Socket(SelectIP.ip, 7800);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(donnee);
            pw.write(donnee2);
            pw.write(donnee3);
            pw.flush();
            pw.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
