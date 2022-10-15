package com.azure_functions.services;

import com.azure_functions.db.SqlSessionManager;
import com.azure_functions.mappers.TodoItemMapper;
import com.azure_functions.models.TodoItem;
import com.microsoft.azure.functions.ExecutionContext;
import java.util.List;
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
    context.getLogger().info("fetchTodoItemsが呼び出されました。");

    // DBからTODO一覧取得
    List<TodoItem> todoItems = sqlSessionManager.transaction(
      sqlSession -> {
        TodoItemMapper todoItemMapper = sqlSession.getMapper(
          TodoItemMapper.class
        );
        return todoItemMapper.selectTodo();
      }
    );

    return todoItems;
  }

  /**
   * TODOを登録
   * @param todo TODOインスタンス
   * @return 登録件数
   */
  public int insertTodo(TodoItem todo) {
    context.getLogger().info("insertTodoが呼び出されました。");

    // TODOを登録
    int inserted = sqlSessionManager.transaction(
      sqlSession -> {
        TodoItemMapper todoItemMapper = sqlSession.getMapper(
          TodoItemMapper.class
        );
        return todoItemMapper.insertTodo(todo);
      }
    );

    return inserted;
  }

  /**
   * TODOを更新
   * @param todo TODOインスタンス
   * @return 更新件数
   */
  public int updateTodo(int id, TodoItem todo) {
    context.getLogger().info("updateTodoが呼び出されました。");

    // idをインスタンスに設定
    todo.setId(id);

    // TODOを更新
    int updated = sqlSessionManager.transaction(
      sqlSession -> {
        TodoItemMapper todoItemMapper = sqlSession.getMapper(
          TodoItemMapper.class
        );
        return todoItemMapper.updateTodo(todo);
      }
    );

    return updated;
  }

  /**
   * TODOを削除
   * @param id TODOのID
   * @return 削除件数
   */
  public int deleteTodo(int id) {
    context.getLogger().info("deleteTodoが呼び出されました。");

    // TODOを更新
    int deleted = sqlSessionManager.transaction(
      sqlSession -> {
        TodoItemMapper todoItemMapper = sqlSession.getMapper(
          TodoItemMapper.class
        );
        return todoItemMapper.deleteTodo(id);
      }
    );

    return deleted;
  }
}
