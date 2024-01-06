package SandZ.Tutors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends ArrayAdapter<String> {
    private List<String> teacherSubjects;
    private List<String> subjects;
    private List<String> selectedSubjects; // Lista przechowująca zaznaczone przedmioty

    public SubjectAdapter(Context context, List<String> subjects, List<String> teacherSubjects) {
        super(context, R.layout.list_item_layout, subjects);
        this.subjects = subjects;
        this.teacherSubjects = teacherSubjects;
        this.selectedSubjects = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        String subject = getItem(position);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView textViewItem = convertView.findViewById(R.id.textViewItem);

        // Ustaw tekst dla każdego elementu
        if (subject != null) {
            textViewItem.setText(subject);

            // Sprawdź, czy nauczyciel ma ten przedmiot i ustaw stan CheckBoxa
            boolean isSelected = teacherSubjects.contains(subject);
            checkBox.setChecked(isSelected);

            // Dodaj przedmiot do listy zaznaczonych, jeśli jest zaznaczony
            if (isSelected) {
                selectedSubjects.add(subject);
            }

            // Dodaj słuchacza zmiany stanu CheckBoxa
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedSubjects.add(subject);
                } else {
                    selectedSubjects.remove(subject);
                }
            });
        }

        return convertView;
    }

    // Metoda zwracająca listę zaznaczonych przedmiotów
    public List<String> getSelectedSubjects() {
        return selectedSubjects;
    }
}
