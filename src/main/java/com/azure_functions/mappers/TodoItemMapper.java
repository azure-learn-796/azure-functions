package com.azure_functions.mappers;

import com.azure_functions.models.TodoItem;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * TodoItemのマッパー
 */
public interface TodoItemMapper {
  /**
   * TODO一覧を全件取得
   *
   * @return TODO一覧
   */
  List<TodoItem> selectTodo();

  /**
   * TODOを登録
   *
   * @return 登録件数
   */
  int insertTodo(TodoItem todo);

  /**
   * TODOを更新
   *
   * @return 更新件数
   */
  int updateTodo(TodoItem todo);

  /**
   * TODOを削除
   *
   * @return 削除件数
   */
  int deleteTodo(@Param("id") int id);
}
