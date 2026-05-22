package com.bugtracker.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private boolean success; // успех или ошбика
    private String message;  // сообщение (например, "Неверный пароль" или "Задача создана")
    private String data;     // данные ответа в JSON
}