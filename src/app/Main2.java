import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

void main() {

    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    System.out.println("========TEST 1: department findById========");
    Department department = departmentDao.findById(3);
    System.out.println(department);

    System.out.println("========TEST 2: department findAll========");
    List<Department> list = departmentDao.findAll();
    for (Department dep : list) {
        System.out.println(dep);
    }

    System.out.println("========TEST 2: department insert========");
    Department newDepartment = new Department(null,"Games");
    departmentDao.insert(newDepartment);
    System.out.println("Inserted department with id: " + newDepartment.getId());
}
