package info.shreygupta.testapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by XCODER on 2/28/2018.
 */

public class ProductList {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    public class ExtensionAttributes {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("minimal_price")
        @Expose
        private Integer minimalPrice;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("description")
        @Expose
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMinimalPrice() {
            return minimalPrice;
        }

        public void setMinimalPrice(Integer minimalPrice) {
            this.minimalPrice = minimalPrice;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

}
