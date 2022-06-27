package com.cleanup.todoc;

import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.database.CleanupDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by Vegeto52-PC on 09/06/2022.
 */
@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    // FOR DATA
    private CleanupDatabase mCleanupDatabase;

    // DATA SET FOR TEST
    private static long PROJECT_ID = 1L;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Projet Tartampion", 0xFFEADAD1);

    private static Task NEW_TASK_1 = new Task(1, PROJECT_ID, "Arthur", System.currentTimeMillis());
    private static Task NEW_TASK_2 = new Task(2, PROJECT_ID, "Lancelot", System.currentTimeMillis());
    private static Task NEW_TASK_3 = new Task(3, PROJECT_ID, "Perceval", System.currentTimeMillis());

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.mCleanupDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                CleanupDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        mCleanupDatabase.close();
    }

    @Test
    public void insertAndGetUser() throws InterruptedException {

        // BEFORE : Adding a new project
        this.mCleanupDatabase.mProjectDao().createProject(PROJECT_DEMO);

        // TEST
        Project project = LiveDataTestUtil.getValue(this.mCleanupDatabase.mProjectDao().getProject(PROJECT_ID));

        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) && project.getId() == PROJECT_ID);
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.mCleanupDatabase.mTaskDao().getTaskByProject(PROJECT_ID));
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {

        // BEFORE : Adding demo project & demo tasks
        this.mCleanupDatabase.mProjectDao().createProject(PROJECT_DEMO);
        this.mCleanupDatabase.mTaskDao().insertTask(NEW_TASK_1);
        this.mCleanupDatabase.mTaskDao().insertTask(NEW_TASK_2);
        this.mCleanupDatabase.mTaskDao().insertTask(NEW_TASK_3);

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.mCleanupDatabase.mTaskDao().getTaskByProject(PROJECT_ID));
        assertTrue(tasks.size() == 3);
    }

    @Test
    public void insertAndUpdateTask() throws InterruptedException {

        // BEFORE : Adding demo project & demo tasks. Next, update task added & re-save it
        this.mCleanupDatabase.mProjectDao().createProject(PROJECT_DEMO);
        this.mCleanupDatabase.mTaskDao().insertTask(NEW_TASK_1);
        Task taskAdded = LiveDataTestUtil.getValue(this.mCleanupDatabase.mTaskDao().getTaskByProject(PROJECT_ID)).get(0);
        taskAdded.setName("Merlin");
        this.mCleanupDatabase.mTaskDao().updateTask(taskAdded);

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.mCleanupDatabase.mTaskDao().getTaskByProject(PROJECT_ID));
        assertTrue(tasks.size() == 1 && tasks.get(0).getName().equals("Merlin"));
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {

        // BEFORE : Adding demo project & demo task. Next, get the task added & delete it.
        this.mCleanupDatabase.mProjectDao().createProject(PROJECT_DEMO);
        this.mCleanupDatabase.mTaskDao().insertTask(NEW_TASK_1);
        Task taskAdded = LiveDataTestUtil.getValue(this.mCleanupDatabase.mTaskDao().getTaskByProject(PROJECT_ID)).get(0);
        this.mCleanupDatabase.mTaskDao().deleteTask(taskAdded.getId());

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.mCleanupDatabase.mTaskDao().getTaskByProject(PROJECT_ID));
        assertTrue(tasks.isEmpty());
    }
}
