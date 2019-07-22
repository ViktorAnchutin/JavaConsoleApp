package dao;

import model.Project;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectDao implements Dao<Project> {    ;

    private static final String SELECT_ALL_PROJECTS =
            "select * " +
                    "from projects";

    private static final String INSERT_PROJECT_TEMPLATE =
            "insert into projects (name) " +
                    "values ('%s');";

    private static final String DELETE_PROJECT_TEMPLATE =
            "delete from projects " +
                    "where name = '%s'";

    private static final String COUNT_PROJECT_TEMPLATE =
            "select count(*) from projects where name = '%s'";

    private static final String GET_PROJECT_BY_NAME_TEMPLATE =
            "select * from projects where name = '%s'";

    @Override
    public List<Project> getAll() {
        List<Project> projects = new ArrayList<>();
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(SELECT_ALL_PROJECTS);
            while (rs.next()) {
                projects.add(mapToProject(rs));
            }
        } catch (SQLException e) {
            System.out.println(e);
            return Collections.emptyList();
        }
        return projects;
    }

    @Override
    public void insert(Project project) {
        try{
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            stm.execute(String.format(INSERT_PROJECT_TEMPLATE, project.getName()));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void delete(Project project) {
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            stm.execute(String.format(DELETE_PROJECT_TEMPLATE, project.getName()));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public Project getByName(String name)
    {
        Project project = null;
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(GET_PROJECT_BY_NAME_TEMPLATE, name));
            rs.next();
            project = mapToProject(rs);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return  project;
    }


    public boolean Exists(Project project)
    {
        boolean result = false;
        try {
            Connection con = DbConnector.getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(String.format(COUNT_PROJECT_TEMPLATE, project.getName()));
            rs.next();
            if(!(rs.getInt(1) == 0))
                result = true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }


    private static Project mapToProject(ResultSet rs) throws SQLException {
        return new Project()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"));
    }
}
