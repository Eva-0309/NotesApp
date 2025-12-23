package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity - главный экран приложения.
 * Показывает список заметок и кнопку для добавления.
 *
 * AppCompatActivity - базовый класс для Activity
 */
public class MainActivity extends AppCompatActivity
        implements NotesAdapter.OnNoteListener {

    // Объявление переменных (полей класса)
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Note> notesList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    /**
     * onCreate - вызывается при создании Activity
     * Основной метод для инициализации
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Устанавливаем layout

        // Инициализация базы данных
        databaseHelper = new DatabaseHelper(this);

        // Находим View элементы по ID (findViewById)
        recyclerView = findViewById(R.id.recyclerView);
        SearchView searchView = findViewById(R.id.searchView);
        FloatingActionButton fabAddNote = findViewById(R.id.fabAddNote);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Вертикальный список
        adapter = new NotesAdapter(notesList, this);  // Создаем адаптер
        recyclerView.setAdapter(adapter);  // Устанавливаем адаптер

        loadNotes();  // Загружаем заметки из БД

        /**
         * Обработчик клика по кнопке добавления заметки
         * Лямбда-выражение (Java 8+)
         */
        fabAddNote.setOnClickListener(v -> {
            // Intent - намерение перейти к другой Activity
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivity(intent);  // Запускаем новую Activity
        });

        /**
         * Обработчик поиска
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);  // Фильтруем заметки при вводе текста
                return true;
            }
        });
    }

    /**
     * Загружает заметки из базы данных
     */
    private void loadNotes() {
        notesList.clear();  // Очищаем текущий список
        notesList.addAll(databaseHelper.getAllNotes());  // Загружаем из БД
        adapter.notifyDataSetChanged();  // Обновляем список на экране
    }

    /**
     * Фильтрует заметки по тексту поиска
     */
    private void filterNotes(String text) {
        List<Note> filteredList = new ArrayList<>();

        // Проходим по всем заметкам
        for (Note note : databaseHelper.getAllNotes()) {
            // Проверяем, содержит ли заголовок или текст искомую строку
            if (note.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    note.getContent().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(note);
            }
        }

        adapter.updateList(filteredList);  // Обновляем список
    }

    /**
     * Обработчик клика по заметке в списке
     * Реализация метода из интерфейса OnNoteListener
     */
    @Override
    public void onNoteClick(int position) {
        Note note = notesList.get(position);  // Получаем заметку по позиции

        // Создаем Intent для редактирования
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        intent.putExtra("note_id", note.getId());  // Передаем ID заметки
        startActivity(intent);
    }

    /**
     * onResume - вызывается при возвращении на этот экран
     * Обновляем список заметок
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }
}