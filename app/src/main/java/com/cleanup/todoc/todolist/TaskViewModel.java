package com.cleanup.todoc.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Vegeto52-PC on 09/06/2022.
 */
public class TaskViewModel  extends ViewModel {

    // REPOSITORIES
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor mExecutor;

    // DATA
    @Nullable
    private LiveData<Project> currentProject;

    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor mExecutor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.mExecutor = mExecutor;
    }

    public void init(long projectId) {
        if (this.currentProject != null) {
            return;
        }
        currentProject = projectDataSource.getProject(projectId);
    }

    // --- FOR PROJECT ---
    public LiveData<Project> getProject() {
        return this.currentProject;
    }

    // --- FOR TASK ---
    public LiveData<List<Task>> getTasksByProject(long projectId) {
        return taskDataSource.getTasksByProject(projectId);
    }
    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    public void createTask(long id, long projectId, @NonNull String name, long creationTimestamp) {
        mExecutor.execute(() -> {
            taskDataSource.createTask(new Task(id, projectId, name, creationTimestamp));
        });
    }

    public void deleteTask(long taskId) {
        mExecutor.execute(() -> taskDataSource.deleteTask(taskId));
    }

    public void updateTask(Task task) {
        mExecutor.execute(() -> taskDataSource.updateTask(task));
    }
}
