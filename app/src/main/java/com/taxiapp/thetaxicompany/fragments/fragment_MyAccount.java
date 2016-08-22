package com.taxiapp.thetaxicompany.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import com.taxiapp.thetaxicompany.ImageLoader;
import com.taxiapp.thetaxicompany.LoginActivity;
import com.taxiapp.thetaxicompany.R;
import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.json.ModelUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by Ajeet Sandu on 12.05.2016.
 */
public class fragment_MyAccount extends Fragment {


    private static JSONObject jsonObject;
    private ImageView imageView;
    private TextView Eemail, EFirstname, ELastname;
    String EmailString, FirstnameString, LastnameString;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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


        // Set Informations of a Account
        Eemail = (TextView) getView().findViewById(R.id.EmailInput);
        EFirstname = (TextView) getView().findViewById(R.id.FirstnameInput);
        ELastname = (TextView) getView().findViewById(R.id.LastnameInput);

        Eemail.setText(EmailString);
        EFirstname.setText(FirstnameString);
        ELastname.setText(LastnameString);





    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myaccount, container, false);




        return rootView;
    }

}
