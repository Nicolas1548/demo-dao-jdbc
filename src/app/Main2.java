import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

void main() {
    Scanner sc = new Scanner(System.in);
    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    System.out.println("========TEST 1: department findById========");
    Department department = departmentDao.findById(3);
    System.out.println(department);

    System.out.println("========TEST 2: department findAll========");
    List<Department> list = departmentDao.findAll();
    for (Department dep : list) {
        System.out.println(dep);
    }

    System.out.println("========TEST 3: department insert========");
    Department newDepartment = new Department(null,"Games");
    departmentDao.insert(newDepartment);
    System.out.println("Inserted department with id: " + newDepartment.getId());

    System.out.println("========TEST 4: department update========");
    department = departmentDao.findById(3);
    department.setName("IAs");
    departmentDao.update(department);
    System.out.println("Updated department with id: " + department.getId());

    System.out.println("========TEST 5: department delete========");
    System.out.println("Enter id for delete test:");
    int id = sc.nextInt();
    departmentDao.deleteById(id);
    System.out.println("Deleted department with id: " + id);


    sc.close();
}
