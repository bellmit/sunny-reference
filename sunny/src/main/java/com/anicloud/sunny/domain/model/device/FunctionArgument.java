package com.anicloud.sunny.domain.model.device;

import com.ani.cel.service.manager.agent.core.share.DataType;
import com.anicloud.sunny.domain.share.AbstractDomain;
import com.anicloud.sunny.infrastructure.persistence.domain.device.FunctionArgumentDao;
import com.anicloud.sunny.infrastructure.persistence.domain.share.ArgumentType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhaoyu on 15-6-12.
 */
public class FunctionArgument extends AbstractDomain {
    private static final long serialVersionUID = -3960860210268032641L;

    public String name;
    public DataType dataType;
    public ArgumentType argumentType;
    public FunctionArgument() {
    }

    public FunctionArgument(ArgumentType argumentType, DataType dataType, String name) {
        this.argumentType = argumentType;
        this.dataType = dataType;
        this.name = name;
    }

    public static FunctionArgument toFunctionArgument(FunctionArgumentDao argumentDao) {
        if (argumentDao == null) {
            return null;
        }
        FunctionArgument functionArgument = new FunctionArgument(
                argumentDao.argumentType,
                argumentDao.dataType,
                argumentDao.name
        );
        return functionArgument;
    }

    public static FunctionArgumentDao toDao(FunctionArgument argument) {
        if (argument == null) {
            return null;
        }
        FunctionArgumentDao argumentDao = new FunctionArgumentDao(
                argument.argumentType,
                argument.dataType,
                argument.name
        );
        return argumentDao;
    }

    public static List<FunctionArgument> toFunctionArgumentList(List<FunctionArgumentDao> functionArgumentDaoList) {
        if (functionArgumentDaoList == null) {
            return null;
        }

        List<FunctionArgument> argumentList = new ArrayList<>();
        for (FunctionArgumentDao argumentDao : functionArgumentDaoList) {
            argumentList.add(toFunctionArgument(argumentDao));
        }
        return argumentList;
    }

    public static List<FunctionArgumentDao> toDaoList(List<FunctionArgument> functionArgumentList) {
        if (functionArgumentList == null) {
            return null;
        }

        List<FunctionArgumentDao> argumentDaoList = new ArrayList<>();
        for (FunctionArgument functionArgument : functionArgumentList) {
            argumentDaoList.add(toDao(functionArgument));
        }
        return argumentDaoList;
    }

    @Override
    public String toString() {
        return "FunctionArgument{" +
                "dataType=" + dataType +
                ", name='" + name + '\'' +
                '}';
    }
}
