package com.anicloud.sunny.application.service.init;

import com.ani.octopus.commons.object.dto.object.ObjectSlaveInfoDto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by zhaoyu on 15-7-1.
 */
public abstract class DeviceInfoGeneratorService {
    protected Map<String, Set<StubIdentity>> deviceTypeRule;  // key is type, value set is stub set

    public abstract void initDeviceTypeGeneratorRule();
    public abstract String generatorDeviceType(ObjectSlaveInfoDto slaveInfoDto);

}

class StubIdentity implements Serializable {
    public Integer stubId;
    public Long groupId;

    public StubIdentity() {
    }

    public StubIdentity(Integer stubId, Long groupId) {
        this.stubId = stubId;
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StubIdentity that = (StubIdentity) o;
        return Objects.equals(stubId, that.stubId) &&
                Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stubId, groupId);
    }

    @Override
    public String toString() {
        return "StubIdentity{" +
                "stubId=" + stubId +
                ", groupId=" + groupId +
                '}';
    }
}
