package controller;

import exception.NotFoundException;
import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import service.customer.CustomerService;
import service.customer.ICustomerService;
import service.province.IProvinceService;
import service.province.ProvinceService;

@Controller
public class ProvinceController {
    @Autowired
    private IProvinceService iProvinceService;

    @Autowired
    private ICustomerService iCustomerService;

    @GetMapping("/province")
    public ModelAndView listProvince(){
        ModelAndView modelAndView = new ModelAndView("province/list");

        modelAndView.addObject("provinces", iProvinceService.selectAll());
        return modelAndView;
    }
    @GetMapping("/province/create")
    public ModelAndView showCreateProvince(){
        ModelAndView modelAndView = new ModelAndView("province/create");
        modelAndView.addObject("province",new Province());
        return modelAndView;
    }
    @PostMapping("/province/create")
    public ModelAndView createProvince(@ModelAttribute("province") Province province ){
        ModelAndView modelAndView = new ModelAndView("province/create");
        iProvinceService.save(province);
        modelAndView.addObject("province",new Province());
        modelAndView.addObject("message", "New province created successfully");
        return modelAndView;
    }
    @GetMapping("/edit-province/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) throws NotFoundException {
        Province province = iProvinceService.findById(id);
        if(province != null) {
            ModelAndView modelAndView = new ModelAndView("/province/edit");
            modelAndView.addObject("province", province);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-province")
    public ModelAndView updateProvince(@ModelAttribute("province") Province province){
        iProvinceService.save(province);
        ModelAndView modelAndView = new ModelAndView("/province/edit");
        modelAndView.addObject("province", province);
        modelAndView.addObject("message", "Province updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-province/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) throws NotFoundException {
        Province province = iProvinceService.findById(id);
        if(province != null) {
            ModelAndView modelAndView = new ModelAndView("/province/delete");
            modelAndView.addObject("province", province);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-province")
    public String deleteProvince(@ModelAttribute("province") Province province){
        iProvinceService.remote(province.getId());
        return "redirect:/province";
    }
    @GetMapping("/view-province/{id}")
    public ModelAndView viewProvince(@PathVariable("id") Long id) throws NotFoundException {
        Province province = iProvinceService.findById(id);
        if(province == null){
            return new ModelAndView("/error.404");
        }

        Iterable<Customer> customers = iCustomerService.findAllByProvince(province);

        ModelAndView modelAndView = new ModelAndView("/province/view");
        modelAndView.addObject("province", province);
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }
}
