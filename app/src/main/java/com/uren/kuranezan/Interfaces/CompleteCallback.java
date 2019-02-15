package com.uren.kuranezan.Interfaces;

public interface CompleteCallback<T> {
    void onComplete(T object);
    void onFailed(Exception e);
}
