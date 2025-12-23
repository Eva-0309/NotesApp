package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * AddEditNoteActivity - экран для создания и редактирования заметок
 */
public class AddEditNoteActivity extends AppCompatActivity {

    // Объявление View элементов
    private EditText editTextTitle, editTextContent;
    private Button buttonSave, buttonDelete;
    private DatabaseHelper databaseHelper;
    private int noteId = -1;  // -1 означает новую заметку

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        // Инициализация View элементов
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        databaseHelper = new DatabaseHelper(this);

        // Получаем ID заметки из Intent (если редактируем)
        noteId = getIntent().getIntExtra("note_id", -1);

        // Если noteId != -1, значит редактируем существующую заметку
        if (noteId != -1) {
            loadNoteData();  // Загружаем данные заметки
            buttonDelete.setVisibility(View.VISIBLE);  // Показываем кнопку удаления
        } else {
            buttonDelete.setVisibility(View.GONE);  // Скрываем кнопку удаления
        }

        // Обработчик кнопки сохранения
        buttonSave.setOnClickListener(v -> saveNote());

        // Обработчик кнопки удаления
        buttonDelete.setOnClickListener(v -> deleteNote());
    }

    /**
     * Загружает данные заметки для редактирования
     */
    private void loadNoteData() {
        Note note = databaseHelper.getNote(noteId);
        if (note != null) {
            editTextTitle.setText(note.getTitle());
            editTextContent.setText(note.getContent());
        }
    }

    /**
     * Сохраняет заметку (создает новую или обновляет существующую)
     */
    private void saveNote() {
        // Получаем текст из полей ввода
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        // Форматируем текущую дату и время
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(new Date());

        // Валидация: проверяем, что заголовок не пустой
        if (title.isEmpty()) {
            editTextTitle.setError("Введите заголовок");
            editTextTitle.requestFocus();  // Устанавливаем фокус на поле
            return;  // Прерываем выполнение метода
        }

        if (noteId == -1) {
            // Создание новой заметки
            databaseHelper.addNote(title, content, date);
            Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show();
        } else {
            // Обновление существующей заметки
            databaseHelper.updateNote(noteId, title, content, date);
            Toast.makeText(this, "Заметка обновлена", Toast.LENGTH_SHORT).show();
        }

        finish();  // Закрываем Activity и возвращаемся назад
    }

    /**
     * Удаляет текущую заметку
     */
    private void deleteNote() {
        if (noteId != -1) {
            databaseHelper.deleteNote(noteId);
            Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}