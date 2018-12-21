package com.holygunner.cocktailsapp_test.tools;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

public abstract class FabVisibilityHelper {

    public static void setFabVisibility(@NonNull FloatingActionButton fab, boolean isFillExists){
        boolean visibility;

        if ((!fab.isShown()) && isFillExists){
            visibility = true;
        }   else if
                ((fab.isShown()) && !isFillExists){
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
