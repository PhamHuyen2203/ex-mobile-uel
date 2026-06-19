package com.example.k23411tapp;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FontAndMusicActivity extends AppCompatActivity {
    Button btnPlayAudio1;
    Button btnPlayAudio2;
    TextView txtUEL;
    ListView lvFont;
    ArrayList<String>fonts;
    ArrayAdapter<String>adapterFont;
    String LOG_TAG="FontAndMusicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_font_and_music);
        addViews();
        addEvents();
        loadFonts();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadFonts() {
        try {
            AssetManager assetManager = getAssets();
            String[] arrFonts = assetManager.list("fonts");
            fonts.clear();
            if (arrFonts != null) {
                for (String font : arrFonts) {
                    fonts.add(font);
                }
            } adapterFont.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(LOG_TAG,e.toString());
        }

    }

    private void addEvents() {
        btnPlayAudio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio("musics/audio1.mp3");
            }
        });
        btnPlayAudio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio("musics/audio2.mp3");
            }
        });
        lvFont.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                changeFont(i);
            }
        });
    }

    private void changeFont(int i) {
        String fontName = fonts.get(i);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/" + fontName);
        txtUEL.setTypeface(typeface);
    }

    public void playAudio(String audioFile) {
        try {
            AssetFileDescriptor assetFileDescriptor =
                    getAssets().openFd(audioFile);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            assetFileDescriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(LOG_TAG,e.toString());
        }
    }

    private void addViews() {
        btnPlayAudio1 = findViewById(R.id.btnPlayAudio1);
        btnPlayAudio2 = findViewById(R.id.btnPlayAudio2);
        txtUEL = findViewById(R.id.txtUEL);
        lvFont = findViewById(R.id.lvFont);
        fonts = new ArrayList<>();
        adapterFont = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fonts);
        lvFont.setAdapter(adapterFont);
    }
}