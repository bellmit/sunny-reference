package com.anicloud.sunny.interfaces.web;

import com.ani.cel.service.manager.agent.core.share.DeviceState;
import com.anicloud.sunny.application.dto.strategy.StrategyDto;
import com.anicloud.sunny.application.dto.user.UserDto;
import com.anicloud.sunny.application.service.strategy.StrategyService;
import com.anicloud.sunny.application.service.user.UserService;
import com.anicloud.sunny.domain.model.strategy.Strategy;
import com.anicloud.sunny.interfaces.web.dto.DeviceFeatureInstanceFormDto;
import com.anicloud.sunny.interfaces.web.dto.DeviceFormDto;
import com.anicloud.sunny.interfaces.web.dto.StrategyFormDto;
import com.anicloud.sunny.schedule.domain.strategy.StrategyAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sirhuoshan on 2015/7/1.
 */
@Controller
public class StrategyController {
    @Resource
    private StrategyService strategyService;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/strategies",method = RequestMethod.GET)
    @ResponseBody
    public List<StrategyFormDto> getStrategies(@RequestParam(value = "hashUserId")String hashUserId){
        List<StrategyDto> strategies = strategyService.getStrategyByUser(hashUserId);

        StrategyFormDto strategyFormDto = new StrategyFormDto();
        strategyFormDto.strategyId  = "1";
        strategyFormDto.strategyName = "strategy1";
        strategyFormDto.strategyDescription = "";
        strategyFormDto.stage = 2;

        List<DeviceFeatureInstanceFormDto> featureList = new ArrayList<>();

        DeviceFeatureInstanceFormDto deviceFeatureInstanceFormDto1 = new DeviceFeatureInstanceFormDto();

        deviceFeatureInstanceFormDto1.featureInstanceId = "1";
        deviceFeatureInstanceFormDto1.featureName="调节空调温度1";

        DeviceFormDto deviceFormDto = new DeviceFormDto();
        deviceFormDto.id = "12345";
        deviceFormDto.name="空调";
        deviceFormDto.deviceState = DeviceState.CONNECTED;
        deviceFormDto.deviceType = "";
        deviceFormDto.deviceGroup ="客厅";

        deviceFeatureInstanceFormDto1.device = deviceFormDto;

        featureList.add(deviceFeatureInstanceFormDto1);

        strategyFormDto.featureList = featureList;
        List<StrategyFormDto> list = new ArrayList<>();
        list.add(strategyFormDto);
        return list;
        //return StrategyFormDto.convertToStrategyForms(strategies);
    }

    @RequestMapping(value = "/strategy",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveStrategy(@RequestParam(value = "hashUserId")String hashUserId,@RequestParam(value = "strategyInstance")String strategyInstance){
        Map<String, Object> message = new HashMap<>();
        try{
            ObjectMapper mapper = new ObjectMapper();
            strategyInstance = strategyInstance.replaceAll("triggerValue","value");
            StrategyFormDto strategyFormDto = mapper.readValue(strategyInstance,StrategyFormDto.class);
            UserDto userDto = userService.getUserByHashUserId(hashUserId);
            StrategyDto strategyDto =  strategyService.saveStrategy(StrategyFormDto.convertToStrategyDto(strategyFormDto, userDto));
            StrategyFormDto strategyForm = StrategyFormDto.convertToStrategyForm(strategyDto);
            message.put("status", "success");
            message.put("strategy", strategyForm);
        }catch (Exception e){
            message.put("status", "error");
            message.put("message", "save strategy failed");
            e.printStackTrace();
        }
        return message;
    }

    @RequestMapping(value="/strategy",method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, String> deleteStrategy(@RequestParam("strategyId")String strategyId){
        Map<String, String> message = new HashMap<>();
        try{
            strategyService.removeStrategy(strategyId);
            message.put("status", "success");
            message.put("message", "delete strategy success");
        }catch (Exception e){
            message.put("status", "error");
            message.put("message", "delete strategy failed");
            e.printStackTrace();
        }
        return message;
    }

    @RequestMapping(value="/operateStrategy",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> operateStrategy(String strategyId, StrategyAction action){
        Map<String, String> message = new HashMap<>();
        try{
            strategyService.operateStrategy(strategyId,action);
            message.put("status", "success");
            message.put("message", "operate strategy success");
        }catch (Exception e){
            message.put("status", "error");
            message.put("message", "operate strategy failed");
            e.printStackTrace();
        }
        return message;
    }
}
