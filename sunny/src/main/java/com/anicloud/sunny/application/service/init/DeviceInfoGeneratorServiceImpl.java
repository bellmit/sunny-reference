package com.anicloud.sunny.application.service.init;

import com.ani.bus.service.commons.dto.anidevice.stub.StubMeta;
import com.ani.octopus.commons.stub.dto.StubDto;
import com.ani.octopus.commons.stub.dto.StubInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhaoyu on 15-7-1.
 */
@Service
public class DeviceInfoGeneratorServiceImpl extends DeviceInfoGeneratorService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceInfoGeneratorServiceImpl.class);
    private final static String FILE_PATH_DEVICE_TYPE = "properties/DeviceTypeRule.json";
    private final static String FILE_PATH_DEVICE_LOGO = "properties/DeviceLogoUrl.json";

    @Resource
    private ObjectMapper objectMapper;

    @Override
    @PostConstruct
    public void initDeviceTypeGeneratorRule() {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        org.springframework.core.io.Resource resource = resourceLoader.getResource(FILE_PATH_DEVICE_TYPE);
        try {
            File targetFile = resource.getFile();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
            Map<String, ArrayList<HashMap<String, Integer>>> typeRule = objectMapper.readValue(targetFile, Map.class);
            Map<String, Set<StubIdentity>> targetTypeRule = new HashMap<>();
            Iterator<Map.Entry<String, ArrayList<HashMap<String, Integer>>>> entries = typeRule.entrySet().iterator();
            while(entries.hasNext()){
                Map.Entry<String, ArrayList<HashMap<String, Integer>>> entry = entries.next();
                ArrayList<HashMap<String, Integer>> stublist = entry.getValue();
                Set<StubIdentity> stubIdentitySet = new HashSet<>();
                for (int i = 0;i < stublist.size();i++) {
                    String stubId = stublist.get(i).get("stubId").toString();
                    StubIdentity stubIdentity = new StubIdentity(
                            stublist.get(i).get("stubId"),
                            stublist.get(i).get("groupId").longValue()
                    );
                    stubIdentitySet.add(stubIdentity);
                }
                targetTypeRule.put(entry.getKey(),stubIdentitySet);
            }
            super.deviceTypeRule = targetTypeRule;
        } catch (IOException e) {
            LOGGER.error("read device type generate rule error. {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String generatorDeviceType(List<StubInfoDto> stubs) {
        Set<StubIdentity> identitySet = fetchDeviceStubSet(stubs);
        Set<String> keySet = super.deviceTypeRule.keySet();
        String deviceType = "";
        double percentage = 0.0;
        for (String key : keySet) {
            Set<StubIdentity> valueSet = (Set<StubIdentity>) super.deviceTypeRule.get(key);
            Collection<StubIdentity> intersectionSet = CollectionUtils.intersection(identitySet, valueSet);
            double tempPercentage = intersectionSet.size() / Double.valueOf(valueSet.size());
            // when percentage is 1.0
            if (tempPercentage == 1.0) {
                return key;
            } else {
                if (tempPercentage > percentage) {
                    percentage = tempPercentage;
                    deviceType = key;
                }
            }
        }
        return deviceType;
    }

    @Override
    @PostConstruct
    public void initDeviceLogoUrl() {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        org.springframework.core.io.Resource resource = resourceLoader.getResource(FILE_PATH_DEVICE_LOGO);
        try {
            File targetFile = resource.getFile();
            deviceLogoUrls = objectMapper.readValue(targetFile, Map.class);
        } catch (IOException e) {
            LOGGER.error("read device logo error. {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getDeviceLogoUrl(String deviceType) {
        String logoUrl = deviceLogoUrls.get(deviceType);
        return  logoUrl;
    }

    private Set<StubIdentity> fetchDeviceStubSet(List<StubInfoDto> stubs) {
        if (stubs == null) {
            throw new IllegalArgumentException("stubs is null.");
        }
        Set<StubIdentity> stubIdentitySet = new HashSet<>();
        for (StubInfoDto stubDto : stubs) {
            StubIdentity stubIdentity = new StubIdentity(
                    stubDto.stubId,
                    stubDto.group.groupId
            );
            stubIdentitySet.add(stubIdentity);
        }
        return stubIdentitySet;
    }
}
