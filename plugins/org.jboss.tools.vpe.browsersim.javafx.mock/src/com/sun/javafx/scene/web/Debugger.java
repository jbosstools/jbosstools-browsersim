package com.sun.javafx.scene.web;

import javafx.util.Callback;

public interface Debugger {
        public void setEnabled(boolean b);
        public void sendMessage(String string);
        public void setMessageCallback(Callback<?, ?> callback);
}