package com.example.epaycocheckout.entities;

public class TypeDoc {

    private String typeCode;
    private String typeName;

    public TypeDoc(){
        //empty constructor
    }

    public TypeDoc(String typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
