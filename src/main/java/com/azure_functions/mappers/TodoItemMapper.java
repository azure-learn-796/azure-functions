package com.azure_functions.mappers;

import java.util.List;

import com.azure_functions.models.TodoItem;

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

}
