import model.entities.Department;
import model.entities.Seller;

void main() {

    Department obj = new Department(1, "books");

    Seller seller = new Seller(1, "Nicolas", "nicolas@gmail.com", new Date(), 3000.0, obj);

    System.out.println(obj);
    System.out.println(seller);
}
