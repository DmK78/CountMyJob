package com.dmk78.countmyjob;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TextValidator {
    final int MAX_STREAMS = 5;
    SoundPool sp;
    int soundIdClick;
    int soundIdDing;
    int soundIdWarning;
    Context context;

    public TextValidator(Context context) {
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundIdClick = sp.load(context, R.raw.click, 1);
        soundIdDing = sp.load(context, R.raw.ding, 1);
        soundIdWarning = sp.load(context, R.raw.warning, 1);
    }

    public boolean validateAddJob(View view) {
        boolean result = false;
        TextView textViewChooseDate = view.findViewById(R.id.textViewAjChooseDate);
        EditText editTextMoney = view.findViewById(R.id.editTextAjMoney);
        EditText editTextHours = view.findViewById(R.id.editTextAjTime);
        EditText editTextDesc = view.findViewById(R.id.editTextAjDesc);
        if (editTextHours.getText().length() == 0) {
            sp.play(soundIdWarning, 1, 1, 0, 0, 1);
            Toast.makeText(view.getContext(), "Введите часы", Toast.LENGTH_SHORT).show();
            editTextHours.requestFocus();
        } else if (editTextMoney.getText().length() == 0) {
            sp.play(soundIdWarning, 1, 1, 0, 0, 1);
            Toast.makeText(view.getContext(), "Введите деньги", Toast.LENGTH_SHORT).show();
            editTextMoney.requestFocus();
        } else if (textViewChooseDate.getText().length() > 10) {
            sp.play(soundIdWarning, 1, 1, 0, 0, 1);
            Toast.makeText(view.getContext(), "Выберите дату", Toast.LENGTH_SHORT).show();
            textViewChooseDate.callOnClick();
        } else {
            result = true;
        }

        return result;
    }

    public boolean validateAddEmployer(View view) {
        boolean result = false;
        EditText editTextName = view.findViewById(R.id.editTextEmpName);
        if (editTextName.getText().length() == 0) {
            sp.play(soundIdWarning, 1, 1, 0, 0, 1);
            Toast.makeText(view.getContext(), "Введите имя", Toast.LENGTH_SHORT).show();
            editTextName.requestFocus();
        } else result = true;
        return result;

    }
}
