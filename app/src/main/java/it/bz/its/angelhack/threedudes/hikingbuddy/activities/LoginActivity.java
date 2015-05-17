package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;
import it.bz.its.angelhack.threedudes.hikingbuddy.enums.HttpCodes;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Auth;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.AuthResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.RestErrorResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.User;
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

        // Bypass the login if it has already been made
        SharedPreferences prefs = getSharedPreferences("infos", MODE_PRIVATE);
        String prefToken = prefs.getString("token", "");
        if (prefToken.isEmpty() == false) {
            Log.d(TAG, "Token restored from preference: " + prefToken);
            gotoProfileActivity();

            finish();
        }

        // Load used views
        final TextView tvUserName = (TextView) this.findViewById(R.id.tv_login_email);
        final TextView tvPassword = (TextView) this.findViewById(R.id.tv_login_password);

        Button btLogin = (Button) this.findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestAdapter restAdapter = Utils.getRestAdapter(null);
                UserService us = restAdapter.create(UserService.class);

                final ProgressDialog progDialog = Utils.newLoadingDialog(LoginActivity.this, "Login in ...");
                progDialog.show();
                us.login(tvUserName.getText().toString(), tvPassword.getText().toString(), new Callback<AuthResponse>() {
                    @Override
                    public void success(AuthResponse us, Response response2) {
                        // Store the acquired token
                        SharedPreferences.Editor editor = getSharedPreferences("infos", MODE_PRIVATE).edit();
                        Auth authModel = us.getAuthToken();
                        User authUser = authModel.getUser();

                        editor.putString("token", authModel.getToken());
                        editor.putString("name", authUser.getFirstName());
                        editor.putString("avatar", authUser.getPicture().getNormalImageUri());
                        editor.apply();

                        // Go to the user's profile page
                        gotoProfileActivity();
                        progDialog.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        HttpCodes httpCode = HttpCodes.getFromNumericValue(error.getResponse().getStatus());
                        RestErrorResponse restErrorResp = (RestErrorResponse) error.getBodyAs(RestErrorResponse.class);

                        switch (httpCode) {
                            case UNPROCESSABLE:
                                Utils.showOkAlertDialog(LoginActivity.this,
                                        "Bad credentials?",
                                        restErrorResp.getJoinedErrors(),
                                        null);
                                break;
                            default:
                                Utils.showOkAlertDialog(LoginActivity.this, "Problem", error.getMessage(), null);
                                break;
                        }

                        progDialog.dismiss();
                    }
                });
            }
        });
    }

    private void gotoProfileActivity() {
        Intent profileStarter = new Intent(LoginActivity.this, ProfileActivity.class);

        startActivity(profileStarter);
    }
}



