package com.example.kshitij.patentlite;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewFormFragment extends Fragment {

    private View rootview;
    private String response;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private TextView fail_text;
    private Button submit;
    private int role = 1;

    public NewFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        role = getArguments().getInt("role");
        rootview = inflater.inflate(R.layout.fragment_new_form, container, false);
        progressBar = rootview.findViewById(R.id.progressBar);
        fail_text = rootview.findViewById(R.id.failed);
        linearLayout = rootview.findViewById(R.id.newContract);
        submit = rootview.findViewById(R.id.submitContract);
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootview;
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://litehai-vtt6wd-api.azurewebsites.net/api/v1/capabilities/canCreateContract/1")
                .get()
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCIsImtpZCI6Ii1zeE1KTUxDSURXTVRQdlp5SjZ0eC1DRHh3MCJ9.eyJhdWQiOiI2MzRjOGFjYS1mZmE1LTQyMmMtODM5My0zZjQzYTA4ZDY3ZDkiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC80OWRiMDE4OC01ZjhhLTQzZTQtOTcwOC04ZjgyNDA4ZGQwOTgvIiwiaWF0IjoxNTQ5MTkwODE2LCJuYmYiOjE1NDkxOTA4MTYsImV4cCI6MTU0OTE5NDcxNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhLQUFBQWJ5L0RERGxnM0xtU0lKREwrc3g3UGxtbG5UY1gzYmlSc0xEWEdpeVVuRXM9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6IjYzNGM4YWNhLWZmYTUtNDIyYy04MzkzLTNmNDNhMDhkNjdkOSIsImFwcGlkYWNyIjoiMCIsImlwYWRkciI6IjQyLjEwOC4yMDUuMjA0IiwibmFtZSI6IkFwcGxpY2FudCIsIm9pZCI6IjgxNjE2YzE4LTdlMGEtNDgxMC05NjEzLTdmNjQ0YjgwYmQzZSIsInNjcCI6IlVzZXIuUmVhZCBVc2VyLlJlYWRCYXNpYy5BbGwiLCJzdWIiOiJYRHNfaGJIZHJxR1RYaDJxWUxUYWxocjdxTFlJSXF0M0dTSXRiZ2d5dnY0IiwidGlkIjoiNDlkYjAxODgtNWY4YS00M2U0LTk3MDgtOGY4MjQwOGRkMDk4IiwidW5pcXVlX25hbWUiOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1cG4iOiJhcHBsaWNhbnRAaW5zaXlhaGhham9vcmlnbWFpbC5vbm1pY3Jvc29mdC5jb20iLCJ1dGkiOiJUNmpsLTJzSVZFbWRUWnZpZUVFekFBIiwidmVyIjoiMS4wIn0.Cm49E5H8buNjfgeTHFQ_a4m6PFRyjtJcUGDnsIXgL5lNqOcnDDBgKDyL3bwB_SKEQ-Rijf5pSSJFDtQVNWabgCzGlB5n805gtT89Yv2oafsS1KJxwzwKuCS4BKTxfYurIWqsI5TSJnTssDsxv_kLCnKy-_S0umCvQH9NSN8UwFrhGZUbKuk_FWel4gjlVkNPPSeT0LR8hUmZXq0dVmDo-crreOicALhuN7f2z2DYeuzKWccqrWFnJNwPEjSZINZQXtb_vn2Y-bnzioqFcmDFE4vsd13uc5RikTStQusKkn6TxwN5O5cWXwT0gYfdj8CwnyeYwj6syCBKsm0_YQRmUg")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "2a41a752-88d7-4d76-9aaa-8dfa1e0280e1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(myResponse.equals("false")){
                            linearLayout.setVisibility(View.GONE);
                            fail_text.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.GONE);
                        } else {
                            fail_text.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

}
