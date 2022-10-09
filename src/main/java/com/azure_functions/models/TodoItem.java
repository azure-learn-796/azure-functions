package com.azure_functions.models;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoItem {

    /** 内容 */
    private String content;

    /** 完了フラグ */
    private boolean done;

    /** 作成日時 */
    private LocalDateTime createdAt;

}
