package com.cy.store.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/* 作为实体类的基类 */
public class BaseEntity implements Serializable {
    private String createdUser;
    private Date createdTime;
    private String modifiedUser;
    private Date modifiedTime;

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseEntity that)) return false;

        return Objects.equals(getCreatedUser(), that.getCreatedUser()) && Objects.equals(getCreatedTime(), that.getCreatedTime()) && Objects.equals(getModifiedUser(), that.getModifiedUser()) && Objects.equals(getModifiedTime(), that.getModifiedTime());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getCreatedUser());
        result = 31 * result + Objects.hashCode(getCreatedTime());
        result = 31 * result + Objects.hashCode(getModifiedUser());
        result = 31 * result + Objects.hashCode(getModifiedTime());
        return result;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "createdUser='" + createdUser + '\'' +
                ", createdTime=" + createdTime +
                ", modifiedUser='" + modifiedUser + '\'' +
                ", modifiedTime=" + modifiedTime +
                '}';
    }
}
