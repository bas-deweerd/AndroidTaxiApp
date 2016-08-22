package com.taxiapp.thetaxicompany;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxiapp.thetaxicompany.LoginUtil;
import com.taxiapp.thetaxicompany.models.MapsActivity;
import com.taxiapp.thetaxicompany.models.Welcome;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText Eemail, Epassword;
    private TextView signUp;
    private Button Blogin;
    private ImageView imageView;

    String emailString;
    String passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        Eemail = (EditText) findViewById(R.id.email);
        Epassword= (EditText) findViewById(R.id.password);
        imageView = (ImageView) findViewById(R.id.background_image);

        signUp = (TextView) findViewById(R.id.signUpTextView);
        Blogin = (Button) findViewById(R.id.btn_login);

        Blogin.setOnClickListener(this);
        signUp.setOnClickListener(this);

        loadImage(imageView, R.drawable.bg, 4);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:


                if(authenticate()){
                    //do something here..
                    startActivity(new Intent(this, Welcome.class ));
                    System.out.println("==============login successful===============");
                }
                else{
                    showMessage("Please enter valid data.");
                }
                break;
            case R.id.signUpTextView:
                startActivity(new Intent(this, Register.class ));
                break;
        }
    }

    private void loadImage(ImageView imgView, int resourceId, int sampleSize){
        System.out.println("my resource : " + getResources().getResourceName(resourceId));
        ImageLoader imgLoader =  new ImageLoader(imgView, getResources());
        imgLoader.execute(resourceId, sampleSize);
    }




    private boolean authenticate() {
       emailString = Eemail.getText().toString().toLowerCase();
        passwordString = Epassword.getText().toString();

        //****Backdoor****
        if (emailString.equals("@test1234")) return true;

        return LoginUtil.authenticate(emailString, passwordString);
    }

    private void showMessage(String msg){
        AlertDialog.Builder diBuilder = new AlertDialog.Builder(LoginActivity.this);
        diBuilder.setMessage(msg);
        diBuilder.setPositiveButton("Try again", null);
        diBuilder.show();
    }


    public String getEmailString() {
        return emailString;
    }

    public String getPasswordString() {
        return passwordString;
    }
}
