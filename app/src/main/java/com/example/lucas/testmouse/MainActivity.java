package com.example.lucas.testmouse;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener{

    private EditText saisie;
    private View fond;
    private LinearLayout linearLayout;
    private float xClic, yClic, xLt, yLt, xRl, yRl, point1X, point1Y, point2X, point2Y;
    private float posInitial[] = new float[2];
    private boolean firstTouch = false;
    private long time;
    private String doubleClick= "simpleClick";
    private String lettre ="";
    private int lettre3 = 0;
    private RelativeLayout relativeLayout;
    private TextView btn_retour;

    private ImageButton btn_clavier, right_click, left_click;
    private boolean click = true;
    private Activity activity;

    private RewardedVideoAd mVideoAd;

    private ImageView pointeur;
    private ObjectAnimator animX,animY;
    private AnimatorSet animSetXY;
    //private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Déplacez votre doigt dans la zone", Toast.LENGTH_LONG).show();

        relativeLayout = (RelativeLayout) findViewById(R.id.RelativeActivity);
        xRl = relativeLayout.getX();
        yRl = relativeLayout.getY();

        linearLayout =(LinearLayout) findViewById(R.id.linearLayout);
        xLt = linearLayout.getX();
        yLt = linearLayout.getY();

        pointeur = (ImageView) findViewById(R.id.pointeur) ;

        btn_retour = (TextView) findViewById(R.id.btn_retour);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");
        mVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mVideoAd.setRewardedVideoAdListener(this);

        mVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());

        btn_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_retour.setEnabled(false);
                if(mVideoAd.isLoaded()) {
                    mVideoAd.show();
                }
            }
        });
        /*gestureDetectorCompat = new GestureDetectorCompat(this, this);
        gestureDetectorCompat.setOnDoubleTapListener(this);*/

        saisie = (EditText) findViewById(R.id.saisie);
        saisie.setText("");
        saisie.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EnvoiDonnee envoi = new EnvoiDonnee();

                lettre = String.valueOf(saisie.getText());
                int length = lettre.length();

                if(length > 0){
                    String lettre2 = lettre.substring(length-1, length);
                    envoi.execute("0_", "0_", "simpleClick" +"_"+ lettre2);
                    saisie.setText("");
                }

                if(lettre3 >61 && lettre3 <69){
                    envoi.execute("0_", "0_", "simpleClick" +"_"+ String.valueOf(lettre3));
                    lettre3 = 0;
                    saisie.setText("");
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_clavier = (ImageButton) findViewById(R.id.btn_clavier);
        btn_clavier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug : ", "ok");
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(saisie, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        right_click = (ImageButton) findViewById(R.id.right_click);
        right_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnvoiDonnee envoi = new EnvoiDonnee();
                envoi.execute("0.0_", "0.0_", "right-click" +"_  ");
            }
        });

        left_click = (ImageButton) findViewById(R.id.left_click);
        left_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnvoiDonnee envoi = new EnvoiDonnee();
                envoi.execute("0.0_", "0.0_", "doubleClick" +"_  ");
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        //this.gestureDetectorCompat.onTouchEvent(event);
        EnvoiDonnee envoi = new EnvoiDonnee();

        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if(posInitial[0] == 0 && posInitial[1] == 0){
                posInitial[0] = event.getRawX();
                posInitial[1] = event.getRawY();
            }

            xClic = event.getRawX();
            yClic = event.getRawY();

            float sourisX = (xClic - posInitial[0])/13;
            float sourisY = (yClic - posInitial[1])/13;

            envoi.execute(String.valueOf(sourisX) +"_", String.valueOf(sourisY)+ "_","  _  ");

            pointeur.setX(xClic-50);
            pointeur.setY(yClic-50);
            pointeur.setVisibility(ImageView.VISIBLE);
        }
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            posInitial[0] = 0;
            posInitial[1] = 0;

            xClic = 0;
            yClic = 0;

            pointeur.setVisibility(ImageView.INVISIBLE);

            if(firstTouch && (System.currentTimeMillis() - time) <= 200) {
                //do stuff here for double tap
                doubleClick= "doubleClick";
                //fond.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Log.d("** DOUBLE TAP**"," second tap ");
                envoi.execute("0_", "0_", "doubleClick" +"_  ");
                firstTouch = false;

            } else {
                firstTouch = true;
                doubleClick= "simpleClick";
                //envoi.execute("0_", "0_", doubleClick +"_  ");
                time= System.currentTimeMillis();
                return false;
            }
        }

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            pointeur.setX(xClic-50);
            pointeur.setY(yClic-50);
            pointeur.setVisibility(ImageView.VISIBLE);
        }
        return false; //super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event){

        if(keyCode  == KeyEvent.KEYCODE_ENTER){

            Log.d("CODE KEY : ", " "+keyCode);
            lettre3 = keyCode;
        }

        if(keyCode  == KeyEvent.KEYCODE_DEL){

            Log.d("CODE KEY : ", " "+keyCode);
            lettre3 = keyCode;
        }
        lettre3 = keyCode;
//Do something....
        return super.onKeyUp(keyCode, event);
    }
    public void Retour(){
        Intent seletIp = new Intent(this, SelectIP.class);
        startActivity(seletIp);
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        btn_retour.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        Retour();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
    /*@Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Scroll : ", "x1 : "+ e1.getRawX() +" y1 : "+e1.getRawY()+" x2 : "+e2.getRawX() +" y2 : "+ e2.getRawY() + " "+ distanceX + " "+ distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }*/
}
