package com.anicloud.sunny.application.assemble;

import com.anicloud.sunny.application.dto.device.FeatureArgDto;
import com.anicloud.sunny.application.dto.strategy.FeatureTriggerDto;
import com.anicloud.sunny.domain.model.device.FeatureArg;
import com.anicloud.sunny.infrastructure.persistence.domain.device.FeatureArgDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyu on 15-7-10.
 */
public class FeatureArgAssembler {
    private FeatureArgAssembler() {}

    public static FeatureArg toFeatureArg(FeatureArgDto featureArgDto) {
        if (featureArgDto == null) return null;
        return new FeatureArg(
                featureArgDto.dataType,
                featureArgDto.name,
                featureArgDto.screenName
        );
    }

    public static FeatureArgDto toDto(FeatureArg featureArg) {
        if (featureArg == null) return null;
        return new FeatureArgDto(
                featureArg.dataType,
                featureArg.name,
                featureArg.screenName
        );
    }

    public static List<FeatureArg> toFeatureArgList(List<FeatureArgDto> featureArgDtoList) {
        if (featureArgDtoList == null) return null;
        List<FeatureArg> featureArgList = new ArrayList<>();
        for (FeatureArgDto featureArgDto : featureArgDtoList) {
            featureArgList.add(toFeatureArg(featureArgDto));
        }
        return featureArgList;
    }

    public static List<FeatureArgDto> toDtoList(List<FeatureArg> featureArgList) {
        if (featureArgList == null) return null;
        List<FeatureArgDto> featureArgDtoList = new ArrayList<>();
        for (FeatureArg featureArg : featureArgList) {
            featureArgDtoList.add(toDto(featureArg));
        }
        return featureArgDtoList;
    }
}
