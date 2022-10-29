package com.azure_functions;

import com.azure.core.util.UrlBuilder;
import com.azure_functions.db.SqlSessionManager;
import com.azure_functions.models.TodoItem;
import com.azure_functions.services.TodoService;
import com.azure_functions.utils.JsonUtil;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

  /**
   * TODO一覧を取得
   *
   * @param request リクエスト
   * @param context 実行コンテキスト
   * @return レスポンス
   */
  @FunctionName("fetchTodoItems")
  public HttpResponseMessage fetchTodoList(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.GET },
      // 承認レベルをFUNCTIONに変更（APIキーが必要となる）
      authLevel = AuthorizationLevel.FUNCTION,
      // エンドポイントを「/api/todos」に設定
      route = "todos"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("fetchTodoItemsが呼び出されました。");

    // TODO一覧取得
    var sqlSessionManager = new SqlSessionManager(context);
    var todoService = new TodoService(context, sqlSessionManager);
    List<TodoItem> todoItems = todoService.fetchTodoItems();

    // TODO一覧をJSONに変換
    var jsonUtil = new JsonUtil(context);
    String json = jsonUtil.serialize(todoItems);

    return request
      .createResponseBuilder(HttpStatus.OK)
      .header("Content-Type", "application/json;")
      .body(json)
      .build();
  }

  /**
   * TODOを登録
   *
   * @param request リクエスト
   * @param context 実行コンテキスト
   * @return レスポンス
   */
  @FunctionName("createTodoItem")
  public HttpResponseMessage createTodoItem(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.FUNCTION,
      // エンドポイントを「/api/todos」に設定
      route = "todos"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("createTodoItemが呼び出されました。");

    TodoItem todo = getBodyData(request, context, TodoItem.class);

    // TODO登録
    var sqlSessionManager = new SqlSessionManager(context);
    var todoService = new TodoService(context, sqlSessionManager);
    todoService.insertTodo(todo);

    // TODO一覧をJSONに変換
    var jsonUtil = new JsonUtil(context);
    String json = jsonUtil.serialize(todo);

    return request
      .createResponseBuilder(HttpStatus.CREATED)
      .header("Content-Type", "application/json;")
      .header("Location", expandUriPath(request.getUri(), todo.getId()))
      .body(json)
      .build();
  }

  /**
   * TODOを更新
   *
   * @param request リクエスト
   * @param context 実行コンテキスト
   * @return レスポンス
   */
  @FunctionName("updateTodoItem")
  public HttpResponseMessage updateTodoItem(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.PATCH },
      authLevel = AuthorizationLevel.FUNCTION,
      // エンドポイントを「/api/todos/<id>」に設定
      route = "todos/{id:int}"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context,
    // パスのidをバインド
    @BindingName("id") int id
  ) {
    context.getLogger().info("updateTodoItemが呼び出されました。");

    TodoItem todo = getBodyData(request, context, TodoItem.class);

    // TODO更新
    var sqlSessionManager = new SqlSessionManager(context);
    var todoService = new TodoService(context, sqlSessionManager);
    todoService.updateTodo(id, todo);

    return request.createResponseBuilder(HttpStatus.NO_CONTENT).build();
  }

  /**
   * TODOを削除
   *
   * @param request リクエスト
   * @param context 実行コンテキスト
   * @return レスポンス
   */
  @FunctionName("deleteTodoItem")
  public HttpResponseMessage deleteTodoItem(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.DELETE },
      authLevel = AuthorizationLevel.FUNCTION,
      // エンドポイントを「/api/todos/<id>」に設定
      route = "todos/{id:int}"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context,
    // パスのidをバインド
    @BindingName("id") int id
  ) {
    context.getLogger().info("deleteTodoItemが呼び出されました。");

    // TODO削除
    var sqlSessionManager = new SqlSessionManager(context);
    var todoService = new TodoService(context, sqlSessionManager);
    todoService.deleteTodo(id);

    return request.createResponseBuilder(HttpStatus.NO_CONTENT).build();
  }

  /**
   * リクエストボディを読み取り、指定した型に変換
   * @param <T> データの型
   * @param request リクエスト
   * @param context 実行コンテキスト
   * @param type データの型
   * @return ボディデータ
   */
  private static <T> T getBodyData(
    HttpRequestMessage<Optional<String>> request,
    ExecutionContext context,
    Class<T> type
  ) {
    // リクエストボディを取得(取得できなかったらエラー)
    String json = request.getBody().orElse("");
    if (json.isBlank()) {
      String error = "リクエスト内容が取得できませんでした。";
      context.getLogger().severe(error);
      throw new RuntimeException(error);
    }

    // JSONをオブジェクトに変換
    var jsonUtil = new JsonUtil(context);
    T obj = jsonUtil.deserialize(json, type);
    System.out.println(obj);

    return obj;
  }

  private static String expandUriPath(URI uri, Object... paths) {
    String expandedPath = Stream
      .concat(
        Stream.of(uri.getPath().replaceFirst("/$", "")),
        Arrays.stream(paths).map(Objects::toString)
      )
      .filter(Objects::nonNull)
      .collect(Collectors.joining("/"));

    return UrlBuilder.parse(uri.toString()).setPath(expandedPath).toString();
  }
}
