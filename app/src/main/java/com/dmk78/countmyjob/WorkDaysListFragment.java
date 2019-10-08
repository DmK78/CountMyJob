package com.dmk78.countmyjob;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.Utils.JobsDbHelper;
import com.dmk78.countmyjob.Utils.WorkDaysAdapter;

import java.util.List;

public class WorkDaysListFragment extends Fragment {
    private OnWorkDayClickListener callback;
    private WorkDay workDay;
    private JobsDbHelper databaseHelper;
    private RecyclerView recycler;
    public WorkDaysAdapter adapter;
    private List<WorkDay> workDays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_workdays_list, container, false);
        databaseHelper = new JobsDbHelper(getContext());
        workDays = databaseHelper.getAllWorkDaysFromDb();

        FloatingActionButton fab = view.findViewById(R.id.fabAddJob);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onWorkDayListAddJobButtonClicked();
            }
        });
        if (workDays.size() == 0) {
            databaseHelper.fillWorkDaysToBd();
            workDays = databaseHelper.getAllWorkDaysFromDb();
        }
        this.recycler = view.findViewById(R.id.recyclerWorkDays);
        this.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WorkDaysAdapter(getContext(), workDays);
        adapter.setOnItemClickListener(new WorkDaysAdapter.OnWorkDayAdapterClickListener() {
            @Override
            public void onClickWorkDay(WorkDay workDay) {
                callback.onWorkDayListButtonClicked(workDay);
                Toast.makeText(getContext(), "Workday id = " + workDay.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickRemoveWorkday(View view1, final WorkDay workDayFromAdapter) {
                workDay = workDayFromAdapter;
                final AlertDialog confimDeleteJobDialog = new AlertDialog.Builder(view.getContext()).setMessage("Delete item?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHelper.deleteWorkDayFromDb(workDay);
                                workDays.remove(workDays.indexOf(workDayFromAdapter));
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
        this.recycler.setAdapter(adapter);
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnWorkDayClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

public interface OnWorkDayClickListener{
        void onWorkDayListButtonClicked(WorkDay workDay);
        void onWorkDayListAddJobButtonClicked();
}


}



