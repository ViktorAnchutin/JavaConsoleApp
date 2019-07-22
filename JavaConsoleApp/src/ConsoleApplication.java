import dao.ProjectDao;
import dao.TaskDao;
import dao.UserDao;


import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ConsoleApplication {



    public static void main(String ags[]) {

        ConsoleTask consoleTask = new ConsoleTask(new BufferedReader(new InputStreamReader(System.in)));
        consoleTask.setProjectDao(new ProjectDao());
        consoleTask.setUserDao(new UserDao());
        consoleTask.setTaskDao(new TaskDao());

        consoleTask.run();
    }


}
