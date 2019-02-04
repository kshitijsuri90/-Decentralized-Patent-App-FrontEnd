package com.example.kshitij.patentlite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ApplicationFragment extends Fragment {

    private View rootview;
    private int role;
    private RelativeLayout layout;
    private ProgressBar progressBar;

    public ApplicationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        role = getArguments().getInt("role");
        rootview = inflater.inflate(R.layout.fragment_application, container, false);
        layout = rootview.findViewById(R.id.patentDetails);
        progressBar = rootview.findViewById(R.id.progressBar);
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

                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressBar.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            JSONObject object = new JSONObject(myResponse);
                            JSONArray contarctProperties = object.getJSONArray("contractProperties");
                            for(int i =0;i<2;i++){
                                JSONObject temp = contarctProperties.getJSONObject(i);
                                String value = temp.getString("value");
                                if(i==0){
                                    TextView status = rootview.findViewById(R.id.status);
                                    status.setText(value);
                                    if(Integer.parseInt(value)<2){
                                        ImageView image = rootview.findViewById(R.id.imageView);
                                        image.setImageDrawable(getResources().getDrawable(R.drawable.circle_red));
                                    } else if(Integer.parseInt(value)==5){
                                        ImageView image = rootview.findViewById(R.id.imageView);
                                        image.setImageDrawable(getResources().getDrawable(R.drawable.circle_first));
                                    } else {
                                        ImageView image = rootview.findViewById(R.id.imageView);
                                        image.setImageDrawable(getResources().getDrawable(R.drawable.circle_yellow));
                                    }
                                } else {
                                    TextView name = rootview.findViewById(R.id.abstractDetails);
                                    String abstarct = value.substring(0,8);
                                    abstarct = abstarct + '\n' + value.substring(9) + '\n';
                                    name.setText(abstarct);
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Error fetching Contracts",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


    public Request request(){
        return new Request.Builder()
                .url("https://litehai-vtt6wd-api.azurewebsites.net/api/v1/contracts/12")
                .get()
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCIsImtpZCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCJ9.eyJhdWQiOiI2MzRjOGFjYS1mZmE1LTQyMmMtODM5My0zZjQzYTA4ZDY3ZDkiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80OWRiMDE4OC01ZjhhLTQzZTQtOTcwOC04ZjgyNDA4ZGQwOTgvIiwiaWF0IjoxNTQ5MTkwODE2LCJuYmYiOjE1NDkxOTA4MTYsImV4cCI6MTU0OTE5NDcxNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhLQUFBQWJ5L0RERGxnM0xtU0lKREwrc3g3UGxtbG5UY1gzYmlSc0xEWEdpeVVuRXM9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjYzNGM4YWNhLWZmYTUtNDIyYy04MzkzLTNmNDNhMDhkNjdkOSIsImFwcGlkYWNyIjoiMCIsImlwYWRkciI6IjQyLjEwOC4yMDUuMjA0IiwibmFtZSI6IkFwcGxpY2FudCIsIm9pZCI6IjgxNjE2YzE4LTdlMGEtNDgxMC05NjEzLTdmNjQ0YjgwYmQzZSIsInNjcCI6IlVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwiLCJzdWIiOiJYRHNfaGJIZHJxR1RYaDJxWUxUYWxocjdxTFlJSXF0M0dTSXRiZ2d5dnY0IiwidGlkIjoiNDlkYjAxODgtNWY4YS00M2U0LTk3MDgtOGY4MjQwOGRkMDk4IiwidW5pcXVlX25hbWUiOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1dGkiOiJUNmpsLTJzSVZFbWRUWnZpZUVFekFBIiwidmVyIjoiMS4wIn0.Cm49E5H8buNjfgeTHFQ_a4m6PFRyjtJcUGDnsIXgL5lNqOcnDDBgKDyL3bwB_SKEQ-Rijf5pSSJFDtQVNWabgCzGlB5n805gtT89Yv2oafsS1KJxwzwKuCS4BKTxfYurIWqsI5TSJnTssDsxv_kLCnKy-_S0umCvQH9NSN8UwFrhGZUbKuk_FWel4gjlVkNPPSeT0LR8hUmZXq0dVmDo-crreOicALhuN7f2z2DYeuzKWccqrWFnJNwPEjSZINZQXtb_vn2Y-bnzioqFcmDFE4vsd13uc5RikTStQusKkn6TxwN5O5cWXwT0gYfdj8CwnyeYwj6syCBKsm0_YQRmUg")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "78085da1-929f-4a07-8e4b-fa6f300e97fc")
                .build();
    }

}
