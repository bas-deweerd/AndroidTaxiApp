package com.taxiapp.thetaxicompany;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.taxiapp.thetaxicompany.Mail.SendMail;
import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.json.ModelUrl;

/**
 * Created by Ajeet Sandu
 */

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText Efirstname, Elastname, Eemail, Epassword, Econfirmpassword;
    private Button Bregister;
    private ImageView imageView;

    private boolean unequalPassword = false;

    private static int ID = 1;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        Efirstname = (EditText) findViewById(R.id.register_firstname);
        Elastname = (EditText) findViewById(R.id.register_lastname);
        Eemail = (EditText) findViewById(R.id.register_email);
        Epassword = (EditText) findViewById(R.id.register_password);
        Econfirmpassword = (EditText) findViewById(R.id.register_password_confirm);

        Bregister = (Button) findViewById(R.id.btn_register);
        Bregister.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.register_img);
        loadImage(imageView, R.drawable.bg, 4);

        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void sendEmail() {
        //Getting content for email
        String email = Eemail.getText().toString().trim();
        String subject = "Welcome " + Efirstname.getText().toString().trim() + " " +
                        Elastname.getText().toString().trim() + " to CabToGo.";
        String message = "Welcome " + Efirstname.getText().toString().trim() + " " +
                        Elastname.getText().toString().trim() + " to CabToGo. Please confirm your Email to use our service. \n" +
                        "Thanks for using CabToGo. Have fun :)";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:

                    if (register()) sendEmail();

                break;
        }
    }

    private void showMessage(String msg, String buttonText, DialogInterface.OnClickListener listener ){
        AlertDialog.Builder diBuilder = new AlertDialog.Builder(Register.this);
        diBuilder.setMessage(msg);
        diBuilder.setPositiveButton(buttonText, listener);
        diBuilder.show();
    }

    private boolean canProceed(){
        if (Efirstname.getText().toString().isEmpty()) return false;
        if (Elastname.getText().toString().isEmpty()) return false;
        if (Eemail.getText().toString().isEmpty()) return false;
        if (!Eemail.getText().toString().contains("@")) return false;
        if (Epassword.getText().toString().length() < 8) return false;
        if (Econfirmpassword.getText().toString().isEmpty())return false;

        if (!Epassword.getText().toString().equals(Econfirmpassword.getText().toString())) {
            unequalPassword = true;
            Epassword.setText("");
            Econfirmpassword.setText("");
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.taxiapp.thetaxicompany/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.taxiapp.thetaxicompany/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }

    /**
     * Loads an image using a different thread.
     * @param imgView the ImageView from the xml
     * @param resourceId the id of the drawable
     * @param sampleSize size for resulotion, the higher the size, the lower the resolution.
     */
    private void loadImage(ImageView imgView, int resourceId, int sampleSize){
        new ImageLoader(imgView, getResources()).execute(resourceId, sampleSize);
    }

    private boolean register(){
        if(!canProceed() && !unequalPassword){
            String error = "Please enter valid data! Use at least 8 characters for your password and a valid email address.";
            showMessage(error, "Try again.", null);
        } else if(!canProceed() && unequalPassword){
            String passwordError = "These passwords don't match.";
            showMessage(passwordError, "Try again.", null);
        }else if(canProceed()){

            String firstname = Efirstname.getText().toString().toLowerCase();
            String lastname = Elastname.getText().toString().toLowerCase();
            String email = Eemail.getText().toString().trim().toLowerCase();
            String password = Epassword.getText().toString();

            new JSONUtil().execute(ModelUrl.ACCOUNT.getUrl(), JSONUtil.Action.Register.name(), firstname, lastname, email, password);
            String positive = "Your account has been created successfully! Please confirm your email. Please Login.";
            showMessage(positive, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Register.this, LoginActivity.class) );
                }
            });//changing activity

            return true;
        }

        return false;
    }
}
