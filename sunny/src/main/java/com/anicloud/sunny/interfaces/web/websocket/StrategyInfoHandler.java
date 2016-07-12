package com.anicloud.sunny.interfaces.web.websocket;

import com.ani.agent.service.service.websocket.AccountInvoker;
import com.ani.agent.service.service.websocket.AniInvokerImpl;
import com.ani.bus.service.commons.dto.accountobject.AccountObject;
import com.ani.bus.service.commons.message.SocketMessage;
import com.anicloud.sunny.application.constant.Constants;
import com.anicloud.sunny.application.dto.device.DeviceDto;
import com.anicloud.sunny.application.dto.strategy.StrategyDto;
import com.anicloud.sunny.interfaces.web.dto.DeviceFormDto;
import com.anicloud.sunny.interfaces.web.dto.StrategyFormDto;
import com.anicloud.sunny.interfaces.web.session.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

/**
 * Created by sirhuoshan on 2015/7/15.
 */
public class StrategyInfoHandler extends TextWebSocketHandler {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyInfoHandler.class);

    private static Map<String, WebSocketSession> sessionMaps = new HashMap<String, WebSocketSession>();

    public StrategyInfoHandler() {}

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String hashUserId = String.valueOf(session.getAttributes().get("hashUserId"));
        SessionManager.addSession(hashUserId, session);
        int sessionMapSize = sessionMaps.size();
        sessionMaps.put(hashUserId, session);
        if(sessionMapSize == 0) {
            AccountInvoker accountInvoker = new AniInvokerImpl(Constants.aniServiceSession);
            AccountObject accountObj = new AccountObject(Long.parseLong(hashUserId));
            SocketMessage socketMessage = accountInvoker.login(accountObj);
        }
        LOG.info("afterConnectionEstablished" + hashUserId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        //String hashUserId = (String) session.getAttributes().get("hashUserId");
        String hashUserId = session.getAttributes().get("hashUserId").toString();
        LOG.info("afterConnectionClosed" + hashUserId);
        sessionMaps.remove(hashUserId);
        int sessionMapSize = sessionMaps.size();
        sessionMaps.put(hashUserId, session);
        if(sessionMapSize == 0) {
            AccountInvoker accountInvoker = new AniInvokerImpl(Constants.aniServiceSession);
            AccountObject accountObj = new AccountObject(Long.parseLong(hashUserId));
            SocketMessage socketMessage = accountInvoker.logout(accountObj);
        }
        SessionManager.removeSession(hashUserId, session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    /**
     * send message to the only user
     * @param hashUserId
     * @param strategyDto
     */
    public static void sendMessageToUser(Long hashUserId, StrategyDto strategyDto) {
        //WebSocketSession session = sessionMaps.get(hashUserId);
        Vector<WebSocketSession> sessionVector = SessionManager.getWebSocketSession(String.valueOf(hashUserId));
        Enumeration<WebSocketSession> sessionEnumeration = sessionVector.elements();
        while (sessionEnumeration.hasMoreElements()) {
            WebSocketSession session = sessionEnumeration.nextElement();
            if (session != null && session.isOpen()) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    StrategyFormDto strategyFormDto = StrategyFormDto.convertToStrategyForm(strategyDto);
                    String strategyInfoJson = mapper.writeValueAsString(strategyFormDto);
                    TextMessage message = new TextMessage(strategyInfoJson);
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendDeviceMessageToUser(Long hashUserId,DeviceDto deviceDto) {
        Vector<WebSocketSession> sessionVector = SessionManager.getWebSocketSession(String.valueOf(hashUserId));
        Enumeration<WebSocketSession> sessionEnumeration = sessionVector.elements();
        while (sessionEnumeration.hasMoreElements()) {
            WebSocketSession session = sessionEnumeration.nextElement();
            if (session != null && session.isOpen()) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    DeviceFormDto deviceFormDto = DeviceFormDto.convertToDeviceForm(deviceDto);
                    String deviceInfoJson = mapper.writeValueAsString(deviceFormDto);
                    TextMessage message = new TextMessage(deviceInfoJson);
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
