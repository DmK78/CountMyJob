package com.dmk78.countmyjob;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dmk78.countmyjob.Data.Employer;

public class AddEmployerFragment extends Fragment {
    private OnEmployerAddClickListener callback;
    private EditText editTextName, editTextDesc;
    private Button buttonSave, buttonCancel;
    private TextValidator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add_employer, container, false);
        validator=new TextValidator(getContext());
        editTextName = view.findViewById(R.id.editTextEmpName);

        editTextDesc = view.findViewById(R.id.editTextEmpDesc);
        buttonSave=view.findViewById(R.id.buttonEmpSave);
        buttonCancel=view.findViewById(R.id.buttonEmpCancel);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validator.validateAddEmployer(view)){

                    Employer employer = new Employer(editTextName.getText().toString(), editTextDesc.getText().toString());
                    callback.onAddEmployerSaveButtonClicked(employer);
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    callback.onAddEmployerCancelButtonClicked();

            }
        });


        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnEmployerAddClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    public interface OnEmployerAddClickListener {
        void onAddEmployerSaveButtonClicked(Employer employer);

        void onAddEmployerCancelButtonClicked();
    }
}
