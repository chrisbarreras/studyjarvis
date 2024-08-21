package com.christophertbarrerasconsulting.studyjarvis.server;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class HandlerDecorator implements Handler {
    Handler decoratedHandler;
    public HandlerDecorator(Handler decoratedHandler) {
        this.decoratedHandler = decoratedHandler;
    }

    public static HandlerDecorator getInstance(Handler decoratedHandler){
        return new HandlerDecorator(decoratedHandler);
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        if (context.status() != HttpStatus.OK) return;

        decoratedHandler.handle(context);
    }
}
