package com.taxiapp.thetaxicompany.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taxiapp.thetaxicompany.LoginActivity;
import com.taxiapp.thetaxicompany.Mail.SendMail;
import com.taxiapp.thetaxicompany.R;
import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.json.ModelUrl;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;


/**
 * Created by Ajeet Sandu on 18.05.2016.
 */
public class fragment_contactus extends Fragment {

    TextView Subject,Text;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Subject = (TextView) getView().findViewById(R.id.subject);
        Text = (TextView) getView().findViewById(R.id.Textbox);

        sendEmail(Subject.toString().trim(), Text.toString().trim());



    }


    private void sendEmail(String Esubject, String text) {
        //Getting content for email
        String email = "a.sandu@student.fontys.nl";
        String subject = "In App contact:" + Esubject;
        String message = "Message:" + text;



        //Creating SendMail object
        SendMail sm = new SendMail(getContext(), email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contactus, container, false);
         return rootView;
    }

}
