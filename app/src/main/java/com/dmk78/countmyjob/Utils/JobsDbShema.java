package com.dmk78.countmyjob.Utils;

public class JobsDbShema {
    public static final class WorkDaysTable {
        public static final String NAME = "work_days";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                WorkDaysTable.NAME + " (id INTEGER PRIMARY KEY , " +
                Cols.DAY + " TEXT UNIQUE , " +
                Cols.HOURS + " INTEGER, " +
                Cols.MONEY + " INTEGER" +
                ")";

        public static final class Cols {
            public static final String DAY = "day";
            public static final String HOURS = "hours";
            public static final String MONEY = "money";

        }
    }

    public static final class JobsTable {
        public static final String NAME = "jobs";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                JobsTable.NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.WORKDAY_ID + " INTEGER, " +
                Cols.EMPLOYER_ID + " INTEGER, " +
                Cols.HOURS + " INTEGER, " +
                Cols.MONEY + " INTEGER," +
                Cols.COMPLETED + " INTEGER, " +
                Cols.DESC + " TEXT " +
                ")";

        public static final class Cols {
            public static final String WORKDAY_ID = "workday_id";
            public static final String EMPLOYER_ID = "employer_id";
            public static final String HOURS = "hours";
            public static final String MONEY = "money";
            public static final String COMPLETED = "completed";
            public static final String DESC = "desc";

        }
    }

    public static final class EmployersTable {
        public static final String NAME = "employers";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                EmployersTable.NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Cols.NAME + " TEXT, " +
                Cols.DESC + " TEXT" +
                ")";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String DESC = "desc";


        }
    }
}

