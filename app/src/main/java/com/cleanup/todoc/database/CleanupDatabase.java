package com.cleanup.todoc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.Executors;

/**
 * Created by Vegeto52-PC on 07/06/2022.
 */
@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class CleanupDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile CleanupDatabase INSTANCE;

    // --- DAO ---
    public abstract TaskDao mTaskDao();
    public abstract ProjectDao mProjectDao();

    // --- INSTANCE ---
    public static CleanupDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CleanupDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CleanupDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.mProjectDao().createProject(Project.getProjectById(1L)));
                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.mProjectDao().createProject(Project.getProjectById(2L)));
                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.mProjectDao().createProject(Project.getProjectById(3L)));
            }
        };
    }
}
