package com.bugtracker.common.dto;

import com.bugtracker.common.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Геттеры, Сеттеры, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private ActionType action; // команда (LOGIN, CREATE_TASK, GET_PROJECTS)
    private String data;   // данные в формате JSON
}