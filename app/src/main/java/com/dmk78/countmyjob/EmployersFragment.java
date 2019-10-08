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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmk78.countmyjob.Data.Employer;
import com.dmk78.countmyjob.Data.Job;
import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.Utils.JobsDbHelper;

import java.util.ArrayList;
import java.util.List;

public class EmployersFragment extends Fragment {
    private JobsDbHelper databaseHelper;
    private OnEmployerListClickListener callback;
    private RecyclerView recycler;
    public static EmployersAdapter adapter;
    private List<Employer> employerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_employers_list, container, false);

        databaseHelper = new JobsDbHelper(getContext());
        employerList = databaseHelper.getAllEmployersFromDb();
        if (employerList.size() == 0) {
            databaseHelper.fillEmployersToBd();
            employerList = databaseHelper.getAllEmployersFromDb();
        }
        this.recycler = view.findViewById(R.id.recyclerEmployers);
        this.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new EmployersAdapter(getContext(), employerList);
        this.recycler.setAdapter(adapter);
        FloatingActionButton fab = view.findViewById(R.id.fabAddEmployer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onEmployersAddEmployerButtonClicked();

            }
        });
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnEmployerListClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    public class EmployersAdapter extends RecyclerView.Adapter<EmployersAdapter.EmployerHolder> {

        private final List<Employer> adapterEmployers;
        private LayoutInflater inflater;


        public EmployersAdapter(Context context, List<Employer> employers) {
            this.inflater = LayoutInflater.from(context);
            this.adapterEmployers = employers;
        }


        @NonNull
        @Override
        public EmployerHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = inflater.inflate(R.layout.info_employer, parent, false);
            return new EmployerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployerHolder holder, int i) {
            final Employer employer = adapterEmployers.get(i);
            holder.textViewName.setText(employer.getName());
            holder.textViewDesc.setText("" + employer.getDesc());
            holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<WorkDay> workDays = databaseHelper.getWorkDaysByEmployer(employer.getId());

                    if(workDays.size()!=0){
                      StringBuilder stringBuilder = new StringBuilder();
                      stringBuilder.append("Нельзя удалить!\n" +
                              "Связанные работы:\n");
                      for(WorkDay workDay:workDays){
                          stringBuilder.append(workDay.getDay()+"  часы:"+ workDay.getHours()+" деньги: "+ workDay.getMoney()+"\n");

                      }
                        Toast.makeText(getContext(), stringBuilder, Toast.LENGTH_LONG).show();
                      return;
                    }

                    final AlertDialog confimDeleteJobDialog = new AlertDialog.Builder(getContext()).setMessage("Delete item?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseHelper.deleteEmployerFromDb(employer.getId());
                                    employerList.remove(employerList.indexOf(employer));
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

        }

        @Override
        public int getItemCount() {
            return adapterEmployers.size();
        }

        public class EmployerHolder extends RecyclerView.ViewHolder {
            TextView textViewName, textViewDesc;
            ImageView imageViewDelete;

            View view;

            public EmployerHolder(View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.textViewEmpName);
                textViewDesc = itemView.findViewById(R.id.textViewEmpDesc);
                imageViewDelete = itemView.findViewById(R.id.imageViewDeleteEmployer);
                this.view = itemView;
            }
        }
    }

    public interface OnEmployerListClickListener {
        void onEmployersAddEmployerButtonClicked();

    }
}
