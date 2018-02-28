package info.shreygupta.testapp.Models;

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
import java.util.List;

/**
 * Created by XCODER on 2/26/2018.
 */
public class Variant {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Integer price;
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
        if (price == null)
            return 0;
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCustomAttributes(String key) {
        for (CustomAttribute ca : customAttributes) {
            if (ca.getAttributeCode().equals(key)) {
                return ca.getValue();
            }
        }
        return "per -- photos";
    }

    public List<CustomAttribute> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(List<CustomAttribute> customAttributes) {
        this.customAttributes = customAttributes;
    }

    public static class CustomAttribute {

        @SerializedName("attribute_code")
        @Expose
        private String attributeCode;
        @SerializedName("value")
        @Expose
        private String value;

        public String getAttributeCode() {
            return attributeCode;
        }

        public void setAttributeCode(String attributeCode) {
            this.attributeCode = attributeCode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static class CustomAttrValueTypeAdapter extends TypeAdapter<Variant.CustomAttribute> {

        private Gson gson = new Gson();

        @Override
        public void write(JsonWriter out, Variant.CustomAttribute value) throws IOException {
            gson.toJson(value, Variant.CustomAttribute.class, out);
        }

        @Override
        public Variant.CustomAttribute read(JsonReader jsonReader) throws IOException {
            Variant.CustomAttribute locations = new Variant.CustomAttribute();
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
}
