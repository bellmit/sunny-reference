package com.anicloud.sunny.application.service.init;

import com.ani.bus.service.commons.dto.anidevice.DeviceMasterObjInfoDto;
import com.ani.cel.service.manager.agent.core.AnicelServiceConfig;
import com.ani.cel.service.manager.agent.core.share.DeviceState;
import com.ani.cel.service.manager.agent.device.model.DeviceMasterInfoDto;
import com.ani.cel.service.manager.agent.device.model.DeviceSlaveInfoDto;
import com.ani.cel.service.manager.agent.device.model.FunctionArgumentDto;
import com.ani.cel.service.manager.agent.device.model.FunctionInfoDto;
import com.ani.cel.service.manager.agent.device.service.DeviceService;
import com.ani.cel.service.manager.agent.device.service.DeviceServiceImpl;
import com.ani.cel.service.manager.agent.oauth2.model.OAuth2AccessToken;
import com.ani.octopus.commons.accout.dto.AccountDto;
import com.ani.octopus.commons.object.dto.object.ObjectSlaveInfoDto;
import com.ani.octopus.commons.object.enumeration.AniObjectState;
import com.ani.octopus.commons.stub.dto.StubDto;
import com.ani.octopus.service.agent.service.oauth.dto.AniOAuthAccessToken;
import com.anicloud.sunny.application.builder.DeviceAndFeatureRelationDtoBuilder;
import com.anicloud.sunny.application.builder.DeviceDtoBuilder;
import com.anicloud.sunny.application.constant.Constants;
import com.anicloud.sunny.application.dto.device.DeviceAndFeatureRelationDto;
import com.anicloud.sunny.application.dto.device.DeviceDto;
import com.anicloud.sunny.application.dto.device.DeviceFeatureDto;
import com.anicloud.sunny.application.dto.device.FeatureFunctionDto;
import com.anicloud.sunny.application.dto.user.UserDto;
import com.anicloud.sunny.application.service.agent.AgentTemplate;
import com.anicloud.sunny.application.service.device.DeviceAndFeatureRelationService;
import com.anicloud.sunny.application.service.device.DeviceFeatureService;
import com.anicloud.sunny.application.service.user.UserService;
import com.anicloud.sunny.domain.model.device.DeviceFeature;
import com.anicloud.sunny.infrastructure.persistence.domain.share.ArgumentType;
import com.anicloud.sunny.infrastructure.utils.DeviceFeatureJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhaoyu on 15-7-11.
 */
