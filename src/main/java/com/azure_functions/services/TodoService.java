package com.azure_functions.services;

import java.time.LocalDateTime;
import java.util.List;
import com.azure_functions.models.TodoItem;
import com.microsoft.azure.functions.ExecutionContext;

import lombok.RequiredArgsConstructor;

/**
 * TODOサービス
 */
@RequiredArgsConstructor
public class TodoService {

        /** 実行コンテキスト */
        private final ExecutionContext context;

        /**
         * TODO一覧を取得
         * 
         * @return TODO一覧
         */
        public List<TodoItem> fetchTodoItems() {
                context.getLogger().info("fetchTodoItems");
                // ダミーデータ生成
                List<TodoItem> todoItems = List.of(
                                TodoItem.builder()
                                                .content("Azureの勉強")
                                                .done(false)
                                                .createdAt(LocalDateTime.now())
                                                .build(),
                                TodoItem.builder()
                                                .content("Javaの勉強")
                                                .done(true)
                                                .createdAt(LocalDateTime.now())
                                                .build(),
                                TodoItem.builder()
                                                .content("TypeScriptの勉強")
                                                .done(false)
                                                .createdAt(LocalDateTime.now())
                                                .build());

                return todoItems;
        }

}
