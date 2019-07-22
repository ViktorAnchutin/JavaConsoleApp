import dao.DbConnector;
import dao.ProjectDao;
import dao.TaskDao;
import dao.UserDao;
import model.Project;
import model.Task;
import model.TaskType;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleTask {

    private BufferedReader reader;
    private UserDao userDao;
    private ProjectDao projectDao;
    private TaskDao taskDao;

    public ConsoleTask(BufferedReader reader)
    {
        this.reader = reader;
    }

    public void setUserDao(UserDao  userDao)
    {
        this.userDao = userDao;
    }

    public void setProjectDao(ProjectDao projectDao)
    {
        this.projectDao = projectDao;
    }

    public void setTaskDao(TaskDao taskDao)
    {
        this.taskDao = taskDao;
    }


    private void showUsers()
    {
        System.out.println("\n Users:");
        List<User> users = userDao.getAll();
        for(User user : users)
        {
            System.out.println(user);
        }
    }

    private void addUser()
    {
        System.out.println("Enter the login: ");
        try {
            String login = reader.readLine();
            userDao.insert(new User(login));
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    private void deleteUser()
    {
        System.out.println("Enter the login: ");
        try{
            String login = reader.readLine();
            if(!userDao.Exists(new User(login)))
            {
                System.out.println("User does not exist. ");
                return;
            }
            User user = userDao.getByLogin(login);
            if(taskDao.hasUser(user))
            {
                System.out.println("User has a task. Deletion is prohibited");
                return;
            }
            userDao.delete(new User(login));
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    private void addProject()
    {
        System.out.println("Enter project name: ");
        try{
            String name = reader.readLine();
            projectDao.insert(new Project(name));
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    private void showProjects()
    {
        System.out.println("\n Projects:");
        List<Project> projects = projectDao.getAll();
        for(Project project : projects)
        {
            System.out.println(project);
        }
    }

    private void deleteProject()
    {
        System.out.println("Enter project name: ");
        try{
            String name = reader.readLine();
            if(!projectDao.Exists(new Project(name)))
            {
                System.out.println("Project does not exist.");
                return;
            }
            Project project = projectDao.getByName(name);
            if(taskDao.hasProject(project))
            {
                System.out.println("Project has a task. Deletion is prohibited");
                return;
            }
            projectDao.delete(project);
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    private void showCommands()
    {
        System.out.println("Commands:");
        System.out.println(" show users / add user / delete user / show projects / add project / delete project / show tasks / add task / delete task / show tasks for user / change db / commands");
    }



    private void addTask() throws IOException
    {
        String stage = "set topic";
        Task task = new Task();
        boolean finished = false;

        while (true)
        {
            switch (stage)
            {
                case "set topic":
                    System.out.println("Enter a unique task topic: ");
                    String topic = reader.readLine();
                    if(taskDao.topicIsUsed(topic))
                        System.out.println("Task topic is used! Choose another one!");
                    else
                        stage = "set user";
                        task.setTopic(topic);
                    break;

                case "set user":
                    System.out.println("Enter user login. Existing users:");
                    showUsers();
                    String login = reader.readLine();
                    if(userDao.Exists(new User(login)))
                    {
                        User user = userDao.getByLogin(login);
                        task.setUser(user);
                        stage = "set project";
                    }
                    else
                    {
                        System.out.println("User does not exist. Try again.");
                    }
                    break;

                case  "set project":
                    System.out.println("Enter project name. Existing projects: ");
                    showProjects();
                    String projectName = reader.readLine();
                    if(projectDao.Exists(new Project(projectName)))
                    {
                        Project project = projectDao.getByName(projectName);
                        task.setProject(project);
                        stage = "set type";
                    }
                    else
                    {
                        System.out.println("Project does not exist. Try again.");
                    }
                    break;

                case "set type":
                    System.out.println("Enter task type: 0 - planning / 1 - reactionary / 2 - procedural ");
                    int typeNum = Integer.parseInt(reader.readLine());
                    if(typeNum < TaskType.values().length && typeNum >=0)
                    {
                        TaskType type = TaskType.values()[typeNum];
                        task.setType(type);
                        stage = "set priority";
                    }
                    else
                    {
                        System.out.println("Incorrect input: " + typeNum + ", try again.");
                    }

                case "set priority":
                    System.out.println("Enter priority in range of 1 to 5");
                    int priority = Integer.parseInt(reader.readLine());
                    if(priority>0 && priority<6)
                    {
                        task.setPriority(priority);
                        stage = "set description";
                    }
                    else
                    {
                        System.out.printf("Incorrect input: " + priority + ". Try again.");
                    }
                    break;

                case "set description":
                    System.out.println("Enter description:");
                    String description = reader.readLine();
                    task.setDescription(description);
                    taskDao.insert(task);
                    finished = true;
                    break;
            }

            if(finished)
            {
                System.out.println("New task has been added!");
                break;
            }

        }

    }


    private void showTasks(List<Task> tasks)
    {
        System.out.println("\n Tasks:");
        for(Task task : tasks)
        {
            System.out.println("Task topic: " + task.getTopic() + " , task type: " + task.getType() + " , task priority:  " + task.getPriority() +
                                " , task user: " + task.getUser().getLogin() + " , project: " + task.getProject().getName());
            System.out.println("Description: " + task.getDescription() + " \n ");
        }
    }


    private void showTasksForUser() throws IOException
    {
        System.out.println("Enter user login: ");
        String login = reader.readLine();
        User user = new User(login);
        if(userDao.Exists(user))
        {
            List<Task> tasks = taskDao.getByUser(new User(login));
            showTasks(tasks);

        }
        else
        {
            System.out.println("User does not exist. Current users are:");// view.Error("No such user.")
            showUsers();
        }

    }

    private void showTasksForProject() throws IOException
    {
        System.out.println("Enter project name: ");
        String name = reader.readLine();
        Project project = new Project(name);
        if(projectDao.Exists(project))
        {
            List<Task> tasks = taskDao.getByProject(new Project(name));
            showTasks(tasks);

        }
        else
        {
            System.out.println("Project does not exist. Current projects are:");// view.Error("No such user.")
            showProjects();
        }
    }

    public void deleteTask() throws IOException
    {
        System.out.println("Existing topics: ");
        for(String topic : taskDao.getTopics())
        {
            System.out.println(topic);
        }
        System.out.println("\n Enter task topic: ");
        String topic = reader.readLine();
        if(taskDao.exists(topic))
        {
            Task task = new Task();
            task.setTopic(topic);
            taskDao.delete(task);
        }
        else
        {
            System.out.println("Task does not exist.");
        }
    }


    private void changeDb() throws IOException
    {
        System.out.println(" Enter DB file name: ");
        String fileDB = reader.readLine();
        DbConnector.setDbFile(fileDB);
    }






    public void run()
    {
        String command = "command";

        showCommands();

        while (true)
        {
            System.out.println("\n Enter the command: ");

            try{
                command = reader.readLine();
            } catch (IOException e) {
                System.out.println(e);
            }

            switch (command)
            {
                case "show users":
                    showUsers();
                    break;

                case "add user":
                    addUser();
                    break;

                case "delete user":
                    deleteUser();
                    break;

                case "add project":
                    addProject();
                    break;


                case "show projects":
                    showProjects();
                    break;

                case "delete project":
                    deleteProject();
                    break;


                case "add task":
                    try{
                        addTask();
                    } catch (IOException e)
                    {
                        System.out.println(e);
                    }
                    break;


                case "show tasks":
                    List<Task> tasks = taskDao.getAll();
                    showTasks(tasks);
                    break;

                case "show tasks for user":
                    try{
                        showTasksForUser();
                    } catch(IOException e)
                    {
                        System.out.println(e);
                    }
                    break;

                case "show tasks for project":
                    try{
                        showTasksForProject();
                    } catch(IOException e)
                    {
                        System.out.println(e);
                    }
                    break;

                case "delete task":
                    try{
                        deleteTask();
                    } catch(IOException e)
                    {
                        System.out.println(e);
                    }
                    break;

                case "change db":
                    try{
                        changeDb();
                    } catch (IOException e)
                    {
                        System.out.println(e);
                    }
                    break;



                case "commands":
                    showCommands();
                    break;

                default:
                    System.out.println("Incorrect command");
            }
        }
    }




}
