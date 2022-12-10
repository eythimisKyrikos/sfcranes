package com.safeline.safelinecranes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Resource<T> {
    @NotNull public final Status status;
    @Nullable public final T data;
    @Nullable public final String message;

    public enum Status  { SUCCESS, ERROR, LOADING, FORBIDDEN}

    private Resource(@NotNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NotNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> forbidden(String msg, @Nullable T data) {
        return new Resource<>(Status.FORBIDDEN, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }
}

