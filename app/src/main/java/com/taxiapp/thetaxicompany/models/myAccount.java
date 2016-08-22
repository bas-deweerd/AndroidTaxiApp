package com.taxiapp.thetaxicompany.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taxiapp.thetaxicompany.ImageLoader;
import com.taxiapp.thetaxicompany.LoginActivity;
import com.taxiapp.thetaxicompany.R;
import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.json.ModelUrl;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class myAccount extends AppCompatActivity  {

    private static JSONObject jsonObject;
    private ImageView imageView;
    private TextView Eemail, EFirstname, ELastname;
    String EmailString, FirstnameString, LastnameString;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);







        //------------------------------------------//
        LoginActivity la = new LoginActivity();




        try {
            jsonObject =  new JSONUtil().execute(ModelUrl.ACCOUNT.getUrl(), JSONUtil.Action.Filter.name(), "Email", "m@demo.com" ).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        try {
            EmailString = jsonObject.getString("email");
            FirstnameString = jsonObject.getString("first_name");
            LastnameString = jsonObject.getString("last_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        // Background Image
        imageView = (ImageView) findViewById(R.id.background_image);
        loadImage(imageView, R.drawable.bg, 4);

        // Set Informations of a Account
        Eemail = (TextView) findViewById(R.id.EmailInput);
        EFirstname = (TextView) findViewById(R.id.FirstnameInput);
        ELastname = (TextView) findViewById(R.id.LastnameInput);

        Eemail.setText(EmailString);
        EFirstname.setText(FirstnameString);
        ELastname.setText(LastnameString);


    }

    private void loadImage(ImageView imgView, int resourceId, int sampleSize){
        System.out.println("my resource : " + getResources().getResourceName(resourceId));
        ImageLoader imgLoader =  new ImageLoader(imgView, getResources());
        imgLoader.execute(resourceId, sampleSize);
    }



}
