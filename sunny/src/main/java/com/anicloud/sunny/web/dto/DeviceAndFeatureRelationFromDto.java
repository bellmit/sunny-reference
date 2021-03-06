package com.anicloud.sunny.web.dto;

import com.anicloud.sunny.application.dto.device.DeviceAndFeatureRelationDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sirhuoshan on 2015/7/16.
 */
public class DeviceAndFeatureRelationFromDto {
    public DeviceFormDto deviceFormDto;
    public List<DeviceFeatureFormDto> deviceFeatureFormDtoList;

    public static DeviceAndFeatureRelationFromDto convertToDeviceAndFeatureRelationFrom(DeviceAndFeatureRelationDto deviceAndFeatureRelationDto){
        DeviceAndFeatureRelationFromDto deviceAndFeatureRelationFromDto = new DeviceAndFeatureRelationFromDto();
        if(deviceAndFeatureRelationDto != null) {
            deviceAndFeatureRelationFromDto.deviceFormDto = DeviceFormDto.convertToDeviceForm(deviceAndFeatureRelationDto.deviceDto);
            deviceAndFeatureRelationFromDto.deviceFeatureFormDtoList = DeviceFeatureFormDto.convertToDeviceFeatureForms(deviceAndFeatureRelationDto.deviceFeatureDtoList);
        }
        return deviceAndFeatureRelationFromDto;
    }

    public static List<DeviceAndFeatureRelationFromDto> convertToDeviceAndFeatureRelationFroms(List<DeviceAndFeatureRelationDto> deviceAndFeatureRelationDtos){
        List<DeviceAndFeatureRelationFromDto> deviceAndFeatureRelationFromDtos = new ArrayList<>();
        if(deviceAndFeatureRelationDtos != null) {
            for (DeviceAndFeatureRelationDto deviceAndFeatureRelationDto : deviceAndFeatureRelationDtos) {
                deviceAndFeatureRelationFromDtos.add(convertToDeviceAndFeatureRelationFrom(deviceAndFeatureRelationDto));
            }
        }
        return deviceAndFeatureRelationFromDtos;
    }
}
