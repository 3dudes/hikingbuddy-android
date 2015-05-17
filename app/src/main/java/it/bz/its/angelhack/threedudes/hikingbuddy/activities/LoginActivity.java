package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.UserResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.MissionSessionService;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.UserService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView tvUserName = (TextView) this.findViewById(R.id.tv_login_user_name);
        final TextView tvPassword = (TextView) this.findViewById(R.id.tv_login_password);

        Button btLogin = (Button) this.findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestAdapter restAdapter = Utils.getRestAdapter();
                UserService us = restAdapter.create(UserService.class);

                final ProgressDialog progDialog = Utils.newLoadingDialog(LoginActivity.this, "Login in ...");
                progDialog.show();
                us.login(tvUserName.getText().toString(), tvPassword.getText().toString(), new Callback<UserResponse>() {
                    @Override
                    public void success(UserResponse us, Response response2) {
                        Intent profileStarter = new Intent(LoginActivity.this, ProfileActivity.class);

                        startActivity(profileStarter);
                        progDialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Utils.showOkAlertDialog(LoginActivity.this, "Problem", error.getMessage(), null);
                        progDialog.dismiss();
                    }
                });
            }
        });
    }
}



