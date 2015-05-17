package it.bz.its.angelhack.threedudes.hikingbuddy.moks;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.games.request.Requests;

import java.io.IOException;
import java.util.Collections;

import it.bz.its.angelhack.threedudes.hikingbuddy.enums.HttpCodes;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class RetrofitMissionClient implements Client {
    private static final String TAG = "RetrofitMissionClient";

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());
        String responseString = "";
        HttpCodes responseCode = HttpCodes.OK;

        Log.d(TAG, "Fetching URI: " + uri.toString());

        if(uri.getPath().startsWith("/mission/")) {
            responseString = "";
        }

        return new Response(request.getUrl(), responseCode.getNumericValue(), "nothing", Collections.EMPTY_LIST,
                            new TypedByteArray("application/json", responseString.getBytes()));

    }
}
