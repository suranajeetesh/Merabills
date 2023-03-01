package com.example.merabills.core.ui;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Jeetesh Surana.
 */
public class BaseActivity extends AppCompatActivity {

    public void addReplaceFragment(int container, Fragment fragment, boolean addFragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addFragment) {
            transaction.add(container, fragment, fragment.getClass().getSimpleName());
        } else {
            transaction.replace(container, fragment, fragment.getClass().getSimpleName());
        }
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getTag());
        }
        hideKeyboard();
        if (!getSupportFragmentManager().isDestroyed()) {
            transaction.commit();
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
