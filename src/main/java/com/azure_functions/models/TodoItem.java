package com.azure_functions.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = TodoItem.TodoItemBuilder.class)
public class TodoItem {

  /** ID */
  private int id;

  /** 内容 */
  private String content;

  /** 完了フラグ */
  private boolean done;

  /** 作成日時 */
  private LocalDateTime createdAt;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TodoItemBuilder {}
}
