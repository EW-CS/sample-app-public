package com.clearsense.providerintegration.common;

import com.clearsense.providerintegration.adapters.locale.LocaleUtils;
import lombok.Getter;
import lombok.Setter;


public enum ErrorType {

    INVALID_ARGUMENT("7000101", "invalid.request.arguments"),
    INVALID_OR_NO_TENANT("7000105", "invalid.request.tenantId"),
    NO_TENANT_PERMISSION("7000106", "no.tenant.permission"),
    INVALID_OR_NO_USER("7000106", "invalid.request.userId"),
    GENERIC_ERROR("7000107", "invalid.request.generic");

    public @Getter final String key;
    public @Getter final String errorCode;
    private @Setter LocaleUtils localeUtils;


     ErrorType(String errorCode, String key){
        this.errorCode = errorCode;
        this.key = key;
    }

    public String getMessage(){
         return getKey();
    }

    public String getMessage(Object[] objects){
        return LocaleUtils.getMessage(this.key, objects);
    }

    public String getLocaleMessage(){
        return LocaleUtils.getMessage(this.key, null);
    }

}
