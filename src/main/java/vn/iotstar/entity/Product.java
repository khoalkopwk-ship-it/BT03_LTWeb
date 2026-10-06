package vn.iotstar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;

    @Column(nullable = false, length = 255)
    private String product_name;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "nvarchar(max)")
    private String description;

    @Column(length = 255)
    private String image;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cate_id", nullable = false)
    private Category category;

    public Product() {
    }

    @PrePersist
    private void prePersist() {
        if (createdDate == null) {
            createdDate = new Date();
        }
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
