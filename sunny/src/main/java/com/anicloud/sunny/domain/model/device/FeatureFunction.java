package com.anicloud.sunny.domain.model.device;

import com.ani.cel.service.manager.agent.core.share.FunctionType;
import com.anicloud.sunny.domain.share.AbstractDomain;
import com.anicloud.sunny.infrastructure.persistence.domain.device.FeatureFunctionDao;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhaoyu on 15-6-12.
 */
public class FeatureFunction extends AbstractDomain {
    private static final long serialVersionUID = -3112990501367413230L;

    public String functionGroup;
    public String functionName;
    public FunctionType functionType;
    public Integer sequenceNum;
    public Set<FunctionArgument> functionArgumentSet;

    public FeatureFunction() {
    }

    public FeatureFunction(Set<FunctionArgument> functionArgumentSet,
                           String functionGroup, String functionName,
                           FunctionType functionType, Integer sequenceNum) {
        this.functionArgumentSet = functionArgumentSet;
        this.functionGroup = functionGroup;
        this.functionName = functionName;
        this.functionType = functionType;
        this.sequenceNum = sequenceNum;
    }

    public static FeatureFunction toFeatureFunction(FeatureFunctionDao functionDao) {
        if (functionDao == null) {
            return null;
        }
        FeatureFunction featureFunction = new FeatureFunction(
                FunctionArgument.toFunctionArgumentSet(functionDao.functionArgumentDaoSet),
                functionDao.functionGroup,
                functionDao.functionName,
                functionDao.functionType,
                functionDao.sequenceNum
        );
        return featureFunction;
    }

    public static FeatureFunctionDao toDao(FeatureFunction featureFunction) {
        if (featureFunction == null) {
            return null;
        }
        FeatureFunctionDao functionDao = new FeatureFunctionDao(
                FunctionArgument.toDaoSet(featureFunction.functionArgumentSet),
                featureFunction.functionGroup,
                featureFunction.functionName,
                featureFunction.functionType,
                featureFunction.sequenceNum
        );
        return functionDao;
    }

    public static Set<FeatureFunction> toFeatureFunctionSet(Set<FeatureFunctionDao> functionDaoSet) {
        if (functionDaoSet == null) {
            return null;
        }
        Set<FeatureFunction> functionSet = new HashSet<FeatureFunction>();
        for (FeatureFunctionDao functionDao : functionDaoSet) {
            functionSet.add(toFeatureFunction(functionDao));
        }
        return functionSet;
    }

    public static Set<FeatureFunctionDao> toDaoSet(Set<FeatureFunction> functionSet) {
        if (functionSet == null) {
            return null;
        }
        Set<FeatureFunctionDao> functionDaoSet = new HashSet<FeatureFunctionDao>();
        for (FeatureFunction function : functionSet) {
            functionDaoSet.add(toDao(function));
        }
        return functionDaoSet;
    }

    @Override
    public String toString() {
        return "FeatureFunction{" +
                "functionGroup='" + functionGroup + '\'' +
                ", functionName='" + functionName + '\'' +
                ", functionType=" + functionType +
                ", sequenceNum=" + sequenceNum +
                '}';
    }
}