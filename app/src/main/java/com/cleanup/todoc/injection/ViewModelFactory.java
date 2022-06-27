package com.cleanup.todoc.injection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.database.CleanupDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.todolist.TaskViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Vegeto52-PC on 10/06/2022.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final TaskDataRepository taskDataSource;

    private final ProjectDataRepository projectDataSource;

    private final Executor executor;

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(Context context) {

        if (factory == null) {

            synchronized (ViewModelFactory.class) {

                if (factory == null) {

                    factory = new ViewModelFactory(context);

                }

            }

        }

        return factory;

    }

    private ViewModelFactory(Context context) {

        CleanupDatabase database = CleanupDatabase.getInstance(context);

        this.taskDataSource = new TaskDataRepository(database.mTaskDao());

        this.projectDataSource = new ProjectDataRepository(database.mProjectDao());

        this.executor = Executors.newSingleThreadExecutor();

    }

    @NonNull
    @Override
    public <T extends ViewModel>  T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(TaskViewModel.class)) {

            return (T) new TaskViewModel(taskDataSource, projectDataSource, executor);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
