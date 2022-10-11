package com.azure_functions.services;

import java.util.List;

import com.azure_functions.db.SqlSessionManager;
import com.azure_functions.mappers.TodoItemMapper;
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

        /** SQLセッション管理 */
        private final SqlSessionManager sqlSessionManager;

        /**
         * TODO一覧を取得
         * 
         * @return TODO一覧
         */
        public List<TodoItem> fetchTodoItems() {
                context.getLogger().info("fetchTodoItems");

                // DBからTODO一覧取得
                List<TodoItem> todoItems = sqlSessionManager.transaction(sqlSession -> {
                        TodoItemMapper todoItemMapper = sqlSession.getMapper(TodoItemMapper.class);
                        return todoItemMapper.selectTodo();
                });

                return todoItems;
        }

}
