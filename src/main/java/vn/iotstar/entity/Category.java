package vn.iotstar.entity;


import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cate_id;
    private String cate_name;
    private String icons;
    @OneToMany(
            mappedBy = "category"
    )
    private List<Product> products;
    public Category() {
    }
    public int getCate_id() {
        return cate_id;
    }
    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }
    public String getCate_name() {
        return cate_name;
    }
    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }
    public String getIcons() {
        return icons;
    }
    public void setIcons(String icons) {
        this.icons = icons;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}