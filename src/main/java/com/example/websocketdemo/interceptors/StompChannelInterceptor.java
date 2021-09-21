package com.example.websocketdemo.interceptors;

import com.example.websocketdemo.controller.WebSocketEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.LinkedMultiValueMap;

public class StompChannelInterceptor implements ChannelInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(StompChannelInterceptor.class);
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String destination = "/exchange/metrics/test_metrics";
            MessageHeaderAccessor mutableAccessor = MessageHeaderAccessor.getMutableAccessor(message);
            mutableAccessor.setHeader("simpDestination", destination);
            ((LinkedMultiValueMap)mutableAccessor.getHeader("nativeHeaders")).set("destination", destination);
            return new GenericMessage<>(message.getPayload(), mutableAccessor.getMessageHeaders());
        }
        return message;
    }
}
