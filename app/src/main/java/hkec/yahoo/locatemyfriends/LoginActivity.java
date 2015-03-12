package hkec.yahoo.locatemyfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends ActionBarActivity {

    private Button confirmButton;
    private EditText idInput;
    private TextView errorMsg;
    private String loginId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setElements();
    }

    private void setElements() {
        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        idInput = (EditText) findViewById(R.id.idInput);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
    }

    private boolean verifyId(String input) {
        if (input.length() == 0) {
            return false;
        }

        return true;
    }

    private void checkLogin() {
        // hide keyboard
        Util.hideKeyBoard(this);

        loginId = idInput.getText().toString();
        if (!verifyId(loginId)) {
            errorMsg.setVisibility(View.VISIBLE);
        } else {
            errorMsg.setVisibility(View.INVISIBLE);
            doLogin();
        }
    }

    private void doLogin() {
        // call api
        // create user model
        createUserModel(loginId);
        // bind sucket
        // swap to main page
        Log.d("myLog", "login as : " + loginId);
        Util.hideKeyBoard(this);
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createUserModel(String id) {
        UserProfile u = new UserProfile(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
