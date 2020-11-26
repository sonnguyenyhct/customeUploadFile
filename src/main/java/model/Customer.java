package model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Table
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String email;
    private String image;
    @Transient
    private MultipartFile avartar;

    public Customer() {
    }

    @ManyToOne(targetEntity = Province.class)
    private Province province;

    public Customer(Long id, String name, String address, String email, String image, MultipartFile avartar, Province province) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.image = image;
        this.avartar = avartar;
        this.province = province;
    }

    public Customer(String name, String address, String email, String image, Province province) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.image = image;
        this.province = province;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultipartFile getAvartar() {
        return avartar;
    }

    public void setAvartar(MultipartFile avartar) {
        this.avartar = avartar;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }
}
