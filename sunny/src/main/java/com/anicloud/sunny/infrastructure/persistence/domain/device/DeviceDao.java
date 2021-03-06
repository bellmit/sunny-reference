package com.anicloud.sunny.infrastructure.persistence.domain.device;


import com.ani.agent.service.commons.object.enumeration.DeviceState;
import com.anicloud.sunny.infrastructure.persistence.domain.share.AbstractEntity;
import com.anicloud.sunny.infrastructure.persistence.domain.share.DeviceLogicState;
import com.anicloud.sunny.infrastructure.persistence.domain.user.UserDao;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by zhaoyu on 15-6-8.
 */
@Entity
@Table(name = "t_device")
public class DeviceDao extends AbstractEntity {
    private static final long serialVersionUID = 7730145057050493786L;

    @Column(name = "identification_code", nullable = false, unique = true, length = 150)
    public String identificationCode;       // id of device, consist of masterDeviceId and slaveDeviceId
    @Column(name = "name", nullable = false, length = 100)
    public String name;
    @Column(name = "device_state", nullable = false)
    @Enumerated(EnumType.STRING)
    public DeviceState deviceState;
    @Column(name = "logic_state", nullable = false)
    @Enumerated(EnumType.STRING)
    public DeviceLogicState logicState;
    @Column(name = "device_type", length = 50)
    public String deviceType;
    @Column(name = "device_group", length = 50)
    public String deviceGroup;
    @Column(name = "logoUrl", length = 200)
    public String logoUrl;
    @Column(name = "user_id", nullable = true)
    public Long ownerId;


    public DeviceDao() {
    }

    public DeviceDao(String deviceGroup, DeviceState deviceState, String deviceType,
                     String identificationCode, String name, Long ownerId, DeviceLogicState logicState, String logoUrl) {
        this.deviceGroup = deviceGroup;
        this.deviceState = deviceState;
        this.deviceType = deviceType;
        this.identificationCode = identificationCode;
        this.name = name;
        this.ownerId = ownerId;
        this.logicState = logicState;
        this.logoUrl = logoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeviceDao deviceDao = (DeviceDao) o;
        return Objects.equals(identificationCode, deviceDao.identificationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), identificationCode);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
