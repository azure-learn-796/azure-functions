package com.azure_functions.services;

import java.util.List;
import com.azure_functions.models.TodoItem;

/**
 * TODOサービス
 */
public class TodoService {

    /**
     * TODO一覧を取得
     * 
     * @return TODO一覧
     */
    public List<TodoItem> fetchTodoList() {
        // ダミーデータ生成
        List<TodoItem> todoList = List.of(
                TodoItem.builder()
                        .content("Azureの勉強")
                        .done(false)
                        .build(),
                TodoItem.builder()
                        .content("Javaの勉強")
                        .done(true)
                        .build(),
                TodoItem.builder()
                        .content("TypeScriptの勉強")
                        .done(false)
                        .build());

        return todoList;
    }

}
