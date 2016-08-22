package com.taxiapp.thetaxicompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.taxiapp.thetaxicompany.json.JSONUtil;
import com.taxiapp.thetaxicompany.push.PushRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText firstName, lastName, email, password;
  private Button submit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    firstName = (EditText) findViewById(R.id.first_name_txt);
    lastName = (EditText) findViewById(R.id.last_name_txt);
    email = (EditText) findViewById(R.id.email_txt);
    password = (EditText) findViewById(R.id.password_txt);

    submit = (Button) findViewById(R.id.submit_btn);
    submit.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    register();
    startActivity(new Intent(this, LoginActivity.class));

  }

  private void register(){
    String fName = firstName.getText().toString().trim();
    String lName = lastName.getText().toString().trim();
    String mail = email.getText().toString().toLowerCase().trim();
    String pw = password.getText().toString().trim();

    String notificationKey = PushRequest.getRegistrationId(this);

      String url = "http://cabtogo.eu-gb.mybluemix.net/api/accounts";
    String idUrl = "http://cabtogo.eu-gb.mybluemix.net/api/accounts?filter[order]=id%20DESC&filter[limit]=1";
    new JSONUtil().execute(url, JSONUtil.Action.Register.name(), idUrl, "first_name", fName, "last_name", lName,
      "email", mail, "password", pw, "notification_key", notificationKey);
  }
}