@Service
@Transactional
public class ApplicationInitServiceImpl extends ApplicationInitService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationInitServiceImpl.class);

    @Resource
    private UserService userService;
    @Resource
    private DeviceFeatureService deviceFeatureService;
    @Resource
    private DeviceInfoGeneratorService deviceInfoGeneratorService;
    @Resource
    private com.anicloud.sunny.application.service.device.DeviceService sunnyDeviceService;
    @Resource
    private DeviceAndFeatureRelationService deviceAndFeatureRelationService;
    @Resource
    private ObjectMapper objectMapper;
    @Resource(name = "agentTemplate")
    private AgentTemplate agentTemplate;
    private static List<DeviceFeatureDto> deviceFeatureDtos;

    static {
        try {
            deviceFeatureDtos = DeviceFeatureJsonUtils.getDeviceFeatureDtoListFromJsonFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected UserDto initUser(UserDto userDto) {
        LOGGER.info("initUser {}.", userDto);
        return userService.saveUser(userDto);
    }

    @Override
    protected void initUserDeviceAndDeviceFeatureRelation(AccountDto accountDto,
                                                          AniOAuthAccessToken accessToken) throws Exception {
        List<DeviceMasterObjInfoDto> deviceMasterObjInfoDtoList = agentTemplate
                .getDeviceObjService(accessToken.getAccessToken())
                .getDeviceObjInfo(accountDto.accountId, Boolean.TRUE);
        List<DeviceAndFeatureRelationDto> deviceAndFeatureRelationDtos = getRelation(deviceMasterObjInfoDtoList, accessToken);
        LOGGER.info("Initialize DeviceAndFeatureRelation...");
        deviceAndFeatureRelationService.batchSave(deviceAndFeatureRelationDtos);
    }

    @Override
    protected boolean isUserNotExists(Long accountId) {
        // return userService.getUserByHashUserId(hashUserId);
        UserDto userDto = userService.getUserByHashUserId(accountId);
        return userDto == null ? true : false;
    }

    @Override
    public UserDto initApplication(AniOAuthAccessToken accessToken) throws Exception {
        AccountDto accountDto = agentTemplate
                .getAccountService(accessToken.getAccessToken())
                .getByAccessToken();
        UserDto userDto = fetchUserInfo(accountDto, accessToken);
        // not exists
        if (!isUserNotExists(accountDto.accountId)) {
            // init user
            initUser(userDto);
            // init user-device-devicefeature relation
            initUserDeviceAndDeviceFeatureRelation(accountDto, accessToken);
        }
        return userDto;
    }

    protected UserDto fetchUserInfo(AccountDto accountDto, AniOAuthAccessToken accessToken) {
        return new UserDto(
                accessToken.getAccessToken(),
                accountDto.email,
                accessToken.getExpiresIn(),
                accountDto.accountId,
                accessToken.getRefreshToken(),
                accessToken.getScope(),
                accountDto.screenName,
                accessToken.getTokenType(),
                getCurrentTime()
        );
    }

    public List<DeviceAndFeatureRelationDto> getRelation(List<DeviceMasterObjInfoDto> deviceMasterObjInfoDtoList, AniOAuthAccessToken accessToken) throws Exception {
        List<DeviceAndFeatureRelationDto> deviceAndFeatureDtos = new ArrayList<>();
        for (DeviceMasterObjInfoDto dto : deviceMasterObjInfoDtoList) {
            for (ObjectSlaveInfoDto objDto : dto.slaves) {
                DeviceDto deviceDto = new DeviceDto(
                        "default",
                        convert(objDto.state),
                        "unknown",
                        buildId(dto.objectId, objDto.objectSlaveId),
                        dto.name,
                        fetchUserInfo(dto.owner, accessToken),
                        null
                );
                DeviceAndFeatureRelationDto deviceAndFeatureDto = new DeviceAndFeatureRelationDto(
                        deviceDto, buildDeviceFeatureByStubDto(objDto.stubs));
                deviceAndFeatureDtos.add(deviceAndFeatureDto);
            }
        }
        return deviceAndFeatureDtos;
    }

    public static DeviceState convert(AniObjectState state) {
        switch (state) {
            case ACTIVE:
                return DeviceState.CONNECTED;
            case DISABLE:
                return DeviceState.DISCONNECTED;
            case REMOVED:
                return DeviceState.REMOVED;
        }
        return null;
    }

    public String buildId(Long deviceMasterId, Integer slaveId) {
        return deviceMasterId.toString() + "-" + slaveId.toString();
    }

    public List<DeviceFeatureDto> buildDeviceFeatureByStubDto(List<StubDto> stubDtos) {
        List<DeviceFeatureDto> deviceFeatureDtoList = new ArrayList<>();
        for (DeviceFeatureDto deviceFeatureDto : deviceFeatureDtos) {
            for (StubDto stubDto : stubDtos) {

            }
        }
        return deviceFeatureDtoList;
    }

    public boolean isBelongDeviceFeature(DeviceFeatureDto deviceFeatureDto,List<StubDto> stubDtos){
        boolean validata =false;
        for (StubDto stubDto : stubDtos) {
            for (FeatureFunctionDto ffd : deviceFeatureDto.featureFunctionDtoList) {
                if(ffd.stubId.equals(stubDto.stubId.toString())){
                    validata = true;
                    break;
                }
            }
        }
        return validata;
    }

    public Long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
