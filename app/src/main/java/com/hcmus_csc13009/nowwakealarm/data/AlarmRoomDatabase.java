package com.hcmus_csc13009.nowwakealarm.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hcmus_csc13009.nowwakealarm.models.Alarm;
import com.hcmus_csc13009.nowwakealarm.utils.AlarmUtils;
import com.hcmus_csc13009.nowwakealarm.utils.WeekDays;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Alarm.class}, version = 1, exportSchema = false)
public abstract class AlarmRoomDatabase extends RoomDatabase {
    static public AlarmRoomDatabase INSTANCE = null;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract AlarmDao alarmDao();

    final private static Callback sOnOpenCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                final AlarmDao alarmDao = INSTANCE.alarmDao();
                if (alarmDao.getLastAlarm().length < 1) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 12);
                    calendar.set(Calendar.MINUTE, 30);
                    for (int i = 0; i < 5; ++i) {
                        Alarm alarm = new Alarm(calendar.getTimeInMillis(), String.format("My Alarm %02d", i),
                                "Nothing to show", "?", i % 2 == 0,
                                false, false, true, AlarmUtils.getBitFormat(WeekDays.FRI, WeekDays.MON, WeekDays.THU));
                        alarmDao.insert(alarm);
                    }
                }
            });
        }
    };


    public static AlarmRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AlarmRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AlarmRoomDatabase.class, "alarm_database")
                            .addCallback(sOnOpenCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}