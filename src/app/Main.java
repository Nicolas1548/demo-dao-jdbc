import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

void main() {
    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("========TEST 1: seller findById========");
    Seller seller = sellerDao.findById(3);
    System.out.println(seller);
}
