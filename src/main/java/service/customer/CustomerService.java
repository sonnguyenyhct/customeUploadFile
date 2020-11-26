package service.customer;

import exception.NotFoundException;
import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import repository.CustomerRepository;

public class CustomerService implements ICustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Iterable<Customer> selectAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Long id) throws NotFoundException {
        if (customerRepository.findOne(id) != null){
            return customerRepository.findOne(id);
        }else throw new NotFoundException();
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void update(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void remote(Long id) {
        customerRepository.delete(id);
    }

    @Override
    public Iterable<Customer> findAllByProvince(Province province) {
        return customerRepository.findAllByProvince(province);
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> findAllByNameContaining(String name, Pageable pageable) {
        return customerRepository.findAllByNameContaining(name,pageable);
    }
}
