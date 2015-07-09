package com.anicloud.sunny.domain.model.device;

import com.ani.cel.service.manager.agent.core.share.DataType;
import com.anicloud.sunny.domain.share.AbstractDomain;
import com.anicloud.sunny.infrastructure.persistence.domain.device.FunctionArgumentDao;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhaoyu on 15-6-12.
 */
public class FunctionArgument extends AbstractDomain {
    private static final long serialVersionUID = -3960860210268032641L;

    public String name;
    public DataType dataType;

    public FunctionArgument() {
    }

    public FunctionArgument(DataType dataType, String name) {
        this.dataType = dataType;
        this.name = name;
    }

    public static FunctionArgument toFunctionArgument(FunctionArgumentDao argumentDao) {
        if (argumentDao == null) {
            return null;
        }
        FunctionArgument functionArgument = new FunctionArgument(
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
                argument.dataType,
                argument.name
        );
        return argumentDao;
    }

    public static Set<FunctionArgument> toFunctionArgumentSet(Set<FunctionArgumentDao> functionArgumentDaoSet) {
        if (functionArgumentDaoSet == null) {
            return null;
        }

        Set<FunctionArgument> argumentSet = new HashSet<FunctionArgument>();
        for (FunctionArgumentDao argumentDao : functionArgumentDaoSet) {
            argumentSet.add(toFunctionArgument(argumentDao));
        }
        return argumentSet;
    }

    public static Set<FunctionArgumentDao> toDaoSet(Set<FunctionArgument> functionArgumentSet) {
        if (functionArgumentSet == null) {
            return null;
        }

        Set<FunctionArgumentDao> argumentDaoSet = new HashSet<FunctionArgumentDao>(functionArgumentSet.size());
        for (FunctionArgument functionArgument : functionArgumentSet) {
            argumentDaoSet.add(toDao(functionArgument));
        }
        return argumentDaoSet;
    }

    @Override
    public String toString() {
        return "FunctionArgument{" +
                "dataType=" + dataType +
                ", name='" + name + '\'' +
                '}';
    }
}