package com.example.merabills.view.activities;


import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.merabills.R;
import com.example.merabills.core.ui.BaseActivity;
import com.example.merabills.view.fragments.AddPaymentFragment;

public class MainActivity extends BaseActivity {
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addReplaceFragment(R.id.main_container, new AddPaymentFragment(), false, false);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}