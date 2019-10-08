package com.dmk78.countmyjob.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.R;

import java.util.List;

public class WorkDaysAdapter extends RecyclerView.Adapter<WorkDaysAdapter.WorkDayHolder> {
    private static OnWorkDayAdapterClickListener callback;
    private List<WorkDay> adapterWorkDays;
    private LayoutInflater inflater;

    public WorkDaysAdapter(Context context, List<WorkDay> workDays) {
        inflater = LayoutInflater.from(context);
        adapterWorkDays = workDays;
    }

    @NonNull
    @Override
    public WorkDaysAdapter.WorkDayHolder onCreateViewHolder(@NonNull final ViewGroup parent, int i) {
        final View view = inflater.inflate(R.layout.info_workday, parent, false);
        return new WorkDayHolder(view);
    }

    public void setData(List<WorkDay> workDays) {
        adapterWorkDays=workDays;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkDayHolder holder, int i) {
        final WorkDay workDay = adapterWorkDays.get(i);
        if (workDay.isAllJobsDone()){
            holder.imageViewWorkDayDone.setVisibility(View.VISIBLE);

        }
        holder.textViewDate.setText(workDay.getDay());
        holder.textViewHours.setText("" + workDay.getHours());
        holder.textViewMoney.setText("" + workDay.getMoney());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickWorkDay(workDay);
            }
        });
        holder.imageViewDeleteWorkday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickRemoveWorkday(v, workDay);
            }
        });

    }

    @Override
    public int getItemCount() {
        return adapterWorkDays.size();
    }

    public class WorkDayHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewHours, textViewMoney;
        ImageView imageViewDeleteWorkday, imageViewWorkDayDone;

        View view;

        public WorkDayHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewHours = itemView.findViewById(R.id.textViewHours);
            textViewMoney = itemView.findViewById(R.id.textViewMoney);
            imageViewDeleteWorkday = itemView.findViewById(R.id.imageViewDeleteWorkday);
            imageViewWorkDayDone = itemView.findViewById(R.id.imageViewWorkdayDone);
            this.view = itemView;
        }


    }

    public void setOnItemClickListener(OnWorkDayAdapterClickListener clickListener) {
        WorkDaysAdapter.callback = clickListener;
    }

    public interface OnWorkDayAdapterClickListener {
        void onClickWorkDay(WorkDay workDay);

        void onClickRemoveWorkday(View view, WorkDay workDay);
    }


}