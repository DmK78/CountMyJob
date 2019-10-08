package com.dmk78.countmyjob;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dmk78.countmyjob.Data.Employer;
import com.dmk78.countmyjob.Data.Job;
import com.dmk78.countmyjob.Data.WorkDay;
import com.dmk78.countmyjob.Utils.JobsDbHelper;

public class MainActivity extends AppCompatActivity implements WorkDayEditFragment.OnEditWorkdayClickListener,
        WorkDaysListFragment.OnWorkDayClickListener, AddJobFragment.OnAddJobClickListener,
        EmployersFragment.OnEmployerListClickListener, AddEmployerFragment.OnEmployerAddClickListener,
ReportsFragment.OnReportsClickListener{
    JobsDbHelper databaseHelper;
    public static final String JOB_ID = "jobId";
    public static final String JOB_DATE = "jobDate";
    public static final String WORK_DAY_ID = "workDayId";
    public static final String EMPLOYER_ID = "employerId";





    private final FragmentManager manager = getSupportFragmentManager();
    private Fragment fragmentWorkDays;
    private Fragment fragmentEmployers;
    private Fragment fragmentJobs;
    private Fragment fragmentAddJob;
    private Fragment fragmentAddEmployer;
    private Fragment fragmentReports;
    final int MAX_STREAMS = 5;
    SoundPool sp;
    int soundIdClick;
    int soundIdDing;
    int soundIdWarning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundIdClick = sp.load(this, R.raw.click, 1);
        soundIdDing = sp.load(this, R.raw.ding, 1);
        soundIdWarning = sp.load(this, R.raw.warning, 1);
        databaseHelper = new JobsDbHelper(getApplicationContext());
        fragmentWorkDays = manager.findFragmentById(R.id.mainContainer);
        if (fragmentWorkDays == null) {
            fragmentWorkDays = new WorkDaysListFragment();
            manager.beginTransaction()
                    .add(R.id.mainContainer, fragmentWorkDays)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.employers:
                sp.play(soundIdDing, 1, 1, 0, 0, 1);
                if (fragmentEmployers == null) {
                    fragmentEmployers = new EmployersFragment();
                }
                manager.beginTransaction()
                        .replace(R.id.mainContainer, fragmentEmployers)
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.workdays:
                sp.play(soundIdDing, 1, 1, 0, 0, 1);
                if (fragmentWorkDays == null) {
                    fragmentWorkDays = new WorkDaysListFragment();
                }
                manager.beginTransaction()
                        .replace(R.id.mainContainer, fragmentWorkDays)
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.reports:
                sp.play(soundIdDing, 1, 1, 0, 0, 1);
                if (fragmentReports == null) {
                    fragmentReports = new ReportsFragment();
                }
                manager.beginTransaction()
                        .replace(R.id.mainContainer, fragmentReports)
                        .addToBackStack(null)
                        .commit();
                return true;
          /*  case android.R.id.home:
                onBackPressed();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWorkDayEditCloseButtonClicked(WorkDay workDay) {
        sp.play(soundIdDing, 1, 1, 0, 0, 1);
        if(workDay.getJobs().isEmpty()){
            databaseHelper.deleteWorkDayFromDb(workDay);
        }else {
            databaseHelper.updateWorkDayToDb(workDay);
        }
        if (fragmentWorkDays == null) {
            fragmentWorkDays = new WorkDaysListFragment();
        }
        manager.beginTransaction()
                .remove(fragmentJobs)
                .commit();
        manager.popBackStack();
        /*manager.beginTransaction()

                .replace(R.id.mainContainer, fragmentWorkDays)
                .addToBackStack(null)
                .commit();*/
    }

    @Override
    public void onWorkDayEditJobButtonClicked(int workDayId, String workDayDate, Job job) {
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_ID,job.getId());
        bundle.putString(JOB_DATE,workDayDate);
        bundle.putInt(WORK_DAY_ID,workDayId);

        //if (fragmentAddJob == null) {
            fragmentAddJob = new AddJobFragment();
        //}
        fragmentAddJob.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentAddJob)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onWorkDayEditAddJobButtonClicked(WorkDay workDay) {
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        Bundle bundle = new Bundle();
        bundle.putString(JOB_DATE,workDay.getDay());
        bundle.putInt(WORK_DAY_ID,workDay.getId());


        //if (fragmentAddJob == null) {
            fragmentAddJob = new AddJobFragment();
        //}
        fragmentAddJob.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentAddJob)
                .addToBackStack(null)
                .commit();

    }




    @Override
    public void onWorkDayListButtonClicked(WorkDay workDay) {
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        Bundle bundle = new Bundle();
        bundle.putInt(WORK_DAY_ID,workDay.getId());
        databaseHelper.getWorkDayById(workDay.getId());
        if (fragmentJobs == null) {
            fragmentJobs = new WorkDayEditFragment();
        }
        fragmentJobs.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentJobs)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWorkDayListAddJobButtonClicked() {
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        //if (fragmentAddJob == null) {
            fragmentAddJob = new AddJobFragment();
        //}



        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentAddJob)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAddJobUpdateJobButtonClicked() {
        sp.play(soundIdDing, 1, 1, 0, 0, 1);
       if (fragmentJobs == null) {
            fragmentJobs = new WorkDayEditFragment();
        }
        manager.beginTransaction()
                .remove(fragmentAddJob)
                .commit();
        manager.popBackStack();
        /*manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentJobs)

                .addToBackStack(null)
                .commit();*/

    }

    @Override
    public void onAddJobCreateJobButtonClicked() {
        sp.play(soundIdDing, 1, 1, 0, 0, 1);
        if (fragmentWorkDays == null) {
            fragmentWorkDays = new WorkDaysListFragment();
        }
        manager.beginTransaction()
                .remove(fragmentAddJob)
                .commit();
        manager.popBackStack();
            manager.beginTransaction()
                    .replace(R.id.mainContainer, fragmentWorkDays)
                    .commit();
    }

    @Override
    public void onAddJobCancelButtonClicked() {
        sp.play(soundIdWarning, 1, 1, 0, 0, 1);

        //onBackPressed();
        manager.beginTransaction()
                .remove(fragmentAddJob)
                .commit();
        manager.popBackStack();

        /*if (fragmentWorkDays == null) {
            fragmentWorkDays = new WorkDaysListFragment();
        }
        manager.beginTransaction()
                .remove(fragmentAddJob)
                .commit();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentWorkDays)
                .addToBackStack(null)
                .commit();*/

    }

    @Override
    public void onEmployersAddEmployerButtonClicked() {
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        //if (fragmentAddEmployer == null) {
            fragmentAddEmployer = new AddEmployerFragment();
        //}

        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentAddEmployer)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onAddEmployerSaveButtonClicked(Employer employer) {
        sp.play(soundIdDing, 1, 1, 0, 0, 1);
        databaseHelper.saveEmployerToDb(employer);
        if (fragmentEmployers == null) {
            fragmentEmployers = new EmployersFragment();
        }
        manager.beginTransaction()
                .remove(fragmentAddEmployer)
                .commit();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentEmployers)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onAddEmployerCancelButtonClicked() {
        sp.play(soundIdWarning, 1, 1, 0, 0, 1);
        onBackPressed();

        /*if (fragmentEmployers == null) {
            fragmentEmployers = new EmployersFragment();
        }
        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentEmployers)
                .addToBackStack(null)
                .commit();*/

    }
    public void onBackPressed(){
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }





    @Override
    public void onReportsCloseButtonClicked() {
        sp.play(soundIdDing, 1, 1, 0, 0, 1);
        manager.beginTransaction()
                .remove(fragmentReports)
                .commit();
        manager.popBackStack();
    }

    @Override
    public void onReportsWorkdayButtonClicked(int workDayId, int employerId) {
        sp.play(soundIdClick, 1, 1, 0, 0, 1);
        if (fragmentJobs == null) {
            fragmentJobs = new WorkDayEditFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(WORK_DAY_ID,workDayId);
        bundle.putInt(EMPLOYER_ID,employerId);

        fragmentJobs.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.mainContainer, fragmentJobs)
                .addToBackStack(null)

                .commit();

    }
}
