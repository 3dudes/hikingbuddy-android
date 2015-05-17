package it.bz.its.angelhack.threedudes.hikingbuddy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class Utils {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void showOkAlertDialog(Context ctx, String title, String body, final Runnable onOkClick) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle(title);
        builder1.setMessage(body);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        if (onOkClick != null) {
                            onOkClick.run();
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void showYesNoAlertDialog(Context ctx, String title, String body, final Runnable onYesClick, final Runnable onNoClick) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle(title);
        builder1.setMessage(body);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        if (onYesClick != null) {
                            onYesClick.run();
                        }
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        if (onNoClick != null) {
                            onNoClick.run();
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static ProgressDialog newLoadingDialog(Context ctx, String text) {
        ProgressDialog progress = new ProgressDialog(ctx);

        progress.setMessage(text);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        return progress;
    }

    public static RestAdapter getRestAdapter() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();

        return new RestAdapter.Builder()
                .setEndpoint(Constants.REST_ADDRESS)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }
}
