package com.nick.commands.sca;

public interface Response {
    int START_OK = 0x1;
    int START_FAILURE_ALREADY_STARTED = 0x2;
    int START_FAILURE_SYSTEM_ERR = 0x3;

    int STOP_OK = 0x4;
    int STOP_FAILURE_NOT_STARTED = 0x5;
    int STOP_FAILURE_SYSTEM_ERR = 0x6;

    int BAD_REQUEST = 0;
}
