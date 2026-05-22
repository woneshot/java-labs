package com.bugtracker.common.enums;

public enum ActionType {

    // аутентификация
    LOGIN,
    LOGOUT,

    // пользователи
    CREATE_USER,
    GET_ALL_USERS,
    UPDATE_USER,
    BLOCK_USER,
    UNBLOCK_USER,

    // проекты
    CREATE_PROJECT,
    UPDATE_PROJECT,
    GET_ALL_PROJECTS,
    GET_USER_PROJECTS,
    ADD_PROJECT_MEMBER,
    REMOVE_PROJECT_MEMBER,
    GET_PROJECT_MEMBERS,

    // задачи
    CREATE_TASK,
    UPDATE_TASK,
    CHANGE_TASK_STATUS,
    CHANGE_TASK_PRIORITY,
    ASSIGN_TASK,
    GET_PROJECT_TASKS,
    FILTER_TASKS,
    SEARCH_TASKS,

    // комментарии
    ADD_COMMENT,
    GET_TASK_COMMENTS,

    // теги
    ADD_TAG_TO_TASK,
    REMOVE_TAG_FROM_TASK,
    GET_ALL_TAGS,
    GET_TASK_TAGS,

    // история
    GET_TASK_HISTORY,

    // статистика
    GET_PROJECT_STATISTICS
}