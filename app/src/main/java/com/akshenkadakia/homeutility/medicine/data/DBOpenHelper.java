package com.akshenkadakia.homeutility.medicine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {
    //Singleton Application
    private static DBOpenHelper memberTable;

    public static synchronized DBOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (memberTable == null) {
            memberTable = new DBOpenHelper(context.getApplicationContext());
        }
        return memberTable;
    }

    //private for sync
    private DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Database Info
    private static final String DATABASE_NAME = "medicineDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_FAMILY = "family";
    private static final String TABLE_MEDICINES = "medicines";
    private static final String TABLE_ALARMS = "alarms";

    // family Table Columns
    private static final String FAMILY_ID = "fid";
    private static final String FAMILY_NAME = "name";
    private static final String FAMILY_NOTIFICATIONS = "notification";
    private static final String FAMILY_AI = "ai";

    //medicines Table Columns
    private static final String MEDICINES_ID = "mid";
    private static final String MEDICINES_NAME = "name";
    private static final String MEDICINES_PACKETS = "packets";
    private static final String MEDICINES_TABLETS = "tablets";
    private static final String MEDICINES_LEFT = "left";
    private static final String MEDICINES_TOTAL = "total";
    private static final String MEDICINES_COLOR = "color";
    private static final String MEDICINES_SHAPE = "shape";
    private static final String MEDICINES_AI = "ai";

    //alarms Table columns
    private static final String ALARMS_ID = "aid";
    private static final String ALARMS_SUNDAY = "sunday";
    private static final String ALARMS_MONDAY = "monday";
    private static final String ALARMS_TUESDAY = "tueday";
    private static final String ALARMS_WEDNESDAY = "wednesday";
    private static final String ALARMS_THURSDAY = "thursday";
    private static final String ALARMS_FRIDAY = "friday";
    private static final String ALARMS_SATURDAY = "saturday";
    private static final String ALARMS_INTENT_ID = "iid";
    private static final String ALARMS_TIME = "time";

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAMILY_TABLE = "CREATE TABLE " + TABLE_FAMILY +
                "(" +
                FAMILY_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                FAMILY_NAME + " TEXT " + "," +
                FAMILY_NOTIFICATIONS + " INTEGER," +
                FAMILY_AI + " INTEGER " +
                ")";
        db.execSQL(CREATE_FAMILY_TABLE);

        String CREATE_MEDICINES_TABLE = "CREATE TABLE " + TABLE_MEDICINES +
                "(" +
                FAMILY_ID + " INTEGER," +
                MEDICINES_ID + " INTEGER ," +// Define a primary key
                MEDICINES_NAME + " TEXT " + "," +
                MEDICINES_PACKETS + " INTEGER," +
                MEDICINES_TABLETS + " INTEGER," +
                MEDICINES_LEFT + " INTEGER," +
                MEDICINES_TOTAL + " INTEGER," +
                MEDICINES_COLOR + " TEXT," +
                MEDICINES_SHAPE + " TEXT," +
                MEDICINES_AI + " INTEGER, " +
                "FOREIGN KEY(" + FAMILY_ID + ") REFERENCES " + TABLE_FAMILY + "(" + FAMILY_ID + ")" +
                ")";
        db.execSQL(CREATE_MEDICINES_TABLE);

        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS +
                "(" +
                FAMILY_ID + " INTEGER," +
                MEDICINES_ID + " INTEGER," +
                ALARMS_ID + " INTEGER " + "," +
                ALARMS_SUNDAY + " INTEGER," +
                ALARMS_MONDAY + " INTEGER," +
                ALARMS_TUESDAY + " INTEGER," +
                ALARMS_WEDNESDAY + " INTEGER," +
                ALARMS_THURSDAY + " INTEGER," +
                ALARMS_FRIDAY + " INTEGER," +
                ALARMS_SATURDAY + " INTEGER," +
                ALARMS_INTENT_ID + " INTEGER," +
                ALARMS_TIME + " TEXT," +
                "FOREIGN KEY(" + FAMILY_ID + ") REFERENCES " + TABLE_FAMILY + "(" + FAMILY_ID + ")" +
                "FOREIGN KEY(" + MEDICINES_ID + ") REFERENCES " + TABLE_MEDICINES + "(" + MEDICINES_ID + ")" +
                ")";
        db.execSQL(CREATE_ALARMS_TABLE);

    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
            switch (oldVersion) {

            }
            //onCreate(db);
        }
    }

    //add new member

    public long addNewMember(FamilyData familyData) {
        SQLiteDatabase db = getWritableDatabase();
        long data = -1l;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(FAMILY_NAME, familyData.getName());
            if (familyData.isNotifications())
                values.put(FAMILY_NOTIFICATIONS, 1);
            else
                values.put(FAMILY_NOTIFICATIONS, 0);
            values.put(FAMILY_AI, -1);
            data = db.insertOrThrow(TABLE_FAMILY, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("addNewMember", "" + e);
            data = -1l;
        } finally {
            db.endTransaction();
            return data;
        }
    }

    // get all members
    public ArrayList<FamilyData> getAllMembers() {
        ArrayList<FamilyData> fd = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FAMILY+" order by "+FAMILY_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    FamilyData memberTable = new FamilyData();
                    memberTable.setFid(cursor.getInt(cursor.getColumnIndex(FAMILY_ID)));
                    memberTable.setName(cursor.getString(cursor.getColumnIndex(FAMILY_NAME)));
                    int i = cursor.getInt(cursor.getColumnIndex(FAMILY_NOTIFICATIONS));
                    if (i == 1)
                        memberTable.setNotifications(true);
                    else
                        memberTable.setNotifications(false);
                    memberTable.setAi(cursor.getInt(cursor.getColumnIndex(FAMILY_AI)));
                    fd.add(memberTable);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getAllMembers", "" + e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return fd;
    }

    // single members
    public FamilyData getMember(int fid) {
        String selectQuery = "SELECT * FROM " + TABLE_FAMILY + " where " + FAMILY_ID + "=" + fid;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        FamilyData memberTable = new FamilyData();
        try {
            if (cursor.moveToFirst()) {

                memberTable.setFid(cursor.getInt(cursor.getColumnIndex(FAMILY_ID)));
                memberTable.setName(cursor.getString(cursor.getColumnIndex(FAMILY_NAME)));
                int i = cursor.getInt(cursor.getColumnIndex(FAMILY_NOTIFICATIONS));
                if (i == 1)
                    memberTable.setNotifications(true);
                else
                    memberTable.setNotifications(false);
                memberTable.setAi(cursor.getInt(cursor.getColumnIndex(FAMILY_AI)));
            }
        } catch (Exception e) {
            Log.e("getMember", "" + e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return memberTable;
    }

    //update
    public int updateMember(FamilyData memberTable) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAMILY_NAME, memberTable.getName());
        if (memberTable.isNotifications())
            values.put(FAMILY_NOTIFICATIONS, 1);
        else
            values.put(FAMILY_NOTIFICATIONS, 0);
        values.put(FAMILY_AI, memberTable.getAi());
        return db.update(TABLE_FAMILY, values, FAMILY_ID + " = ?", new String[]{String.valueOf(memberTable.getFid())});
    }

    //delete single member
    public int deleteMember(FamilyData memberTable) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = -1;
        db.beginTransaction();
        try {
            db.delete(TABLE_ALARMS, FAMILY_ID + " =?", new String[]{String.valueOf(memberTable.getFid())});
            db.delete(TABLE_MEDICINES, FAMILY_ID + " =?", new String[]{String.valueOf(memberTable.getFid())});
            rows = db.delete(TABLE_FAMILY, FAMILY_ID + " =?", new String[]{String.valueOf(memberTable.getFid())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            rows = -1;
            Log.e("deleteMember", "" + e);
        } finally {
            db.endTransaction();
        }
        return rows;
    }


    //add medicine
    public long addNewMedicine(MedicineData medicineData, int fid) {
        SQLiteDatabase db = getWritableDatabase();
        long data = -1l;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(FAMILY_ID, fid);
            values.put(MEDICINES_ID, medicineData.getMid());
            values.put(MEDICINES_NAME, medicineData.getName());
            values.put(MEDICINES_PACKETS, medicineData.getPackets());
            values.put(MEDICINES_TABLETS, medicineData.getTablets());
            values.put(MEDICINES_LEFT, medicineData.getLeft());
            values.put(MEDICINES_TOTAL, medicineData.getTotal());
            values.put(MEDICINES_COLOR, medicineData.getColor());
            values.put(MEDICINES_SHAPE, medicineData.getShape());
            values.put(MEDICINES_AI, medicineData.getAi());
            data = db.insertOrThrow(TABLE_MEDICINES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("addNewMedicine", "" + e);
            data = -1l;
        } finally {
            db.endTransaction();
            return data;
        }
    }

    //get all medicines
    public ArrayList<MedicineData> getAllMedicines(int fid) {
        ArrayList<MedicineData> fd = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MEDICINES + " where fid=" + fid +" order by "+MEDICINES_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    MedicineData memberTable = new MedicineData(cursor.getInt(cursor.getColumnIndex(FAMILY_ID)),
                            cursor.getInt(cursor.getColumnIndex(MEDICINES_ID)),
                            cursor.getInt(cursor.getColumnIndex(MEDICINES_AI)),
                            cursor.getString(cursor.getColumnIndex(MEDICINES_NAME)),
                            cursor.getInt(cursor.getColumnIndex(MEDICINES_PACKETS)),
                            cursor.getInt(cursor.getColumnIndex(MEDICINES_TABLETS)),
                            cursor.getInt(cursor.getColumnIndex(MEDICINES_LEFT)),
                            cursor.getInt(cursor.getColumnIndex(MEDICINES_TOTAL)),
                            cursor.getString(cursor.getColumnIndex(MEDICINES_COLOR)),
                            cursor.getString(cursor.getColumnIndex(MEDICINES_SHAPE)));
                    fd.add(memberTable);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getAllMedicines", "" + e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return fd;
    }

    //get single medicine
    public MedicineData getMedicine(int fid, int mid) {
        String selectQuery = "SELECT * FROM " + TABLE_MEDICINES + " where fid=" + fid + " and mid=" + mid;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        MedicineData memberTable = null;
        try {
            if (cursor.moveToFirst()) {
                memberTable = new MedicineData(cursor.getInt(cursor.getColumnIndex(FAMILY_ID)),
                        cursor.getInt(cursor.getColumnIndex(MEDICINES_ID)),
                        cursor.getInt(cursor.getColumnIndex(MEDICINES_AI)),
                        cursor.getString(cursor.getColumnIndex(MEDICINES_NAME)),
                        cursor.getInt(cursor.getColumnIndex(MEDICINES_PACKETS)),
                        cursor.getInt(cursor.getColumnIndex(MEDICINES_TABLETS)),
                        cursor.getInt(cursor.getColumnIndex(MEDICINES_LEFT)),
                        cursor.getInt(cursor.getColumnIndex(MEDICINES_TOTAL)),
                        cursor.getString(cursor.getColumnIndex(MEDICINES_COLOR)),
                        cursor.getString(cursor.getColumnIndex(MEDICINES_SHAPE)));
            }
        } catch (Exception e) {
            Log.e("getMedicine", "" + e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return memberTable;
    }

    //delete medicine
    public int deleteMedicine(int fid, int mid) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = -1;
        db.beginTransaction();
        try {
            db.delete(TABLE_ALARMS, MEDICINES_ID + " =? and " + FAMILY_ID + " =?", new String[]{String.valueOf(mid), String.valueOf(fid)});
            rows = db.delete(TABLE_MEDICINES, MEDICINES_ID + " =? and " + FAMILY_ID + " =?", new String[]{String.valueOf(mid), String.valueOf(fid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            rows = -1;
            Log.e("deleteMedicine", "" + e);
        } finally {
            db.endTransaction();
        }
        return rows;
    }


    //update medicine
    public int updateMedicine(MedicineData medicineData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEDICINES_NAME, medicineData.getName());
        values.put(MEDICINES_PACKETS, medicineData.getPackets());
        values.put(MEDICINES_TABLETS, medicineData.getTablets());
        values.put(MEDICINES_LEFT, medicineData.getLeft());
        values.put(MEDICINES_TOTAL, medicineData.getTotal());
        values.put(MEDICINES_COLOR, medicineData.getColor());
        values.put(MEDICINES_SHAPE, medicineData.getShape());
        values.put(MEDICINES_AI, medicineData.getAi());
        return db.update(TABLE_MEDICINES, values, FAMILY_ID + " = ? and " + MEDICINES_ID + "= ? ", new String[]{String.valueOf(medicineData.getFid()), String.valueOf(medicineData.getMid())});
    }

    //add new alarm
    public long addNewAlarm(AlarmData alarmData) {
        SQLiteDatabase db = getWritableDatabase();
        long data = -1l;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(FAMILY_ID, alarmData.getFid());
            values.put(MEDICINES_ID, alarmData.getMid());
            values.put(ALARMS_ID, alarmData.getAid());
            values.put(ALARMS_INTENT_ID, alarmData.getIid());

            if (alarmData.isSunday())
                values.put(ALARMS_SUNDAY, 1);
            else
                values.put(ALARMS_SUNDAY, 0);

            if (alarmData.isMonday())
                values.put(ALARMS_MONDAY, 1);
            else
                values.put(ALARMS_MONDAY, 0);

            if (alarmData.isTuesday())
                values.put(ALARMS_TUESDAY, 1);
            else
                values.put(ALARMS_TUESDAY, 0);

            if (alarmData.isWednesday())
                values.put(ALARMS_WEDNESDAY, 1);
            else
                values.put(ALARMS_WEDNESDAY, 0);

            if (alarmData.isThursday())
                values.put(ALARMS_THURSDAY, 1);
            else
                values.put(ALARMS_THURSDAY, 0);

            if (alarmData.isFriday())
                values.put(ALARMS_FRIDAY, 1);
            else
                values.put(ALARMS_FRIDAY, 0);

            if (alarmData.isSaturday())
                values.put(ALARMS_SATURDAY, 1);
            else
                values.put(ALARMS_SATURDAY, 0);

            values.put(ALARMS_TIME, alarmData.getTime().getTime());
            data = db.insertOrThrow(TABLE_ALARMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("addNewAlarm", "" + e);
            data = -1l;
        } finally {
            db.endTransaction();
            return data;
        }
    }

    //get all alarms
    public SortedList<AlarmData> getAllAlarms(MedicineData medicineData) {
        SortedList<AlarmData> fd = new SortedList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ALARMS + " where " + FAMILY_ID + "=" + medicineData.getFid() + " and " + MEDICINES_ID
                + "=" + medicineData.getMid()+" order by "+ALARMS_TIME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    AlarmData alarmData = new AlarmData();
                    alarmData.setFid(cursor.getInt(cursor.getColumnIndex(FAMILY_ID)));
                    alarmData.setMid(cursor.getInt(cursor.getColumnIndex(MEDICINES_ID)));
                    alarmData.setAid(cursor.getInt(cursor.getColumnIndex(ALARMS_ID)));
                    alarmData.setIid(cursor.getInt(cursor.getColumnIndex(ALARMS_INTENT_ID)));

                    alarmData.setTime(new Time(Long.parseLong(cursor.getString(cursor.getColumnIndex(ALARMS_TIME)))));

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_SUNDAY)) == 1)
                        alarmData.setSunday(true);
                    else
                        alarmData.setSunday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_MONDAY)) == 1)
                        alarmData.setMonday(true);
                    else
                        alarmData.setMonday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_TUESDAY)) == 1)
                        alarmData.setTuesday(true);
                    else
                        alarmData.setTuesday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_WEDNESDAY)) == 1)
                        alarmData.setWednesday(true);
                    else
                        alarmData.setWednesday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_THURSDAY)) == 1)
                        alarmData.setThursday(true);
                    else
                        alarmData.setThursday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_FRIDAY)) == 1)
                        alarmData.setFriday(true);
                    else
                        alarmData.setFriday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_SATURDAY)) == 1)
                        alarmData.setSaturday(true);
                    else
                        alarmData.setSaturday(false);

                    fd.add(alarmData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getAllAlarms", "" + e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return fd;
    }

    //get single alarm
    public AlarmData getAlarm(int fid, int mid, int aid) {
        String selectQuery = "SELECT * FROM " + TABLE_ALARMS + " where fid=" + fid + " and mid=" + mid + " and aid=" + aid;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        AlarmData alarmData = null;
        try {
            if (cursor.moveToFirst()) {
                alarmData = new AlarmData();
                alarmData.setFid(cursor.getInt(cursor.getColumnIndex(FAMILY_ID)));
                alarmData.setMid(cursor.getInt(cursor.getColumnIndex(MEDICINES_ID)));
                alarmData.setAid(cursor.getInt(cursor.getColumnIndex(ALARMS_ID)));
                alarmData.setIid(cursor.getInt(cursor.getColumnIndex(ALARMS_INTENT_ID)));

                alarmData.setTime(new Time(Long.parseLong(cursor.getString(cursor.getColumnIndex(ALARMS_TIME)))));

                if (cursor.getInt(cursor.getColumnIndex(ALARMS_SUNDAY)) == 1)
                    alarmData.setSunday(true);
                else
                    alarmData.setSunday(false);

                if (cursor.getInt(cursor.getColumnIndex(ALARMS_MONDAY)) == 1)
                    alarmData.setMonday(true);
                else
                    alarmData.setMonday(false);

                if (cursor.getInt(cursor.getColumnIndex(ALARMS_TUESDAY)) == 1)
                    alarmData.setTuesday(true);
                else
                    alarmData.setTuesday(false);

                if (cursor.getInt(cursor.getColumnIndex(ALARMS_WEDNESDAY)) == 1)
                    alarmData.setWednesday(true);
                else
                    alarmData.setWednesday(false);

                if (cursor.getInt(cursor.getColumnIndex(ALARMS_THURSDAY)) == 1)
                    alarmData.setThursday(true);
                else
                    alarmData.setThursday(false);

                if (cursor.getInt(cursor.getColumnIndex(ALARMS_FRIDAY)) == 1)
                    alarmData.setFriday(true);
                else
                    alarmData.setFriday(false);

                if (cursor.getInt(cursor.getColumnIndex(ALARMS_SATURDAY)) == 1)
                    alarmData.setSaturday(true);
                else
                    alarmData.setSaturday(false);

            }
        } catch (Exception e) {
            Log.e("getAlarm", "" + e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return alarmData;
    }

    //delete alarm
    public int deleteAlarm(int fid, int mid, int aid) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = -1;
        db.beginTransaction();
        try {
            rows = db.delete(TABLE_ALARMS, MEDICINES_ID + " =? and " + FAMILY_ID + " =? and " + ALARMS_ID + " =?", new String[]{String.valueOf(mid), String.valueOf(fid), String.valueOf(aid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            rows = -1;
            Log.e("deleteMedicine", "" + e);
        } finally {
            db.endTransaction();
        }
        return rows;
    }

    //update alarm
    public int updateAlarm(AlarmData alarmData) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        if (alarmData.isSunday())
            values.put(ALARMS_SUNDAY, 1);
        else
            values.put(ALARMS_SUNDAY, 0);

        if (alarmData.isMonday())
            values.put(ALARMS_MONDAY, 1);
        else
            values.put(ALARMS_MONDAY, 0);

        if (alarmData.isTuesday())
            values.put(ALARMS_TUESDAY, 1);
        else
            values.put(ALARMS_TUESDAY, 0);

        if (alarmData.isWednesday())
            values.put(ALARMS_WEDNESDAY, 1);
        else
            values.put(ALARMS_WEDNESDAY, 0);

        if (alarmData.isThursday())
            values.put(ALARMS_THURSDAY, 1);
        else
            values.put(ALARMS_THURSDAY, 0);

        if (alarmData.isFriday())
            values.put(ALARMS_FRIDAY, 1);
        else
            values.put(ALARMS_FRIDAY, 0);

        if (alarmData.isSaturday())
            values.put(ALARMS_SATURDAY, 1);
        else
            values.put(ALARMS_SATURDAY, 0);

        values.put(ALARMS_TIME, alarmData.getTime().getTime());

        return db.update(TABLE_ALARMS, values, FAMILY_ID + " = ? and " + MEDICINES_ID + "= ? and " + ALARMS_ID + " =?", new String[]{String.valueOf(alarmData.getFid()), String.valueOf(alarmData.getMid()), String.valueOf(alarmData.getAid())});
    }

    //get day data
    public ArrayList<DayData> getDayData(String day) {
        ArrayList<DayData> dayDatas = new ArrayList<>();
        dayDatas.add(new DayData());
        String x = null;
        switch (day) {
            case "monday":
                x = ALARMS_MONDAY;
                break;
            case "tuesday":
                x = ALARMS_TUESDAY;
                break;
            case "wednesday":
                x = ALARMS_WEDNESDAY;
                break;
            case "thursday":
                x = ALARMS_THURSDAY;
                break;
            case "friday":
                x = ALARMS_FRIDAY;
                break;
            case "saturday":
                x = ALARMS_SATURDAY;
                break;
            case "sunday":
                x = ALARMS_SUNDAY;
                break;
        }

        String selectQuery = "SELECT * FROM " + TABLE_ALARMS + " where " + x + "=1 order by "+ALARMS_TIME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    AlarmData alarmData=new AlarmData();

                    alarmData.setFid(cursor.getInt(cursor.getColumnIndex(FAMILY_ID)));
                    alarmData.setMid(cursor.getInt(cursor.getColumnIndex(MEDICINES_ID)));
                    alarmData.setAid(cursor.getInt(cursor.getColumnIndex(ALARMS_ID)));
                    alarmData.setIid(cursor.getInt(cursor.getColumnIndex(ALARMS_INTENT_ID)));

                    alarmData.setTime(new Time(Long.parseLong(cursor.getString(cursor.getColumnIndex(ALARMS_TIME)))));

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_SUNDAY)) == 1)
                        alarmData.setSunday(true);
                    else
                        alarmData.setSunday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_MONDAY)) == 1)
                        alarmData.setMonday(true);
                    else
                        alarmData.setMonday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_TUESDAY)) == 1)
                        alarmData.setTuesday(true);
                    else
                        alarmData.setTuesday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_WEDNESDAY)) == 1)
                        alarmData.setWednesday(true);
                    else
                        alarmData.setWednesday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_THURSDAY)) == 1)
                        alarmData.setThursday(true);
                    else
                        alarmData.setThursday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_FRIDAY)) == 1)
                        alarmData.setFriday(true);
                    else
                        alarmData.setFriday(false);

                    if (cursor.getInt(cursor.getColumnIndex(ALARMS_SATURDAY)) == 1)
                        alarmData.setSaturday(true);
                    else
                        alarmData.setSaturday(false);

                    String member=getMember(alarmData.getFid()).getName();

                    String medicine=getMedicine(alarmData.getFid(),alarmData.getMid()).getName();

                    DayData dayData = new DayData(alarmData,member,medicine);
                    dayDatas.add(dayData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("getDayData", "" + e);
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return dayDatas;
    }

}
