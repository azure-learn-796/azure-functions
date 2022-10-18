package com.azure_functions.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoItem {

  /** ID */
  private int id;

  /** 内容 */
  private String content;

  /** 完了フラグ */
  private boolean done;

  /** 作成日時 */
  private LocalDateTime createdAt;
}
