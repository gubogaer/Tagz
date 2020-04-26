package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.tagz.TagViewGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        List<String> tags = new ArrayList<>();
//        tags.add("lala");
//        tags.add("test");
//        tags.add("i ben een lange tag");

        TextInputEditText til = findViewById(R.id.inputTagName);
        til.addTextChangedListener( new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }
        }
        );

        TagViewGroup tvg = findViewById(R.id.tagViewGroup);

        tvg.addTag("lala");
        tvg.addTag("test");
        tvg.addTag("i ben een lange tag");




    }

    public void addTag(String text){
        TagViewGroup tvg = findViewById(R.id.tagViewGroup);
        tvg.addTag(text);
    }

    public void search(String text){
        TagViewGroup tvg = findViewById(R.id.tagViewGroup);
        tvg.filterTags(".*" + text + ".*");
    }
}
