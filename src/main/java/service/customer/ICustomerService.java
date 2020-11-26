package service.customer;

import model.Customer;
import model.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import service.IService;

import javax.swing.text.html.HTMLDocument;

public interface ICustomerService extends IService<Customer> {

    Iterable<Customer> findAllByProvince(Province province);

    Page<Customer> findAll(Pageable pageable);

    Page<Customer> findAllByNameContaining(String name, Pageable pageable);
}
