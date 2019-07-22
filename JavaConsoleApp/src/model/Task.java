package model;

public class Task {
    private int id;
    private User user;
    private Project project;
    private String topic;
    private TaskType type;
    private String description;
    private int priority;

    public Task(User user, Project project, String topic, TaskType type, int priority, String description)
    {
        this.user = user;
        this.project = project;
        this.topic = topic;
        this.type = type;
        this.priority = priority;
        this.description = description;
    }

    public Task(){}

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTopic()
    {
        return this.topic;
    }

    public int getId()
    {
        return this.id;
    }

    public User getUser()
    {
        return this.user;
    }

    public Project getProject()
    {
        return this.project;
    }

    public TaskType getType()
    {
        return this.type;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public String getDescription()
    {
        return this.description;
    }

}
