package controller;

import exception.NotFoundException;
import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.customer.ICustomerService;
import service.province.IProvinceService;
import service.province.ProvinceService;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Controller
public class CustomerController {

    @Autowired
    Environment environment;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProvinceService provinceService;

    @ModelAttribute("provinces")
    public Iterable<Province> provinces(){
        return provinceService.selectAll();
    }
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView showInputNotAcceptable(){
        return new ModelAndView("notfound");
    }

    @GetMapping("")
    public ModelAndView display(@PageableDefault(size = 5) Pageable pageable, @RequestParam("s") Optional<String> s) throws Exception {
        Page<Customer> customers;
        if (s.isPresent()){
            customers = customerService.findAllByNameContaining(s.get(),pageable);
        }else {
            customers = customerService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("listCustomer", customers);
        return modelAndView;
    }

    @GetMapping("/customer/create")
    public ModelAndView showFormCreate(){
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("customer",new Customer());
        return modelAndView;
    }

    @PostMapping("/customer/create")
    public ModelAndView createCustomer(@ModelAttribute Customer customer){

        Customer customerDB = new Customer( customer.getName(),customer.getAddress(),customer.getEmail(),customer.getImage(),customer.getProvince());
        MultipartFile multipartFile = customer.getAvartar();
        String image = multipartFile.getOriginalFilename();
        customerDB.setImage(image);
        String fileUpload = environment.getProperty("file_upload").toString();
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        customerService.save(customerDB);
        return new ModelAndView("create", "customer", new Customer());
    }

    @GetMapping("/customer/edit/{id}")
    public ModelAndView showFormEdit(@PathVariable Long id) throws NotFoundException{
        Customer customer = customerService.findById(id);
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }
    @PostMapping("/customer/edit/{id}")
    public String editCustomer(@ModelAttribute Customer customer) throws NotFoundException {
        MultipartFile multipartFile = customer.getAvartar();
        String image = multipartFile.getOriginalFilename();
        if (!image.equals("")){
            customer.setImage(image);
            String fileUpload = environment.getProperty("file_upload").toString();
            try {
                FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Customer customer1 = customerService.findById(customer.getId());
            customer.setImage(customer1.getImage());
        }
        customerService.update(customer);

        return "redirect:/";
    }
    @GetMapping("/customer/delete/{id}")
    public String deleteCustomer(@PathVariable Long id){
        customerService.remote(id);
        return "redirect:/";
    }

}
