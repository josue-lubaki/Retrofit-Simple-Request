package com.example.meteo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.meteo.interfaces.JsonPlaceHolderAPI;
import com.example.meteo.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtView = findViewById(R.id.text_view_result);

        // lancer retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Par injection
        JsonPlaceHolderAPI myJson = retrofit.create(JsonPlaceHolderAPI.class);

        // Retrieve data
        Call<List<Post>> call = myJson.getPost();
        
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                // Si conncetion Failed
                if(!response.isSuccessful()){
                    txtView.setText("Connection Failed \nFailedCode : " + response.code());
                    return;
                }

                List<Post> maListe = response.body();

                for(Post post : maListe){
                    String content = "";
                    content += "ID : " + post.getId() + "\n";
                    content += "User ID : " + post.getUserId() + "\n";
                    content += "Title : " + post.getTitle() + "\n";
                    content += "Text : " + post.getText() + "\n\n";

                    txtView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Si erreur 404
                txtView.setText(t.getMessage());
            }
        });
    }
}