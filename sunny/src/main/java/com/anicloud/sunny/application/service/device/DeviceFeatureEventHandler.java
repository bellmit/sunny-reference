package com.anicloud.sunny.application.service.device;

import com.anicloud.sunny.application.assemble.DeviceFeatureDtoAssembler;
import com.anicloud.sunny.application.dto.device.DeviceFeatureDto;
import com.anicloud.sunny.application.dto.device.DeviceFeatureInfoDto;
import com.anicloud.sunny.application.utils.NumGenerate;
import com.anicloud.sunny.domain.model.device.DeviceFeature;
import com.anicloud.sunny.infrastructure.persistence.service.DeviceFeaturePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhaoyu on 15-6-12.
 */
@Service
@Transactional
public class DeviceFeatureEventHandler implements DeviceFeatureService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceFeatureEventHandler.class);

    @Resource
    private DeviceFeaturePersistenceService deviceFeaturePersistenceService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DeviceFeatureDto saveDeviceFeature(DeviceFeatureDto deviceFeatureDto) {
        if (deviceFeatureDto.featureId == null) {
            deviceFeatureDto.featureId = NumGenerate.generate();
        }

        DeviceFeature deviceFeature = DeviceFeature.save(deviceFeaturePersistenceService,
                DeviceFeatureDtoAssembler.toDeviceFeature(deviceFeatureDto));
        return DeviceFeatureDtoAssembler.toDto(deviceFeature);
    }

    @Override
    public void batchSaveDeviceFeature(List<DeviceFeatureDto> deviceFeatureDtoList) {
        for (DeviceFeatureDto deviceFeatureDto : deviceFeatureDtoList) {
            saveDeviceFeature(deviceFeatureDto);
        }
    }

    @Override
    public DeviceFeatureDto modifyDeviceFeature(DeviceFeatureDto deviceFeatureDto) {
        DeviceFeature deviceFeature = DeviceFeatureDtoAssembler.toDeviceFeature(deviceFeatureDto);
        deviceFeature = DeviceFeature.modify(deviceFeaturePersistenceService, deviceFeature);
        return DeviceFeatureDtoAssembler.toDto(deviceFeature);
    }

    @Override
    public void removeDeviceFeature(String deviceFeatureId) {
        DeviceFeature.remove(deviceFeaturePersistenceService, deviceFeatureId);
    }

    @Override
    public DeviceFeatureDto getDeviceFeatureById(String deviceFeatureId) {
        DeviceFeature deviceFeature = DeviceFeature
                .getDeviceFeatureByNum(deviceFeaturePersistenceService, deviceFeatureId);

        return DeviceFeatureDtoAssembler.toDto(deviceFeature);
    }

    @Override
    public List<DeviceFeatureDto> getAllDeviceFeature() {
        List<DeviceFeature> deviceFeatureList = DeviceFeature.getAll(deviceFeaturePersistenceService);
        return DeviceFeatureDtoAssembler.toDtoList(deviceFeatureList);
    }

    @Override
    @Cacheable(value = "deviceFeatureListCache")
    public List<DeviceFeatureInfoDto> getAllDeviceFeatureInfo() {
        List<DeviceFeature> deviceFeatureList = DeviceFeature.getAll(deviceFeaturePersistenceService);
        return DeviceFeatureDtoAssembler.toDeviceFeatureInfoDtoList(deviceFeatureList);
    }
}
