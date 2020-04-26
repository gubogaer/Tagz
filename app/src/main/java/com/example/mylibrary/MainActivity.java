package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.tagz.TagViewGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String opzoekString = "";

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

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }
        }
        );

        findViewById(R.id.addTagButton).setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        addTag();
                    }
                }
        );


        TagViewGroup tvg = findViewById(R.id.tagViewGroup);






    }

    public void addTag(){
        TagViewGroup tvg = findViewById(R.id.tagViewGroup);
        tvg.addTag(opzoekString);
    }

    public void search(String text){
        TagViewGroup tvg = findViewById(R.id.tagViewGroup);
        opzoekString = text.trim();
        tvg.filterTags(".*" + opzoekString + ".*");
    }
}
