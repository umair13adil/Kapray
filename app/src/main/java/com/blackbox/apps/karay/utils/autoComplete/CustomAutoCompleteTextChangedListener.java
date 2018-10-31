package com.blackbox.apps.karay.utils.autoComplete;

import android.text.Editable;
import android.text.TextWatcher;

import com.blackbox.apps.karay.models.rxbus.UserInput;
import com.michaelflisar.rxbus2.RxBus;

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CustomAuto";

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        RxBus.get().withSendToSuperClasses(true).send(new UserInput(userInput.toString()));
    }
}
