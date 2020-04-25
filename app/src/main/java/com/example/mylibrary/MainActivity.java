package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tagz.TagViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TagViewGroup tvg = findViewById(R.id.tagViewGroup);
        tvg.addTag("tTOHFHGGklgjfl");
        tvg.addTag("hallo");
        tvg.addTag("user");
        tvg.addTag("pannnenkoek");
        tvg.addTag("noob");
        tvg.addTag("werkt het??");
    }
}
