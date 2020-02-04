package com.dmk78.countmyjob.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dmk78.countmyjob.Data.Employer;
import com.dmk78.countmyjob.Data.Job;
import com.dmk78.countmyjob.Data.WorkDay;

import java.util.ArrayList;
import java.util.List;

public class JobsDbHelper extends SQLiteOpenHelper {
    SQLiteDatabase dbRead = getReadableDatabase();
    SQLiteDatabase dbWrite = getWritableDatabase();

    public static final String DB = "workdays.db";
    public static final int VERSION = 18;

    public JobsDbHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JobsDbShema.WorkDaysTable.CREATE_TABLE);
        db.execSQL(JobsDbShema.JobsTable.CREATE_TABLE);
        db.execSQL(JobsDbShema.EmployersTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + JobsDbShema.WorkDaysTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + JobsDbShema.JobsTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + JobsDbShema.EmployersTable.NAME);
        onCreate(db);
    }

    public List<WorkDay> fillWorkDaysToBd() {
        List<WorkDay> result = new ArrayList<>();
        /*for (int i = 1; i < 30; i++) {
            ContentValues valueExam = new ContentValues();
            String date = i > 9 ? String.valueOf(i) : "0" + i;
            valueExam.put(JobsDbShema.WorkDaysTable.Cols.DAY, date + ".09.2019");
            valueExam.put(JobsDbShema.WorkDaysTable.Cols.HOURS, 5);
            valueExam.put(JobsDbShema.WorkDaysTable.Cols.MONEY, 2000);
            dbWrite.insert(JobsDbShema.WorkDaysTable.NAME, null, valueExam);
        }*/
        return result;
    }

    public List<WorkDay> getAllWorkDaysFromDb() {
        List<WorkDay> result = new ArrayList<>();
        Cursor cursor = dbRead.query(
                JobsDbShema.WorkDaysTable.NAME,
                null, null, null,
                null, null, JobsDbShema.WorkDaysTable.Cols.DAY
        );
        if (cursor.moveToFirst()) {
            do {
                WorkDay workDay = new WorkDay(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.DAY)),
                        new ArrayList<Job>(),
                        cursor.getInt(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.HOURS)),
                        cursor.getInt(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.MONEY))
                );
                List<Job> jobs = getJobsFromDb(workDay.getId());
                workDay.setJobs(jobs);
                result.add(workDay);

            } while (cursor.moveToNext());
        }
        return result;
    }

    public List<Employer> getAllEmployersFromDb() {
        List<Employer> result = new ArrayList<>();
        Cursor cursor = dbRead.query(
                JobsDbShema.EmployersTable.NAME,
                null, null, null,
                null, null, null
        );
        if (cursor.moveToFirst()) {
            do {
                result.add(new Employer(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex(JobsDbShema.EmployersTable.Cols.NAME)),
                        cursor.getString(cursor.getColumnIndex(JobsDbShema.EmployersTable.Cols.DESC))
                ));
            } while (cursor.moveToNext());
        } else fillEmployersToBd();
        return result;
    }

    public void fillEmployersToBd() {

        /*for (int i = 1; i < 30; i++) {
            ContentValues value = new ContentValues();
            value.put(JobsDbShema.EmployersTable.Cols.NAME, "Employer " + i);
            value.put(JobsDbShema.EmployersTable.Cols.DESC, "desc " + i);
            dbWrite.insert(JobsDbShema.EmployersTable.NAME, null, value);
        }*/


    }

    public WorkDay getWorkDayById(int workDayId) {
        WorkDay result = null;
        List<Job> resultJobs = new ArrayList<>();
        String selectionWorkday = "id =?";
        String[] selectionArgsExam = new String[]{String.valueOf(workDayId)};
        Cursor cursor = dbRead.query(
                JobsDbShema.WorkDaysTable.NAME,
                null, selectionWorkday, selectionArgsExam,
                null, null, null
        );
        cursor.moveToFirst();
        result = new WorkDay(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.DAY)),
                resultJobs,
                cursor.getInt(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.HOURS)),
                cursor.getInt(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.MONEY)));
        cursor.close();

        String selectionJobs = JobsDbShema.JobsTable.Cols.WORKDAY_ID + " =?";
        String[] selectionArgsQuestion = new String[]{String.valueOf(workDayId)};
        Cursor cursorJobs = dbRead.query(
                JobsDbShema.JobsTable.NAME,
                null, selectionJobs, selectionArgsQuestion,
                null, null, null
        );
        if (cursorJobs.moveToFirst()) {
            do {
                Employer employer = null;
                Job job = new Job(
                        cursorJobs.getInt(cursorJobs.getColumnIndex("id")),
                        employer,
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.HOURS)),
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.MONEY)),
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.COMPLETED)),
                        cursorJobs.getString(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.DESC))
                );
                int employerId = cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.EMPLOYER_ID));
                String selectionOption = "id =?";
                String[] selectionArgsEmployer = new String[]{String.valueOf(employerId)};
                Cursor cursorEmployer = dbRead.query(
                        JobsDbShema.EmployersTable.NAME,
                        null, selectionOption, selectionArgsEmployer,
                        null, null, null
                );
                cursorEmployer.moveToFirst();
                job.setEmployer(new Employer(
                        cursorEmployer.getInt(cursorEmployer.getColumnIndex("id")),
                        cursorEmployer.getString(cursorEmployer.getColumnIndex(JobsDbShema.EmployersTable.Cols.NAME)),
                        cursorEmployer.getString(cursorEmployer.getColumnIndex(JobsDbShema.EmployersTable.Cols.DESC))));
                cursorEmployer.close();
                resultJobs.add(job);
            } while (cursorJobs.moveToNext());
        }
        return result;
    }

    public void saveJobToDb(int wordayId, Job job) {
        ContentValues value = new ContentValues();
        value.put(JobsDbShema.JobsTable.Cols.WORKDAY_ID, wordayId);
        value.put(JobsDbShema.JobsTable.Cols.EMPLOYER_ID, job.getEmployer().getId());
        value.put(JobsDbShema.JobsTable.Cols.HOURS, job.getHour());
        value.put(JobsDbShema.JobsTable.Cols.MONEY, job.getMoney());
        value.put(JobsDbShema.JobsTable.Cols.COMPLETED, job.getCompleted());
        value.put(JobsDbShema.JobsTable.Cols.DESC, job.getDesc());
        dbWrite.insert(JobsDbShema.JobsTable.NAME, null, value);
    }

    public void saveEmployerToDb(Employer employer) {
        ContentValues value = new ContentValues();
        value.put(JobsDbShema.EmployersTable.Cols.NAME, employer.getName());
        value.put(JobsDbShema.EmployersTable.Cols.DESC, employer.getDesc());
        dbWrite.insert(JobsDbShema.EmployersTable.NAME, null, value);
    }

    public void saveWorkDayToDb(WorkDay workDay) {
        ContentValues value = new ContentValues();
        value.put(JobsDbShema.WorkDaysTable.Cols.DAY, workDay.getDay());
        value.put(JobsDbShema.WorkDaysTable.Cols.HOURS, workDay.getHours());
        value.put(JobsDbShema.WorkDaysTable.Cols.MONEY, workDay.getMoney());
        dbWrite.insert(JobsDbShema.WorkDaysTable.NAME, null, value);
    }

    public Employer getEmployerFromDb(int id) {
        Employer result = null;
        String selectionWorkday = "id =?";
        String[] selectionArgsExam = new String[]{String.valueOf(id)};
        Cursor cursor = dbRead.query(
                JobsDbShema.EmployersTable.NAME,
                null, selectionWorkday, selectionArgsExam,
                null, null, null
        );
        cursor.moveToFirst();
        result = new Employer(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex(JobsDbShema.EmployersTable.Cols.NAME)),
                cursor.getString(cursor.getColumnIndex(JobsDbShema.EmployersTable.Cols.DESC)));
        cursor.close();
        return result;
    }

    public List<Job> getJobsFromDb(int id) {
        List<Job> result = new ArrayList<>();
        String selectionJobs = JobsDbShema.JobsTable.Cols.WORKDAY_ID + " =?";
        String[] selectionArgsQuestion = new String[]{String.valueOf(id)};
        Cursor cursorJobs = dbRead.query(
                JobsDbShema.JobsTable.NAME,
                null, selectionJobs, selectionArgsQuestion,
                null, null, null
        );
        if (cursorJobs.moveToFirst()) {
            do {
                Job job = new Job(
                        cursorJobs.getInt(cursorJobs.getColumnIndex("id")),
                        null,
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.HOURS)),
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.MONEY)),
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.COMPLETED)),
                        cursorJobs.getString(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.COMPLETED)));
                job.setEmployer(getEmployerFromDb(cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.EMPLOYER_ID))));
                result.add(job);
            } while (cursorJobs.moveToNext());
        }
        return result;


    }

    public WorkDay getWorkDayByDate(String date) {
        WorkDay result = null;
        List<Job> resultJobs = new ArrayList<>();
        String selection = JobsDbShema.WorkDaysTable.Cols.DAY + " =?";
        String[] selectionArgs = new String[]{String.valueOf(date)};
        Cursor cursorWorkDay = dbRead.query(
                JobsDbShema.WorkDaysTable.NAME,
                null, selection, selectionArgs,
                null, null, null
        );
        if (cursorWorkDay.moveToFirst()) {
            result = new WorkDay(
                    cursorWorkDay.getInt(cursorWorkDay.getColumnIndex("id")),
                    cursorWorkDay.getString(cursorWorkDay.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.DAY)),
                    resultJobs,
                    cursorWorkDay.getInt(cursorWorkDay.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.HOURS)),
                    cursorWorkDay.getInt(cursorWorkDay.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.MONEY)));
            cursorWorkDay.close();
            String selectionJobs = JobsDbShema.JobsTable.Cols.WORKDAY_ID + " =?";
            String[] selectionArgsJobs = new String[]{String.valueOf(result.getId())};
            Cursor cursorJobs = dbRead.query(
                    JobsDbShema.JobsTable.NAME,
                    null, selectionJobs, selectionArgsJobs,
                    null, null, null
            );
            if (cursorJobs.moveToFirst()) {
                do {
                    Employer employer = null;
                    Job job = new Job(
                            cursorJobs.getInt(cursorJobs.getColumnIndex("id")),
                            employer,
                            cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.HOURS)),
                            cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.MONEY)),
                            cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.COMPLETED)),
                            cursorJobs.getString(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.DESC))
                    );
                    int employerId = cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.EMPLOYER_ID));
                    //String selectionEmployer = " =?";
                    String selectionEmployer = "id =?";
                    String[] selectionArgsEmployer = new String[]{String.valueOf(employerId)};
                    Cursor cursorEmployer = dbRead.query(
                            JobsDbShema.EmployersTable.NAME,
                            null, selectionEmployer, selectionArgsEmployer,
                            null, null, null
                    );
                    cursorEmployer.moveToFirst();

                    employer = new Employer(
                            cursorEmployer.getInt(cursorEmployer.getColumnIndex("id")),
                            cursorEmployer.getString(cursorEmployer.getColumnIndex(JobsDbShema.EmployersTable.Cols.NAME)),
                            cursorEmployer.getString(cursorEmployer.getColumnIndex(JobsDbShema.EmployersTable.Cols.DESC)));
                    cursorEmployer.close();
                    job.setEmployer(employer);
                    resultJobs.add(job);
                } while (cursorJobs.moveToNext());
            }
            cursorJobs.close();
            result.setJobs(resultJobs);
        }


        return result;
    }

    public int findWorkadayIdByDate(String date) {
        int result = -1;
        String selection = JobsDbShema.WorkDaysTable.Cols.DAY + " =?";
        String[] selectionArgs = new String[]{String.valueOf(date)};
        Cursor cursor = dbRead.query(
                JobsDbShema.WorkDaysTable.NAME,
                null, selection, selectionArgs,
                null, null, null
        );
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex("id"));
        }
        return result;

    }

    public void updateWorkDayToDb(WorkDay workDay) {

        ContentValues value = new ContentValues();
        value.put(JobsDbShema.WorkDaysTable.Cols.MONEY, workDay.getMoney());
        value.put(JobsDbShema.WorkDaysTable.Cols.HOURS, workDay.getHours());
        dbWrite.update(JobsDbShema.WorkDaysTable.NAME, value, "id =?",
                new String[]{String.valueOf(workDay.getId())});
        for (Job job : workDay.getJobs()) {
            value = new ContentValues();
            value.put(JobsDbShema.JobsTable.Cols.MONEY, workDay.getMoney());
            value.put(JobsDbShema.WorkDaysTable.Cols.HOURS, workDay.getHours());
            dbWrite.update(JobsDbShema.JobsTable.NAME, value, "id =?",
                    new String[]{String.valueOf(workDay.getId())});
        }


    }

    public void deleteJobfromDb(int id) {
        dbWrite.delete(JobsDbShema.JobsTable.NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteWorkDayFromDb(WorkDay workDay) {
        for (Job job : workDay.getJobs()) {
            dbWrite.delete(JobsDbShema.JobsTable.NAME, "id = ?", new String[]{String.valueOf(job.getId())});
        }
        dbWrite.delete(JobsDbShema.WorkDaysTable.NAME, "id = ?", new String[]{String.valueOf(workDay.getId())});


    }

    public Job getJobFromDb(int jobId) {
        Job result = null;
        String selectionJobs = "id =?";
        String[] selectionArgsJobs = new String[]{String.valueOf(jobId)};
        Cursor cursorJobs = dbRead.query(
                JobsDbShema.JobsTable.NAME,
                null, selectionJobs, selectionArgsJobs,
                null, null, null
        );
        cursorJobs.moveToFirst();
        Employer employer = null;
        result = new Job(
                cursorJobs.getInt(cursorJobs.getColumnIndex("id")),
                employer,
                cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.HOURS)),
                cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.MONEY)),
                cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.COMPLETED)),
                cursorJobs.getString(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.DESC))
        );
        int employerId = cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.EMPLOYER_ID));
        String selectionEmployer = "id =?";
        String[] selectionArgsEmployer = new String[]{String.valueOf(employerId)};
        Cursor cursorEmployer = dbRead.query(
                JobsDbShema.EmployersTable.NAME,
                null, selectionEmployer, selectionArgsEmployer,
                null, null, null
        );
        cursorEmployer.moveToFirst();
        employer = new Employer(
                cursorEmployer.getInt(cursorEmployer.getColumnIndex("id")),
                cursorEmployer.getString(cursorEmployer.getColumnIndex(JobsDbShema.EmployersTable.Cols.NAME)),
                cursorEmployer.getString(cursorEmployer.getColumnIndex(JobsDbShema.EmployersTable.Cols.DESC)));
        cursorEmployer.close();
        cursorJobs.close();
        result.setEmployer(employer);


        return result;
    }

    public void updateJobToDb(Job job) {
        ContentValues value = new ContentValues();
        value.put(JobsDbShema.JobsTable.Cols.COMPLETED, job.getCompleted());
        value.put(JobsDbShema.JobsTable.Cols.MONEY, job.getMoney());
        value.put(JobsDbShema.JobsTable.Cols.DESC, job.getDesc());
        value.put(JobsDbShema.JobsTable.Cols.HOURS, job.getHour());
        value.put(JobsDbShema.JobsTable.Cols.EMPLOYER_ID, job.getEmployer().getId());
        dbWrite.update(JobsDbShema.JobsTable.NAME, value, "id =?",
                new String[]{String.valueOf(job.getId())});

    }

    public void deleteEmployerFromDb(int id) {
        dbWrite.delete(JobsDbShema.EmployersTable.NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Job> getJobsFromDbByEmployer(int id) {
        List<Job> result = new ArrayList<>();
        String selectionJobs = JobsDbShema.JobsTable.Cols.EMPLOYER_ID + " =?";
        String[] selectionArgsQuestion = new String[]{String.valueOf(id)};
        Cursor cursorJobs = dbRead.query(
                JobsDbShema.JobsTable.NAME,
                null, selectionJobs, selectionArgsQuestion,
                null, null, null
        );
        if (cursorJobs.moveToFirst()) {
            do {
                Job job = new Job(
                        cursorJobs.getInt(cursorJobs.getColumnIndex("id")),
                        null,
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.HOURS)),
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.MONEY)),
                        cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.COMPLETED)),
                        cursorJobs.getString(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.COMPLETED)));
                job.setEmployer(getEmployerFromDb(cursorJobs.getInt(cursorJobs.getColumnIndex(JobsDbShema.JobsTable.Cols.EMPLOYER_ID))));
                result.add(job);
            } while (cursorJobs.moveToNext());
        }
        return result;
    }

    public List<WorkDay> getWorkDaysByEmployer(int id) {
        List<WorkDay> result = new ArrayList<>();
        List<WorkDay> input = getAllWorkDaysFromDb();
        for (WorkDay workDay : input) {
            List<Job> jobs = new ArrayList<>();
            for (Job job : workDay.getJobs()) {
                if (job.getEmployer().getId() == id) {
                    jobs.add(job);
                }
            }
            if (jobs.size() > 0) {
                WorkDay workDay1 = new WorkDay(workDay.getId(), workDay.getDay(), jobs, 0, 0);
                workDay1.recountJobs();
                result.add(workDay1);
            }
        }


        return result;

    }

    public List<WorkDay> getWorkDaysByEmployer(int id, String data1, String data2) {
        List<WorkDay> result = new ArrayList<>();
        String selectionWorkDay = JobsDbShema.WorkDaysTable.Cols.DAY + " >=? AND " + JobsDbShema.WorkDaysTable.Cols.DAY + " <=?";
        String[] selectionArgsWorkDay = new String[]{data1, data2};
        Cursor cursor = dbRead.query(
                JobsDbShema.WorkDaysTable.NAME,
                null, selectionWorkDay, selectionArgsWorkDay,
                null, null, JobsDbShema.WorkDaysTable.Cols.DAY
        );
        if (cursor.moveToFirst()) {
            do {
                WorkDay workDay = new WorkDay(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.DAY)),
                        new ArrayList<Job>(),
                        cursor.getInt(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.HOURS)),
                        cursor.getInt(cursor.getColumnIndex(JobsDbShema.WorkDaysTable.Cols.MONEY))
                );
                List<Job> input = getJobsFromDb(workDay.getId());
                List<Job> jobsWithEmployer = new ArrayList<>();
                for (Job job : input) {
                    if (job.getEmployer().getId() == id) {
                        jobsWithEmployer.add(job);
                    }
                }
                if (jobsWithEmployer.size() > 0) {
                    workDay.setJobs(jobsWithEmployer);
                    workDay.recountJobs();
                    result.add(workDay);
                }


            } while (cursor.moveToNext());
        }
        return result;


    }

    public WorkDay getWorkDayByEmployer(int workDayId, int employerId) {
        WorkDay result = getWorkDayById(workDayId);
        result.filterByEmployer(employerId);
        return result;




    }
}
