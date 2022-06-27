package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

/**
 * Created by Vegeto52-PC on 09/06/2022.
 */
public class ProjectDataRepository {

    private final ProjectDao mProjectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        mProjectDao = projectDao;
    }

    // --- GET PROJECT ---
    public LiveData<Project> getProject(long projectId) {
        return this.mProjectDao.getProject(projectId);
    }
}
