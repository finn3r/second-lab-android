package com.example.mydbapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(this);
    List<com.example.mydbapp.Songs> songs;
    TextView textView, total;
    ImageButton addButton, deleteButton, updateButton;
    RecyclerView list;

    TextInputEditText filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db.onUpgrade(db.getWritableDatabase(), 0, 0);
        initialState();

        textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        total = findViewById(R.id.total);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);
        list = findViewById(R.id.list);
        filter = findViewById(R.id.filter);

        addButton.setOnClickListener(v -> addSong());
        deleteButton.setOnClickListener(v -> deleteSong());
        updateButton.setOnClickListener(v -> updateSong());

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateRecyclerView(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setupRecyclerView();
        getUniqueResults();
    }

    protected void initialState() {
        db.addSong(new com.example.mydbapp.Songs("Поворот", "Кутиков", 227, "10 лет спустя"));
        db.addSong(new com.example.mydbapp.Songs("Свеча", "Макаревич", 272, "Маленький принц"));
        db.addSong(new com.example.mydbapp.Songs("Скачки", "Кутиков", 198, "10 лет спустя"));
        db.addSong(new com.example.mydbapp.Songs("Кого ты хотел удивить?", "Кутиков", 303, "Кого ты хотел удивить?"));
        db.addSong(new com.example.mydbapp.Songs("Синяя птица", "Макаревич", 137, "Маленький принц"));
        db.addSong(new com.example.mydbapp.Songs("Скворец", "Макаревич", 231, "В добрый час"));
        db.addSong(new com.example.mydbapp.Songs("Марионетки", "Макаревич", 258, "10 лет спустя"));
        db.addSong(new com.example.mydbapp.Songs("Три окна", "Макаревич", 368, "Маленький принц"));
    }

    private void setupRecyclerView() {
        songs = db.getAllSongs();
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(songs);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        total.setText(String.format("Количество песен: %d", songs.size()));
    }

    private void addSong() {
        db.addSong(new com.example.mydbapp.Songs("Костер", "Макаревич", 214, "Медленная хорошая музыка"));
        Toast.makeText(this, "Песня добавлена", Toast.LENGTH_SHORT).show();
        updateRecyclerView();
    }

    private void deleteSong() {
        db.deleteSong(songs.get(0));
        Toast.makeText(this, "Песня удалена", Toast.LENGTH_SHORT).show();
        updateRecyclerView();
    }

    private void updateSong() {
        com.example.mydbapp.Songs firstSongs = songs.get(0);
        firstSongs.setAuthor("Цветков");
        db.updateSong(firstSongs);
        Toast.makeText(this, "Песня обновлена", Toast.LENGTH_SHORT).show();
        updateRecyclerView();
    }

    private String resultToString(String title, List<String> authors) {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        for (String author : authors) {
            sb.append(author).append("\n");
        }
        return sb.toString();
    }

    private void getUniqueResults() {
        StringBuilder sb = new StringBuilder();
        sb.append(resultToString("Названия песен в порядке уменьшения длительности их звучания:", db.getSongsSortedByDuration()));
        sb.append('\n');
        sb.append(resultToString("Названия песен в алфавитном порядке:", db.getSongsSortedByName()));
        sb.append('\n');
        sb.append(resultToString("Названия песен, написанных Андреем Макаревичем:", db.getSongsDependsOnAuthor("Макаревич")));
        sb.append('\n');
        sb.append(resultToString("Названия песен, написанных Александром Кутиковым:", db.getSongsDependsOnAuthor("Кутиков")));
        sb.append('\n');
        sb.append(resultToString("Названия песен альбома “10 лет спустя”, написанных Александром Кутиковым:", db.getSongsOfAlbumWithAuthor("10 лет спустя", "Кутиков")));
        textView.setText(sb.toString());
    }

    private void updateRecyclerView(String nameFilter) {
        List<com.example.mydbapp.Songs> filteredSongs;
        if (nameFilter.trim().isEmpty()) {
            filteredSongs = db.getAllSongs();
        } else {
            filteredSongs = db.getSongsByNameFilter(nameFilter);
        }
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(filteredSongs);
        list.setAdapter(adapter);
        total.setText(String.format("Количество песен: %d", filteredSongs.size()));
    }

    private void updateRecyclerView() {
        songs = db.getAllSongs();
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(songs);
        list.setAdapter(adapter);
        total.setText(String.format("Количество песен: %d", songs.size()));
    }
}
