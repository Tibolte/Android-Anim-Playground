package fr.northborders.AnimationPlayground.DynamicForm.Listeners;

import android.view.View;

/**
 * Created by thibaultguegan on 31/05/2014.
 */
public interface EditTextFocusListener {

    public void focusOn(View v, View movableView, boolean animated);

    public void focusOff(View v, View movableView, boolean animated);

}
