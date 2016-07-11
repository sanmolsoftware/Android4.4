package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class homePageSectionSlotBean extends UltaBean {

    /**
     *
     */
    private static final long serialVersionUID = -2118629869586963133L;
    private String slotDisplayName;
    private String slotDescription;
    private String serviceType;
    private String serviceName;
    private String serviceParameters;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlotDisplayName() {
        return slotDisplayName;
    }

    public void setSlotDisplayName(String slotDisplayName) {
        this.slotDisplayName = slotDisplayName;
    }

    public String getSlotDescription() {
        return slotDescription;
    }

    public void setSlotDescription(String slotDescription) {
        this.slotDescription = slotDescription;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceParameters() {
        return serviceParameters;
    }

    public void setServiceParameters(String serviceParameters) {
        this.serviceParameters = serviceParameters;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
