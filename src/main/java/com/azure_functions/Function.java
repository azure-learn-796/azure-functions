package com.azure_functions;

import com.azure_functions.models.TodoItem;
import com.azure_functions.services.TodoService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.List;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    /** TODOサービス */
    public TodoService todoService = new TodoService();

    /**
     * TODO一覧を取得
     * 
     * @param request リクエスト
     * @param context 実行コンテキスト
     * @return レスポンス
     */
    @FunctionName("fetchTodoList")
    public HttpResponseMessage fetchTodoList(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS,
                    // エンドポイントを「/api/todo/list」に設定
                    route = "todo/list") HttpRequestMessage<Optional<TodoItem>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // TODO一覧取得
        List<TodoItem> todoList = todoService.fetchTodoList();

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(todoList).build();

    }

}
