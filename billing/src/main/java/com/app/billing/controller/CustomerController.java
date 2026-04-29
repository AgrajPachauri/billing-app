package com.app.billing.controller;

import com.app.billing.dto.DeleteRequest;
import com.app.billing.model.Customer;
import com.app.billing.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository repo;

    // Add Customer
    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return repo.save(customer);
    }

    // Get All Customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    // Get Customers by Area
    @GetMapping("/by-area")
    public List<Customer> getByArea(@RequestParam String area) {
        return repo.findByArea(area);
    }
    @PutMapping("/{id}")
public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {

    Customer customer = repo.findById(id).orElse(null);

    if (customer == null) {
        return null;
    }

    customer.setName(updatedCustomer.getName());
    customer.setArea(updatedCustomer.getArea());
    customer.setAddress(updatedCustomer.getAddress());
    customer.setContact(updatedCustomer.getContact());

    return repo.save(customer);
}
@DeleteMapping("/{id}")
public String deleteCustomer(@PathVariable Long id) {

    repo.deleteById(id);
    return "Customer deleted";
}
@DeleteMapping("/multiple")
public String deleteMultiple(@RequestBody DeleteRequest request) {

    repo.deleteAllById(request.getIds());
    return "Selected customers deleted";
}
@GetMapping("/areas")
public List<String> getAreas() {
    return repo.findAll()
            .stream()
            .map(Customer::getArea)
            .distinct()
            .toList();
}
@GetMapping("/all")
public List<Customer> testAll() {
    return repo.findAll();
}
@GetMapping("/add-test")
public String addTestCustomer() {

    Customer c = new Customer();
    c.setName("Ramesh");
    c.setArea("Area A");
    c.setAddress("Street 1");
    c.setContact("9876543210");

    repo.save(c);

    return "Test customer added";
}
}