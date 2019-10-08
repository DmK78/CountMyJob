package com.dmk78.countmyjob.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmk78.countmyjob.Data.Job;
import com.dmk78.countmyjob.R;

import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsHolder> {
    private final List<Job> adapterJobs;
    private LayoutInflater inflater;
    private static OnJobAdapterClickListener callback;

    public JobsAdapter(Context context, List<Job> jobs) {
        this.inflater = LayoutInflater.from(context);
        this.adapterJobs = jobs;
    }

    @NonNull
    @Override
    public JobsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.info_job, parent, false);

        return new JobsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsHolder holder, int i) {
        final Job job = adapterJobs.get(i);
        holder.textViewEmployer.setText(job.getEmployer().getName());
        holder.textViewHours.setText("" + job.getHour());
        holder.textViewMoney.setText("" + job.getMoney());
        holder.textViewDesc.setText("" + job.getDesc());
        if (job.getCompleted() == 1) {
            holder.imageViewDone.setVisibility(View.VISIBLE);

        } else {
            holder.cardView.setAlpha(0.5f);

        }


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onJobEditJobClicked(job);
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onJobDeleteClicked(job);

            }
        });

    }

    @Override
    public int getItemCount() {
        return adapterJobs.size();
    }


    public class JobsHolder extends RecyclerView.ViewHolder {
        TextView textViewEmployer, textViewHours, textViewMoney, textViewDesc;
        ImageView imageViewDelete, imageViewDone;
        CardView cardView;
        View view;

        public JobsHolder(View itemView) {
            super(itemView);
            textViewEmployer = itemView.findViewById(R.id.textViewIjEmployer);
            textViewHours = itemView.findViewById(R.id.textViewIjHours);
            textViewMoney = itemView.findViewById(R.id.textViewIjMoney);
            textViewDesc = itemView.findViewById(R.id.textViewIjDesc);
            imageViewDone = itemView.findViewById(R.id.imageViewIjDone);
            imageViewDelete = itemView.findViewById(R.id.imageViewIjDelete);
            cardView = itemView.findViewById(R.id.cardViewJob);

            this.view = itemView;
        }


    }

    public void setOnItemClickListener(OnJobAdapterClickListener clickListener) {
        JobsAdapter.callback = clickListener;
    }

    public interface OnJobAdapterClickListener {

        public void onJobEditJobClicked(Job job);

        public void onJobDeleteClicked(Job job);

    }


}