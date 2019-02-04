package com.example.kshitij.patentlite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StatusFragment extends Fragment { ;

    private View rootview;
    private TextView text;
    private String id;
    private String timestamp;
    private String status;
    private String action;
    private int role;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        role = getArguments().getInt("role");
        rootview = inflater.inflate(R.layout.fragment_status, container, false);
        text = rootview.findViewById(R.id.status);
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootview;
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = request();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                try {

                    JSONObject obj = new JSONObject(myResponse);
                    id = obj.getString("id");
                    timestamp = obj.getString("timestamp");
                    timestamp = timestamp.substring(0,10);
                    JSONArray contractProperties = obj.getJSONArray("contractProperties");
                    JSONObject temp = contractProperties.getJSONObject(0);
                    status = temp.getString("value");
                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + myResponse + "\"");
                }

                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootview.findViewById(R.id.constraint).setVisibility(View.VISIBLE);
                        rootview.findViewById(R.id.infoLayout).setVisibility(View.VISIBLE);
                        rootview.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        TextView id_view = rootview.findViewById(R.id.patentID);
                        id_view.setText(id);
                        TextView status_view = rootview.findViewById(R.id.status);
                        status_view.setText(status);
                        ImageView image = rootview.findViewById(R.id.imageView);
                        if(status !=null){
                            if(Integer.parseInt(status)<2){
                                image.setImageDrawable(getResources().getDrawable(R.drawable.circle_red));
                            } else if(Integer.parseInt(status)==5){
                                image.setImageDrawable(getResources().getDrawable(R.drawable.circle_first));
                            } else {
                                image.setImageDrawable(getResources().getDrawable(R.drawable.circle_yellow));
                            }
                        }
                        TextView timestamp_view = rootview.findViewById(R.id.timestamp);
                        timestamp_view.setText(timestamp);
                        try {
                            runActions();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public Request request(){
        Request request;
        if(role==1){
            //Applicant
            request = new Request.Builder()
                    .url("https://litehai-vtt6wd-api.azurewebsites.net/api/v1/contracts/12")
                    .get()
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCIsImtpZCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCJ9.eyJhdWQiOiI2MzRjOGFjYS1mZmE1LTQyMmMtODM5My0zZjQzYTA4ZDY3ZDkiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80OWRiMDE4OC01ZjhhLTQzZTQtOTcwOC04ZjgyNDA4ZGQwOTgvIiwiaWF0IjoxNTQ5MTkwODE2LCJuYmYiOjE1NDkxOTA4MTYsImV4cCI6MTU0OTE5NDcxNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhLQUFBQWJ5L0RERGxnM0xtU0lKREwrc3g3UGxtbG5UY1gzYmlSc0xEWEdpeVVuRXM9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjYzNGM4YWNhLWZmYTUtNDIyYy04MzkzLTNmNDNhMDhkNjdkOSIsImFwcGlkYWNyIjoiMCIsImlwYWRkciI6IjQyLjEwOC4yMDUuMjA0IiwibmFtZSI6IkFwcGxpY2FudCIsIm9pZCI6IjgxNjE2YzE4LTdlMGEtNDgxMC05NjEzLTdmNjQ0YjgwYmQzZSIsInNjcCI6IlVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwiLCJzdWIiOiJYRHNfaGJIZHJxR1RYaDJxWUxUYWxocjdxTFlJSXF0M0dTSXRiZ2d5dnY0IiwidGlkIjoiNDlkYjAxODgtNWY4YS00M2U0LTk3MDgtOGY4MjQwOGRkMDk4IiwidW5pcXVlX25hbWUiOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1dGkiOiJUNmpsLTJzSVZFbWRUWnZpZUVFekFBIiwidmVyIjoiMS4wIn0.Cm49E5H8buNjfgeTHFQ_a4m6PFRyjtJcUGDnsIXgL5lNqOcnDDBgKDyL3bwB_SKEQ-Rijf5pSSJFDtQVNWabgCzGlB5n805gtT89Yv2oafsS1KJxwzwKuCS4BKTxfYurIWqsI5TSJnTssDsxv_kLCnKy-_S0umCvQH9NSN8UwFrhGZUbKuk_FWel4gjlVkNPPSeT0LR8hUmZXq0dVmDo-crreOicALhuN7f2z2DYeuzKWccqrWFnJNwPEjSZINZQXtb_vn2Y-bnzioqFcmDFE4vsd13uc5RikTStQusKkn6TxwN5O5cWXwT0gYfdj8CwnyeYwj6syCBKsm0_YQRmUg")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "78085da1-929f-4a07-8e4b-fa6f300e97fc")
                    .build();

        } else if (role==2){
            //Appraiser
            request = new Request.Builder()
                    .url("https://litehai-vtt6wd-api.azurewebsites.net/api/v1/contracts/12")
                    .get()
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCIsImtpZCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCJ9.eyJhdWQiOiI2MzRjOGFjYS1mZmE1LTQyMmMtODM5My0zZjQzYTA4ZDY3ZDkiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80OWRiMDE4OC01ZjhhLTQzZTQtOTcwOC04ZjgyNDA4ZGQwOTgvIiwiaWF0IjoxNTQ5MTkwODE2LCJuYmYiOjE1NDkxOTA4MTYsImV4cCI6MTU0OTE5NDcxNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhLQUFBQWJ5L0RERGxnM0xtU0lKREwrc3g3UGxtbG5UY1gzYmlSc0xEWEdpeVVuRXM9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjYzNGM4YWNhLWZmYTUtNDIyYy04MzkzLTNmNDNhMDhkNjdkOSIsImFwcGlkYWNyIjoiMCIsImlwYWRkciI6IjQyLjEwOC4yMDUuMjA0IiwibmFtZSI6IkFwcGxpY2FudCIsIm9pZCI6IjgxNjE2YzE4LTdlMGEtNDgxMC05NjEzLTdmNjQ0YjgwYmQzZSIsInNjcCI6IlVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwiLCJzdWIiOiJYRHNfaGJIZHJxR1RYaDJxWUxUYWxocjdxTFlJSXF0M0dTSXRiZ2d5dnY0IiwidGlkIjoiNDlkYjAxODgtNWY4YS00M2U0LTk3MDgtOGY4MjQwOGRkMDk4IiwidW5pcXVlX25hbWUiOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1dGkiOiJUNmpsLTJzSVZFbWRUWnZpZUVFekFBIiwidmVyIjoiMS4wIn0.Cm49E5H8buNjfgeTHFQ_a4m6PFRyjtJcUGDnsIXgL5lNqOcnDDBgKDyL3bwB_SKEQ-Rijf5pSSJFDtQVNWabgCzGlB5n805gtT89Yv2oafsS1KJxwzwKuCS4BKTxfYurIWqsI5TSJnTssDsxv_kLCnKy-_S0umCvQH9NSN8UwFrhGZUbKuk_FWel4gjlVkNPPSeT0LR8hUmZXq0dVmDo-crreOicALhuN7f2z2DYeuzKWccqrWFnJNwPEjSZINZQXtb_vn2Y-bnzioqFcmDFE4vsd13uc5RikTStQusKkn6TxwN5O5cWXwT0gYfdj8CwnyeYwj6syCBKsm0_YQRmUg")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "78085da1-929f-4a07-8e4b-fa6f300e97fc")
                    .build();

        } else if(role==3){
            //Inspector
            request = new Request.Builder()
                    .url("https://litehai-vtt6wd-api.azurewebsites.net/api/v1/contracts/12")
                    .get()
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCIsImtpZCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCJ9.eyJhdWQiOiI2MzRjOGFjYS1mZmE1LTQyMmMtODM5My0zZjQzYTA4ZDY3ZDkiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80OWRiMDE4OC01ZjhhLTQzZTQtOTcwOC04ZjgyNDA4ZGQwOTgvIiwiaWF0IjoxNTQ5MTkwODE2LCJuYmYiOjE1NDkxOTA4MTYsImV4cCI6MTU0OTE5NDcxNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhLQUFBQWJ5L0RERGxnM0xtU0lKREwrc3g3UGxtbG5UY1gzYmlSc0xEWEdpeVVuRXM9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjYzNGM4YWNhLWZmYTUtNDIyYy04MzkzLTNmNDNhMDhkNjdkOSIsImFwcGlkYWNyIjoiMCIsImlwYWRkciI6IjQyLjEwOC4yMDUuMjA0IiwibmFtZSI6IkFwcGxpY2FudCIsIm9pZCI6IjgxNjE2YzE4LTdlMGEtNDgxMC05NjEzLTdmNjQ0YjgwYmQzZSIsInNjcCI6IlVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwiLCJzdWIiOiJYRHNfaGJIZHJxR1RYaDJxWUxUYWxocjdxTFlJSXF0M0dTSXRiZ2d5dnY0IiwidGlkIjoiNDlkYjAxODgtNWY4YS00M2U0LTk3MDgtOGY4MjQwOGRkMDk4IiwidW5pcXVlX25hbWUiOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1dGkiOiJUNmpsLTJzSVZFbWRUWnZpZUVFekFBIiwidmVyIjoiMS4wIn0.Cm49E5H8buNjfgeTHFQ_a4m6PFRyjtJcUGDnsIXgL5lNqOcnDDBgKDyL3bwB_SKEQ-Rijf5pSSJFDtQVNWabgCzGlB5n805gtT89Yv2oafsS1KJxwzwKuCS4BKTxfYurIWqsI5TSJnTssDsxv_kLCnKy-_S0umCvQH9NSN8UwFrhGZUbKuk_FWel4gjlVkNPPSeT0LR8hUmZXq0dVmDo-crreOicALhuN7f2z2DYeuzKWccqrWFnJNwPEjSZINZQXtb_vn2Y-bnzioqFcmDFE4vsd13uc5RikTStQusKkn6TxwN5O5cWXwT0gYfdj8CwnyeYwj6syCBKsm0_YQRmUg")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "78085da1-929f-4a07-8e4b-fa6f300e97fc")
                    .build();

        } else {
            //Admin
            request = new Request.Builder()
                    .url("https://litehai-vtt6wd-api.azurewebsites.net/api/v1/contracts/12")
                    .get()
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCIsImtpZCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCJ9.eyJhdWQiOiI2MzRjOGFjYS1mZmE1LTQyMmMtODM5My0zZjQzYTA4ZDY3ZDkiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80OWRiMDE4OC01ZjhhLTQzZTQtOTcwOC04ZjgyNDA4ZGQwOTgvIiwiaWF0IjoxNTQ5MTkwODE2LCJuYmYiOjE1NDkxOTA4MTYsImV4cCI6MTU0OTE5NDcxNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhLQUFBQWJ5L0RERGxnM0xtU0lKREwrc3g3UGxtbG5UY1gzYmlSc0xEWEdpeVVuRXM9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjYzNGM4YWNhLWZmYTUtNDIyYy04MzkzLTNmNDNhMDhkNjdkOSIsImFwcGlkYWNyIjoiMCIsImlwYWRkciI6IjQyLjEwOC4yMDUuMjA0IiwibmFtZSI6IkFwcGxpY2FudCIsIm9pZCI6IjgxNjE2YzE4LTdlMGEtNDgxMC05NjEzLTdmNjQ0YjgwYmQzZSIsInNjcCI6IlVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwiLCJzdWIiOiJYRHNfaGJIZHJxR1RYaDJxWUxUYWxocjdxTFlJSXF0M0dTSXRiZ2d5dnY0IiwidGlkIjoiNDlkYjAxODgtNWY4YS00M2U0LTk3MDgtOGY4MjQwOGRkMDk4IiwidW5pcXVlX25hbWUiOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1dGkiOiJUNmpsLTJzSVZFbWRUWnZpZUVFekFBIiwidmVyIjoiMS4wIn0.Cm49E5H8buNjfgeTHFQ_a4m6PFRyjtJcUGDnsIXgL5lNqOcnDDBgKDyL3bwB_SKEQ-Rijf5pSSJFDtQVNWabgCzGlB5n805gtT89Yv2oafsS1KJxwzwKuCS4BKTxfYurIWqsI5TSJnTssDsxv_kLCnKy-_S0umCvQH9NSN8UwFrhGZUbKuk_FWel4gjlVkNPPSeT0LR8hUmZXq0dVmDo-crreOicALhuN7f2z2DYeuzKWccqrWFnJNwPEjSZINZQXtb_vn2Y-bnzioqFcmDFE4vsd13uc5RikTStQusKkn6TxwN5O5cWXwT0gYfdj8CwnyeYwj6syCBKsm0_YQRmUg")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "78085da1-929f-4a07-8e4b-fa6f300e97fc")
                    .build();

        }
        return request;
    }

    void runActions() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://litehai-vtt6wd-api.azurewebsites.net/api/v1/contracts/12/actions")
                .get()
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCIsImtpZCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCJ9.eyJhdWQiOiI2MzRjOGFjYS1mZmE1LTQyMmMtODM5My0zZjQzYTA4ZDY3ZDkiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80OWRiMDE4OC01ZjhhLTQzZTQtOTcwOC04ZjgyNDA4ZGQwOTgvIiwiaWF0IjoxNTQ5MTkwODE2LCJuYmYiOjE1NDkxOTA4MTYsImV4cCI6MTU0OTE5NDcxNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhLQUFBQWJ5L0RERGxnM0xtU0lKREwrc3g3UGxtbG5UY1gzYmlSc0xEWEdpeVVuRXM9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjYzNGM4YWNhLWZmYTUtNDIyYy04MzkzLTNmNDNhMDhkNjdkOSIsImFwcGlkYWNyIjoiMCIsImlwYWRkciI6IjQyLjEwOC4yMDUuMjA0IiwibmFtZSI6IkFwcGxpY2FudCIsIm9pZCI6IjgxNjE2YzE4LTdlMGEtNDgxMC05NjEzLTdmNjQ0YjgwYmQzZSIsInNjcCI6IlVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwiLCJzdWIiOiJYRHNfaGJIZHJxR1RYaDJxWUxUYWxocjdxTFlJSXF0M0dTSXRiZ2d5dnY0IiwidGlkIjoiNDlkYjAxODgtNWY4YS00M2U0LTk3MDgtOGY4MjQwOGRkMDk4IiwidW5pcXVlX25hbWUiOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1dGkiOiJUNmpsLTJzSVZFbWRUWnZpZUVFekFBIiwidmVyIjoiMS4wIn0.Cm49E5H8buNjfgeTHFQ_a4m6PFRyjtJcUGDnsIXgL5lNqOcnDDBgKDyL3bwB_SKEQ-Rijf5pSSJFDtQVNWabgCzGlB5n805gtT89Yv2oafsS1KJxwzwKuCS4BKTxfYurIWqsI5TSJnTssDsxv_kLCnKy-_S0umCvQH9NSN8UwFrhGZUbKuk_FWel4gjlVkNPPSeT0LR8hUmZXq0dVmDo-crreOicALhuN7f2z2DYeuzKWccqrWFnJNwPEjSZINZQXtb_vn2Y-bnzioqFcmDFE4vsd13uc5RikTStQusKkn6TxwN5O5cWXwT0gYfdj8CwnyeYwj6syCBKsm0_YQRmUg")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "141f468f-0c0b-4d8b-8729-bb0c1b69771f")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                try {

                    JSONObject obj = new JSONObject(myResponse);
                    JSONArray workFlow = obj.getJSONArray("workflowFunctions");
                    JSONObject object = workFlow.getJSONObject(0);
                    action = object.getString("name");

                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + myResponse + "\"");
                }

                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rootview.findViewById(R.id.constraint).setVisibility(View.VISIBLE);
                        rootview.findViewById(R.id.infoLayout).setVisibility(View.VISIBLE);
                        rootview.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        TextView id_view = rootview.findViewById(R.id.actions);
                        id_view.setText(action);
                    }
                });

            }
        });
    }

}
