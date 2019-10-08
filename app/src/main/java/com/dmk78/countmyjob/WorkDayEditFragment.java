package com.dmk78.countmyjob;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dmk78.countmyjob.Data.Job;
import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.Utils.JobsAdapter;
import com.dmk78.countmyjob.Utils.JobsDbHelper;

import java.util.ArrayList;
import java.util.List;

public class WorkDayEditFragment extends android.support.v4.app.Fragment {
    private JobsDbHelper databaseHelper;
    private RecyclerView recycler;
    private int workDayId;

    private AlertDialog.Builder confirmDeleteDialog;
    private Button buttonClose;
    private OnEditWorkdayClickListener callback;
    private WorkDay workDay;
    private Job job;
    private JobsAdapter adapter;
    private int employerId;
    private TextView textViewDateTitle, textViewHoursTitle, textViewMoneyTitle;
    private List<Job> jobList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_workday_edit, container, false);
        databaseHelper = new JobsDbHelper(getContext());
        if (getArguments() != null) {
            workDayId = getArguments().getInt(MainActivity.WORK_DAY_ID, -1);
            employerId = getArguments().getInt(MainActivity.EMPLOYER_ID, -1);
        }
        if (employerId != -1) {
            workDay = databaseHelper.getWorkDayByEmployer(workDayId, employerId);
        } else if (workDayId != -1) {
            workDay = databaseHelper.getWorkDayById(workDayId);
        }

        textViewDateTitle = view.findViewById(R.id.textViewEwDate);
        textViewHoursTitle = view.findViewById(R.id.textViewEwHours);
        textViewMoneyTitle = view.findViewById(R.id.textViewEwMoney);
        buttonClose = view.findViewById(R.id.buttonEwClose);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onWorkDayEditCloseButtonClicked(workDay);
            }
        });
        fillTitle();

        jobList = workDay.getJobs();


        this.recycler = view.findViewById(R.id.recyclerEditWorkDay);
        this.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new JobsAdapter(getContext(), jobList);
        adapter.setOnItemClickListener(new JobsAdapter.OnJobAdapterClickListener() {

            @Override
            public void onJobEditJobClicked(Job job) {
                callback.onWorkDayEditJobButtonClicked(workDay.getId(),workDay.getDay(), job);
            }

            @Override
            public void onJobDeleteClicked(final Job job) {

                final AlertDialog confimDeleteJobDialog = new AlertDialog.Builder(
                        getContext()).setMessage("Delete item?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHelper.deleteJobfromDb(job.getId());
                                jobList.remove(jobList.indexOf(job));
                                workDay.recountJobs();
                                fillTitle();
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
                confimDeleteJobDialog.show();

            }
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddJob);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onWorkDayEditAddJobButtonClicked(workDay);

            }
        });


        return view;
    }


    private void fillTitle() {
        textViewMoneyTitle.setText("" + workDay.getMoney());
        textViewHoursTitle.setText("" + workDay.getHours());
        textViewDateTitle.setText(workDay.getDay());
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnEditWorkdayClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            adapter.notifyDataSetChanged();
        }
    }


    public interface OnEditWorkdayClickListener {
        public void onWorkDayEditCloseButtonClicked(WorkDay workDay);

        public void onWorkDayEditJobButtonClicked(int worDayId, String workDayDate, Job job);

        public void onWorkDayEditAddJobButtonClicked(WorkDay workDay);


    }


}
