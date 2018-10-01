package com.dusten.paint.helpers;

import com.sun.istack.internal.NotNull;
import javafx.scene.control.MenuItem;

public interface SaveStatus {

    void setUpdateStatusCall(@NotNull MenuItem saveMenu, @NotNull MenuItem saveAsMenu,
                             @NotNull MenuItem undoMenu, @NotNull MenuItem redoMenu);
    StatusSet<Boolean> getUpdateStatusCall();
}
