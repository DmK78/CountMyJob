package com.dmk78.countmyjob;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmk78.countmyjob.Data.Employer;
import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.Utils.JobsDbHelper;
import com.dmk78.countmyjob.Utils.ReportAdapter;
import com.dmk78.countmyjob.Utils.WorkDaysAdapter;

import java.nio.file.Watchable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ReportsFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {
    private SharedPreferences sharedPreferences;
    public static final String MY_SETTINGS = "my_settings";
    public static final String APP_PREFERENCES_EMPLOYER_ID = "employerId"; // имя кота
    public static final String APP_PREFERENCES_DATE1 = "date1"; // имя кота
    public static final String APP_PREFERENCES_DATE2 = "date2"; // возраст кота
    private JobsDbHelper dbHelper;
    private OnReportsClickListener callback;
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    private List<WorkDay> workDays;
    private RecyclerView recycler;
    private Spinner spinner;
    private ReportAdapter adapter;
    private int employerId;
    private int moneyTotal = 0, hoursTotal = 0;
    float forOneHour = 0f;
    private Button buttonClose;
    private String date1, date2;
    private TextView textViewData1, textViewData2, textViewHours, textViewMoney, textViewForOneHour;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reports, container, false);


        spinner = view.findViewById(R.id.spinnerReports);
        spinner.setOnItemSelectedListener(this);
        buttonClose = view.findViewById(R.id.buttonRepClose);
        textViewData1 = view.findViewById(R.id.textViewRepData1);
        textViewData2 = view.findViewById(R.id.textViewRepData2);
        textViewForOneHour = view.findViewById(R.id.textViewRepForOneHour);
        textViewMoney = view.findViewById(R.id.textViewRepMoney);
        textViewHours = view.findViewById(R.id.textViewRepHours);
        dbHelper = new JobsDbHelper(getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        date1 = sharedPreferences.getString(APP_PREFERENCES_DATE1, "");
        date2 = sharedPreferences.getString(APP_PREFERENCES_DATE2, "");
        employerId = sharedPreferences.getInt(APP_PREFERENCES_EMPLOYER_ID, -1);
        if (date1.equals("")) {
            setInitDates();
        }else {
            textViewData1.setText(date1);
            textViewData2.setText(date2);
        }

        loadSpinnerData();
        workDays = dbHelper.getWorkDaysByEmployer(employerId, textViewData1.getText().toString(), textViewData2.getText().toString());
        recycler = view.findViewById(R.id.recyclerReports);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReportAdapter(getContext(), workDays);
        /*adapter.setOnItemClickListener(new WorkDaysAdapter.OnWorkDayAdapterClickListener() {
            @Override
            public void onClickWorkDay(WorkDay workDay) {

                callback.onReportsWorkdayButtonClicked(workDay.getId(), employerId);
            }

            @Override
            public void onClickRemoveWorkday(View view, WorkDay workDay) {
                Toast.makeText(getContext(), "Soon", Toast.LENGTH_SHORT).show();

            }
        });*/
        recycler.setAdapter(adapter);

        textViewData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ChooseDataDialogFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "date1");
            }
        });
        textViewData1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshReportView();
                savePreferences(APP_PREFERENCES_DATE1, textViewData1.getText().toString());
            }
        });
        textViewData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ChooseDataDialogFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "date2");
            }
        });
        textViewData2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshReportView();
                savePreferences(APP_PREFERENCES_DATE2, textViewData2.getText().toString());
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onReportsCloseButtonClicked();
            }
        });


        return view;
    }

    private void setInitDates() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
// get start of the month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date chosenDate = cal.getTime();
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = output.format(chosenDate);
        textViewData1.setText(formattedDate);

// get start of the next month
        cal.add(Calendar.MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, 0);
        chosenDate = cal.getTime();
        output = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = output.format(chosenDate);
        textViewData2.setText(formattedDate);
    }

    private void loadSpinnerData() {
        List<Employer> employers = dbHelper.getAllEmployersFromDb();
        /*if (employers.size() == 0) {
            dbHelper.fillEmployersToBd();
            employers = dbHelper.getAllEmployersFromDb();

        }*/
        if(employerId==0){
            employerId = employers.get(0).getId();
        }
        ArrayAdapter<Employer> spinnerAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, employers);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();
        Employer employer = (Employer) parent.getItemAtPosition(position);
        employerId = employer.getId();
        savePreferences(APP_PREFERENCES_EMPLOYER_ID, employerId);
        refreshReportView();
    }

    private void refreshReportView() {
        if (adapter != null) {
            workDays = dbHelper.getWorkDaysByEmployer(employerId, textViewData1.getText().toString(), textViewData2.getText().toString());
            adapter.setData(workDays);
            hoursTotal = 0;
            moneyTotal = 0;
            forOneHour = 0;

            for (WorkDay workDay : workDays) {
                hoursTotal += workDay.getHours();
                moneyTotal += workDay.getMoney();
                forOneHour = moneyTotal / hoursTotal;
            }
            textViewHours.setText(String.valueOf(hoursTotal));
            textViewMoney.setText(String.valueOf(moneyTotal));
            textViewForOneHour.setText(String.valueOf(forOneHour));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnReportsClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    public interface OnReportsClickListener {
        public void onReportsCloseButtonClicked();

        public void onReportsWorkdayButtonClicked(int wordayId, int employerId);


    }

    private void savePreferences(String key, int value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void savePreferences(String key, String value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


}
