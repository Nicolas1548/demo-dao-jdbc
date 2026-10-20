package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection connection;

    public DepartmentDaoJDBC(Connection conn) {
        this.connection = conn;
    }

    @Override
    public void insert(Department obj) {

    }

    @Override
    public void update(Department obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM department WHERE Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return dep;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResult(rs);
        }


    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
