package info.shreygupta.testapp.Models;

import android.text.Html;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.shreygupta.testapp.Utilities.ApiConfiguration;

/**
 * Created by XCODER on 2/24/2018.
 */

public class Product {
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
    @SerializedName("media_gallery_entries")
    @Expose
    private List<MediaGalleryEntry> mediaGalleryEntries = null;
    @SerializedName("custom_attributes")
    @Expose
    private List<CustomAttribute> customAttributes = null;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    public ArrayList<String> getImagesList() {
        ArrayList<String> list = new ArrayList<>();
        for (MediaGalleryEntry entry : mediaGalleryEntries) {
            list.add(entry.getFile());
        }
        return list;
    }

    public List<MediaGalleryEntry> getMediaGalleryEntries() {
        return mediaGalleryEntries;
    }

    public void setMediaGalleryEntries(List<MediaGalleryEntry> mediaGalleryEntries) {
        this.mediaGalleryEntries = mediaGalleryEntries;
    }

    public List<CustomAttribute> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<CustomAttribute> customAttributes) {
        this.customAttributes = customAttributes;
    }

    public String getCustomAttributes(String key) {
        for (Product.CustomAttribute ca : customAttributes) {
            if (ca.getAttributeCode().equals(key)) {
                return ca.getValue();
            }
        }
        return "Not Available";
    }

    public static class CustomAttribute {

        @SerializedName("attribute_code")
        @Expose
        private String attributeCode;
        @SerializedName("value")
        @Expose
        private String value;


        public CustomAttribute() {

        }

        public String getAttributeCode() {
            return attributeCode;
        }

        public void setAttributeCode(String attributeCode) {
            this.attributeCode = attributeCode;
        }

        public String getValue() {
            if (value == null || TextUtils.isEmpty(value))
                return "Not Available";
            else
                return Html.fromHtml(value).toString().trim()
                        .replace("</li>", "")
                        .replace("<ul>", "")
                        .replace("</ul>", "")
                        .replace("\n", "")
                        .replace("<li>", "\n" + "\\u2022" + " ");
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static class CustomAttrValueTypeAdapter extends TypeAdapter<CustomAttribute> {

        private Gson gson = new Gson();

        @Override
        public void write(JsonWriter out, CustomAttribute value) throws IOException {
            gson.toJson(value, CustomAttribute.class, out);
        }

        @Override
        public CustomAttribute read(JsonReader jsonReader) throws IOException {
            CustomAttribute locations = new CustomAttribute();
            int i = 0;
            jsonReader.beginObject();
            do {
                jsonReader.nextName();
                if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
                    locations.setValue(TextUtils.join("-", ((String[]) gson.fromJson(jsonReader, String[].class))));
                } else if (jsonReader.peek() == JsonToken.STRING) {
                    if (i == 0) {
                        locations.setAttributeCode((String) gson.fromJson(jsonReader, String.class));
                        i++;
                    } else {
                        locations.setValue((String) gson.fromJson(jsonReader, String.class));
                    }
                } else {
                    throw new JsonParseException("Unexpected token " + jsonReader.peek());
                }
            } while (jsonReader.hasNext());
            jsonReader.endObject();
            return locations;
        }
    }

    public class ConfigurableProductOption {

        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("values")
        @Expose
        private List<Value> values = null;
        private Integer selected;

        public ConfigurableProductOption() {
            selected = 0;
        }

        public Integer getSelected() {
            if (selected == null)
                selected = 0;
            return selected;
        }

        public void setSelected(Integer selected) {
            this.selected = selected;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<Value> getValues() {
            return values;
        }

        public void setValues(List<Value> values) {
            this.values = values;
        }

    }

    public class ExtensionAttributes {

        @SerializedName("configurable_product_options")
        @Expose
        private List<ConfigurableProductOption> configurableProductOptions = null;

        public List<ConfigurableProductOption> getConfigurableProductOptions() {
            return configurableProductOptions;
        }

        public void setConfigurableProductOptions(List<ConfigurableProductOption> configurableProductOptions) {
            this.configurableProductOptions = configurableProductOptions;
        }

    }

    public class ExtensionAttributes_ {

        @SerializedName("label")
        @Expose
        private String label;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

    }

    public class MediaGalleryEntry {

        @SerializedName("file")
        @Expose
        private String file;

        public String getFile() {
            return ApiConfiguration.IMAGE_PATH + file;
        }

        public void setFile(String file) {
            this.file = file;
        }

    }

    public class Value {

        @SerializedName("value_index")
        @Expose
        private Integer valueIndex;
        @SerializedName("extension_attributes")
        @Expose
        private ExtensionAttributes_ extensionAttributes;


        public Integer getValueIndex() {
            return valueIndex;
        }

        public void setValueIndex(Integer valueIndex) {
            this.valueIndex = valueIndex;
        }

        public ExtensionAttributes_ getExtensionAttributes() {
            return extensionAttributes;
        }

        public void setExtensionAttributes(ExtensionAttributes_ extensionAttributes) {
            this.extensionAttributes = extensionAttributes;
        }

    }
}
