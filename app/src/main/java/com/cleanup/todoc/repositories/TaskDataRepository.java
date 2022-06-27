package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

/**
 * Created by Vegeto52-PC on 09/06/2022.
 */
public class TaskDataRepository {

    private final TaskDao mTaskDao;

    public TaskDataRepository(TaskDao taskDao) {
        mTaskDao = taskDao;
    }

    // --- GET ---
    public LiveData<List<Task>> getTasksByProject(long projectId){
        return this.mTaskDao.getTaskByProject(projectId);
    }
    public LiveData<List<Task>> getTasks() {
        return this.mTaskDao.getTasks();
    }

    // --- CREATE ---
    public void createTask(Task task){
        mTaskDao.insertTask(task);
    }

    // --- DELETE ---
    public void deleteTask(long taskId){
        mTaskDao.deleteTask(taskId);
    }

    // --- UPDATE ---
    public void updateTask(Task task){
        mTaskDao.updateTask(task);
    }
}
