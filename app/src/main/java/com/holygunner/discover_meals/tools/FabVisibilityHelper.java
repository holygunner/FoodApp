package com.holygunner.discover_meals.tools;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

public abstract class FabVisibilityHelper {

    public static void setFabVisibility(@NonNull FloatingActionButton fab, boolean isFilled){
        boolean visibility;

        if ((!fab.isShown()) && isFilled){
            visibility = true;
        }   else if
                ((fab.isShown()) && !isFilled){
            visibility = false;
        }   else {
            return;
        }

        if (visibility){
            fab.show();
        }   else {
            fab.hide();
        }
    }
}
