package ca.queensu.beans.sror;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonPropertyOrder({ "StringField", "NumberField" })
public class tForeignSimpleStructure {
    @JsonProperty(value = "StringField", required = true)
    private String StringField;

    @JsonProperty(value = "NumberField", required = true)
    private Double NumberField;
    public String getStringField() {
        return StringField;
    }

    public void setStringField(String stringField) {
        this.StringField = stringField;
    }

    public Double getNumberField() {
        return NumberField;
    }

    public void setNumberField(Double numberField) {
        this.NumberField = numberField;
    }
}
