package com.anicloud.sunny.application.builder;

import com.ani.cel.service.manager.agent.core.share.FunctionType;
import com.ani.cel.service.manager.agent.device.model.FunctionArgumentDto;
import com.anicloud.sunny.application.dto.device.FeatureFunctionDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhaoyu on 15-6-18.
 */
public class FeatureFunctionDtoBuilder {
    private FeatureFunctionDto featureFunctionDto;

    public FeatureFunctionDtoBuilder() {
        this.featureFunctionDto = new FeatureFunctionDto();
    }

    public FeatureFunctionDtoBuilder(FeatureFunctionDto featureFunctionDto) {
        this.featureFunctionDto = featureFunctionDto;
    }

    public FeatureFunctionDtoBuilder setFunctionGroup(String functionGroup) {
        this.featureFunctionDto.functionGroup = functionGroup;
        return this;
    }

    public FeatureFunctionDtoBuilder setFunctionId(String functionId) {
        this.featureFunctionDto.featureFunctionId = functionId;
        return this;
    }

    public FeatureFunctionDtoBuilder setFunctionName(String functionName) {
        this.featureFunctionDto.functionName = functionName;
        return this;
    }

    public FeatureFunctionDtoBuilder setFunctionType(FunctionType functionType) {
        this.featureFunctionDto.functionType = functionType;
        return this;
    }

    public FeatureFunctionDtoBuilder setInputFunctionArgument(FunctionArgumentDto argumentDto) {
        if (this.featureFunctionDto.inputArgList == null) {
            this.featureFunctionDto.inputArgList = new ArrayList<>();
        }
        this.featureFunctionDto.inputArgList.add(argumentDto);
        return this;
    }

    public FeatureFunctionDtoBuilder setInputFunctionArgument(List<FunctionArgumentDto> dtoList) {
        this.featureFunctionDto.inputArgList = dtoList;
        return this;
    }

    public FeatureFunctionDtoBuilder setOutputFunctionArgument(FunctionArgumentDto argumentDto) {
        if (this.featureFunctionDto.outputArgList == null) {
            this.featureFunctionDto.outputArgList = new ArrayList<>();
        }
        this.featureFunctionDto.outputArgList.add(argumentDto);
        return this;
    }

    public FeatureFunctionDtoBuilder setOutputFunctionArgument(List<FunctionArgumentDto> dtoList) {
        this.featureFunctionDto.outputArgList = dtoList;
        return this;
    }



    public FeatureFunctionDto instance() {
        return this.featureFunctionDto;
    }
}
