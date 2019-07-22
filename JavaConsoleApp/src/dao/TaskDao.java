package dao;

import model.Project;
import model.Task;
import model.TaskType;
import model.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TaskDao implements Dao<Task> {

    private static final String INSERT_TASK_TEMPLATE =
            "insert into tasks (type, topic, priority, project_id, user_id, description) " +
                    "values (%s, '%s', %s, %s, %s, '%s')";

    private static final String DELETE_TASK_TEMPLATE =
            "delete from tasks where topic = '%s'";

    private static final String CHECK_TOPIC_TEMPLATE =
            "select count(*) from tasks where topic = '%s'";

    private static final String JOIN_TASKS_TABLE_ALL =
            "select users.id as user_id, users.login as user_login, tasks.id, tasks.topic, tasks.type, tasks.description, tasks.priority, projects.id as project_id, projects.name as project_name " +
                    "from users inner join tasks on users.id = tasks.user_id " +
                    "inner join projects on tasks.project_id = projects.id; ";

    private static final String GET_TASKS_BY_USER =
            "select users.id as user_id, users.login as user_login, tasks.id, tasks.topic, tasks.type, tasks.description, tasks.priority, projects.id as project_id, projects.name as project_name " +
                    "from users inner join tasks on users.id = tasks.user_id " +
                    "inner join projects on tasks.project_id = projects.id " +
                    "where user_login = ";

    private static final String GET_TASKS_BY_PROJECT =
            "select users.id as user_id, users.login as user_login, tasks.id, tasks.topic, tasks.type, tasks.description, tasks.priority, projects.id as project_id, projects.name as project_name " +
                    "from users inner join tasks on users.id = tasks.user_id " +
                    "inner join projects on tasks.project_id = projects.id " +
                    "where project_name = ";

    private static final String SELECT_COUNT_USER = "select count(*) from tasks where user_id = %s";
    private static final String SELECT_COUNT_PROJECT = "select count(*) from tasks where project_id = %s";
    private static final String SELECT_COUNT_TOPIC = "select count(*) from tasks where topic = '%s'";
    private static final String SELECT_TOPICS = "select topic from tasks ";


    @Override
    public void insert(Task task) {

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            stm.execute(String.format(INSERT_TASK_TEMPLATE, task.getType().ordinal(), task.getTopic(), task.getPriority(),
                                                            task.getProject().getId(), task.getUser().getId(), task.getDescription()));
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }


    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(JOIN_TASKS_TABLE_ALL);
            while(rs.next())
            {
                tasks.add(mapToTask(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return tasks;
    }

    @Override
    public void delete(Task task) {
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            stm.execute(String.format(DELETE_TASK_TEMPLATE, task.getTopic()));
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }

    public boolean topicIsUsed(String topic)
    {
        boolean result = true;
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(CHECK_TOPIC_TEMPLATE, topic));
            rs.next();
            if(rs.getInt(1)==0)
                result = false;
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return result;
    }


    public List<Task> getByUser(User user)
    {
        List<Task> tasks = new ArrayList<>();

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(GET_TASKS_BY_USER + "'" + user.getLogin() + "';");
            while(rs.next())
            {
                tasks.add(mapToTask(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return tasks;
    }



    public List<Task> getByProject(Project project)
    {
        List<Task> tasks = new ArrayList<>();

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(GET_TASKS_BY_PROJECT + "'" + project.getName() + "';");
            while(rs.next())
            {
                tasks.add(mapToTask(rs));
            }

        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return tasks;
    }


    public boolean hasUser(User user)
    {
        boolean result = false;

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(SELECT_COUNT_USER, user.getId()));
            rs.next();
            if(!(rs.getInt(1) == 0))
                result = true;

        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return result;
    }

    public boolean hasProject(Project project)
    {
        boolean result = false;

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(SELECT_COUNT_PROJECT, project.getId()));
            rs.next();
            if(!(rs.getInt(1) == 0))
                result = true;
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return result;
    }


    public boolean exists(String topic)
    {
        boolean result = false;

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(SELECT_COUNT_TOPIC, topic));
            rs.next();
            if(!(rs.getInt(1) == 0))
                result = true;
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return result;
    }


    public List<String> getTopics()
    {
        List<String> topics = new ArrayList<>();

        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(SELECT_TOPICS);
            while(rs.next())
            {
                topics.add(rs.getString("topic"));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return topics;
    }




    private static Task mapToTask(ResultSet rs) throws SQLException
    {
        Task task = new Task();
        task.setUser(new User().setId(rs.getInt("user_id")).setLogin(rs.getString("user_login")));
        task.setProject(new Project().setId(rs.getInt("project_id")).setName(rs.getString("project_name")));
        task.setTopic(rs.getString("topic"));
        TaskType type = TaskType.values()[ rs.getInt("type") ];
        task.setType(type);
        task.setDescription(rs.getString("description"));
        task.setPriority(rs.getInt("priority"));
        return task;
    }
}
