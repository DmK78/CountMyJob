package com.dmk78.countmyjob.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmk78.countmyjob.Data.Job;
import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.R;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportHolder> {

    private List<WorkDay> workDays;
    private LayoutInflater inflater;

    public ReportAdapter(Context context, List<WorkDay> workDays) {
        inflater = LayoutInflater.from(context);
        this.workDays = workDays;
    }

    @NonNull
    @Override
    public ReportAdapter.ReportHolder onCreateViewHolder(@NonNull final ViewGroup parent, int i) {
        final View view = inflater.inflate(R.layout.info_workday_report, parent, false);
        return new ReportHolder(view);
    }

    public void setData(List<WorkDay> workDays) {
        this.workDays=workDays;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ReportHolder holder, int i) {
        final WorkDay workDay = workDays.get(i);

        holder.textViewDate.setText(workDay.getDay());
        holder.textViewHours.setText("" + workDay.getHours());
        holder.textViewMoney.setText("" + workDay.getMoney());
        for(Job job:workDay.getJobs()){
            holder.textViewJobHours.append(job.getHour()+"\n");
            holder.textViewJobMoney.append(job.getMoney()+"\n");
            holder.textViewJobDesc.append(job.getDesc()+"\n");
        }



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return workDays.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewHours, textViewMoney, textViewJobHours, textViewJobMoney, textViewJobDesc;


        View view;

        public ReportHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewHours = itemView.findViewById(R.id.textViewHours);
            textViewMoney = itemView.findViewById(R.id.textViewMoney);
            textViewJobHours = itemView.findViewById(R.id.textViewJobHours);
            textViewJobMoney = itemView.findViewById(R.id.textViewJobMoney);
            textViewJobDesc = itemView.findViewById(R.id.textViewJobDesc);


            this.view = itemView;
        }


    }






}