package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * NotesAdapter - адаптер для RecyclerView.
 * Связывает данные (List<Note>) с элементами списка на экране.
 *
 * RecyclerView.Adapter<VH> - базовый класс для адаптера
 * VH - ViewHolder (хранит ссылки на View элементы)
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> notesList;          // Список заметок
    private OnNoteListener onNoteListener; // Интерфейс для обработки кликов

    /**
     * Интерфейс для обработки кликов по элементам списка
     * Паттерн "Слушатель" (Listener Pattern)
     */
    public interface OnNoteListener {
        void onNoteClick(int position);  // Вызывается при клике на заметку
    }

    /**
     * Конструктор адаптера
     */
    public NotesAdapter(List<Note> notesList, OnNoteListener onNoteListener) {
        this.notesList = notesList;
        this.onNoteListener = onNoteListener;
    }

    /**
     * Создает новый ViewHolder (вызывается для каждого элемента)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate - "надувает" XML layout в объекты View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view, onNoteListener);
    }

    /**
     * Связывает данные с ViewHolder (вызывается для каждой позиции)
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notesList.get(position);  // Получаем заметку по позиции

        // Устанавливаем данные в View элементы
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewContent.setText(note.getContent());
        holder.textViewDate.setText(note.getDate());

        // Ограничиваем длину содержимого
        if (note.getContent().length() > 100) {
            holder.textViewContent.setText(note.getContent().substring(0, 100) + "...");
        }
    }

    /**
     * Возвращает количество элементов в списке
     */
    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Обновляет список заметок
     */
    public void updateList(List<Note> newList) {
        notesList.clear();
        notesList.addAll(newList);
        notifyDataSetChanged();  // Уведомляем адаптер об изменении данных
    }

    /**
     * ViewHolder - паттерн для переиспользования View элементов
     * Хранит ссылки на View для быстрого доступа
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView textViewTitle, textViewContent, textViewDate;
        OnNoteListener onNoteListener;

        /**
         * Конструктор ViewHolder
         */
        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            // Находим View элементы по их ID
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewDate = itemView.findViewById(R.id.textViewDate);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);  // Устанавливаем слушатель кликов
        }

        /**
         * Обработчик клика по элементу
         */
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());  // Передаем позицию
        }
    }
}