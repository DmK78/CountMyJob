package com.dmk78.countmyjob;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmk78.countmyjob.Data.Employer;
import com.dmk78.countmyjob.Data.Job;
import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.Utils.JobsDbHelper;

import java.util.List;

public class AddJobFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {
    private TextView textViewChooseDate;
    private int employerId;
    private Spinner spinner;
    private JobsDbHelper dbHelper;
    private EditText editTextHours, editTextMoney, editTextDesc;
    private CheckBox checkBoxJobIsDone;
    private Button buttonSave, buttonCancel;
    private int jobIsDone;
    private Job job;
    private String jobDate;
    private int workDayId;
    private int jobId;
    private TextValidator validator;


    private OnAddJobClickListener callback;

    private boolean editedFromWorkDay = false;
    private boolean createdFromWorkDay = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add_job, container, false);
        validator = new TextValidator(getContext());
        textViewChooseDate = view.findViewById(R.id.textViewAjChooseDate);
        buttonSave = view.findViewById(R.id.buttonAjSaveJob);
        buttonCancel = view.findViewById(R.id.buttonAjJobCancel);
        editTextMoney = view.findViewById(R.id.editTextAjMoney);
        editTextHours = view.findViewById(R.id.editTextAjTime);
        editTextDesc = view.findViewById(R.id.editTextAjDesc);

        editTextDesc.setImeOptions(EditorInfo.IME_ACTION_DONE);
//This will treat Enter as Done instead of new line:
        editTextDesc.setRawInputType(InputType.TYPE_CLASS_TEXT);

        checkBoxJobIsDone = view.findViewById(R.id.checkBoxAjDone);
        spinner = view.findViewById(R.id.spinnerAjEmployer);
        dbHelper = new JobsDbHelper(getContext());
        spinner.setOnItemSelectedListener(this);

        if (getArguments() != null) {
            jobDate = getArguments().getString(MainActivity.JOB_DATE);
            jobId = getArguments().getInt(MainActivity.JOB_ID);
            workDayId = getArguments().getInt(MainActivity.WORK_DAY_ID);
            if (jobId > 0) {
                job = dbHelper.getJobFromDb(jobId);
                editTextMoney.setText("" + job.getMoney());
                editTextHours.setText("" + job.getHour());
                editTextDesc.setText("" + job.getDesc());
                checkBoxJobIsDone.setChecked(job.getCompleted() == 1 ? true : false);
                editedFromWorkDay = true;
                textViewChooseDate.setText(jobDate);
            } else if (jobDate!=null) {
                createdFromWorkDay = true;
                textViewChooseDate.setText(jobDate);
            }

        }
        loadSpinnerData();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validator.validateAddJob(view)) {


                    if (editedFromWorkDay) {
                        job.setEmployer((Employer) spinner.getSelectedItem());
                        job.setDesc(editTextDesc.getText().toString());
                        job.setHour(Integer.valueOf(editTextHours.getText().toString()));
                        job.setMoney(Integer.valueOf(editTextMoney.getText().toString()));
                        job.setCompleted(checkBoxJobIsDone.isChecked() ? 1 : 0);
                        dbHelper.updateJobToDb(job);
                        WorkDay workDay = dbHelper.getWorkDayById(workDayId);

                        workDay.recountJobs();
                        dbHelper.updateWorkDayToDb(workDay);
                        callback.onAddJobUpdateJobButtonClicked();

                    /*Job job = new Job(workDayId, (Employer) spinner.getSelectedItem(), Integer.valueOf(editTextHours.getText().toString()),
                            Integer.valueOf(editTextMoney.getText().toString()), jobIsDone, editTextDesc.getText().toString());
                    */

                    } else if (createdFromWorkDay) {
                        Job job = new Job(workDayId, (Employer) spinner.getSelectedItem(), Integer.valueOf(editTextHours.getText().toString()),
                                Integer.valueOf(editTextMoney.getText().toString()), jobIsDone, editTextDesc.getText().toString());
                        dbHelper.saveJobToDb(workDayId, job);
                        WorkDay workDay = dbHelper.getWorkDayById(workDayId);
                        workDay.recountJobs();
                        dbHelper.updateWorkDayToDb(workDay);
                        callback.onAddJobUpdateJobButtonClicked();


                    } else {

                        WorkDay workDay = dbHelper.getWorkDayByDate(textViewChooseDate.getText().toString());
                        if (workDay == null) {
                            dbHelper.saveWorkDayToDb(new WorkDay(textViewChooseDate.getText().toString(), null, 0, 0));
                            workDay = dbHelper.getWorkDayByDate(textViewChooseDate.getText().toString());
                        }
                        Job job = new Job(workDay.getId(), (Employer) spinner.getSelectedItem(), Integer.valueOf(editTextHours.getText().toString()),
                                Integer.valueOf(editTextMoney.getText().toString()), jobIsDone, editTextDesc.getText().toString());
                        dbHelper.saveJobToDb(workDay.getId(), job);
                        workDay.setJobs(dbHelper.getJobsFromDb(workDay.getId()));
                        workDay.recountJobs();
                        dbHelper.updateWorkDayToDb(workDay);
                        callback.onAddJobCreateJobButtonClicked();

                    }


                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                callback.onAddJobCancelButtonClicked();
            }
        });
        checkBoxJobIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    jobIsDone = 1;
                } else {
                    jobIsDone = 0;
                }
            }
        });
        textViewChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ChooseDataDialogFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });


        return view;
    }


    private void loadSpinnerData() {
        List<Employer> employers = dbHelper.getAllEmployersFromDb();
        if (employers.size() == 0) {
            dbHelper.fillEmployersToBd();
            employers = dbHelper.getAllEmployersFromDb();
        }
        ArrayAdapter<Employer> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, employers);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        if (editedFromWorkDay) {
            int spinnerSelection = spinnerAdapter.getPosition(job.getEmployer());
            spinner.setSelection(spinnerAdapter.getPosition(job.getEmployer()));
        } else {
            spinner.setSelection(0);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();
        Employer employer = (Employer) parent.getItemAtPosition(position);
        employerId = employer.getId();
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnAddJobClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    public interface OnAddJobClickListener {
        void onAddJobUpdateJobButtonClicked();

        void onAddJobCreateJobButtonClicked();

        void onAddJobCancelButtonClicked();

    }


}

