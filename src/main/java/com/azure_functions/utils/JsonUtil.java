package com.azure_functions.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.microsoft.azure.functions.ExecutionContext;

import lombok.RequiredArgsConstructor;

/**
 * JSON関連のユーティリティ
 */
@RequiredArgsConstructor
public class JsonUtil {

    /** JSONシリアライズ用のマッパー */
    private static final ObjectMapper MAPPER = objectMapper();

    /** 実行コンテキスト */
    private final ExecutionContext context;

    /**
     * オブジェクトをJSONに変換
     * 
     * @param <T> オブジェクトの型
     * @param obj オブジェクト
     * @return JSON文字列
     */
    public <T> String serialize(T obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            context.getLogger().severe("オブジェクト→JSONの変換に失敗しました。エラーメッセージ：" + e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * JSONからオブジェクトに変換
     * 
     * @param <T>  オブジェクトの型
     * @param json JSON文字列
     * @param type オブジェクトの型
     * @return オブジェクト
     */
    public <T> T deserialize(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            context.getLogger().severe("JSON→オブジェクトの変換に失敗しました。エラーメッセージ：" + e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private static ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();

        // LocalDateTime用のシリアライザを追加
        var javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")));
        mapper.registerModule(javaTimeModule);

        return mapper;
    }
}
