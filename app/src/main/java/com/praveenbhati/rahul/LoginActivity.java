package com.praveenbhati.rahul;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtUserName, mEtPassword;
    private TextInputLayout inputLayoutName,  inputLayoutPassword;
    private Button mBtnLogin;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getIsLogin()){
            startActivity(new Intent(this,DashboardActivity.class));
            finish();
        }


        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        initXmlViews();
        setListener();

    }

    /**
     * Set Listener to xml views
     */
    private void setListener() {
        mBtnLogin.setOnClickListener(this);
        mEtUserName.addTextChangedListener(new MyTextWatcher(mEtUserName));
        mEtPassword.addTextChangedListener(new MyTextWatcher(mEtPassword));
    }

    /**
     * Initialize all xml controller
     */
    private void initXmlViews() {
        mEtUserName = (EditText) findViewById(R.id.content_login_et_username);
        mEtPassword = (EditText) findViewById(R.id.content_login_et_password);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        String username = mEtUserName.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        if (!validateUserName()){
            return;
        }

        if (!validatePassword()){
            return;
        }

        if (username.equals(Config.USERNAME) && password.equals(Config.PASSWORD)){
            preferenceManager.setKeyIsLogin();
            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }else {
            Toast.makeText(LoginActivity.this, "User name and password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyTextWatcher implements TextWatcher {


        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()){
                case R.id.content_login_et_username:
                    validateUserName();
                    break;
                case R.id.content_login_et_password:
                    validatePassword();
                    break;
            }
        }
    }

    private boolean validatePassword() {
        if (mEtPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(mEtPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateUserName() {
        if (mEtUserName.getText().toString().trim().isEmpty()){
            inputLayoutName.setError(getString(R.string.err_msg_username));
            requestFocus(mEtUserName);
            return false;
        }else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
