import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

void main() {

    Department obj = new Department(1, "books");

    Seller seller = new Seller(1, "Nicolas", "nicolas@gmail.com", new Date(), 3000.0, obj);

    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println(obj);
    System.out.println(seller);
}
