package com.example.mylibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tagz.TagViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> tags = new ArrayList<>();
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test1");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");

        TagViewGroup tvg = findViewById(R.id.tagViewGroup);
        tvg.setTags(tags);


        tvg.filterTags(".*est4.*");
        tvg.filterTags(".*est5.*");

    }
}
