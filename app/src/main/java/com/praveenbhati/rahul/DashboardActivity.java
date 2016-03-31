package com.praveenbhati.rahul;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;

public class DashboardActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    PreferenceManager preferenceManager;
    SwitchCompat switchCompatOne, switchCompatTwo, switchCompatThree, switchCompatFour;
    String serverIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initXmlController();
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            WifiInfo connectionInfo = wifi.getConnectionInfo();
            DhcpInfo dhcpInfo = wifi.getDhcpInfo();
            serverIP = android.text.format.Formatter.formatIpAddress(dhcpInfo.serverAddress);
            Log.i("Server IP",serverIP);
            Log.i("Connection Info", connectionInfo.toString());
            Log.i("Dhcp Info", dhcpInfo.toString());
        } else {
            showMessage("Wifi is off");
        }
    }

    private void initXmlController() {
        switchCompatOne = (SwitchCompat) findViewById(R.id.light1_switch);
        switchCompatTwo = (SwitchCompat) findViewById(R.id.light2_switch);
        switchCompatThree = (SwitchCompat) findViewById(R.id.light3_switch);
        switchCompatFour = (SwitchCompat) findViewById(R.id.light4_switch);
        switchCompatOne.setOnCheckedChangeListener(this);
        switchCompatTwo.setOnCheckedChangeListener(this);
        switchCompatThree.setOnCheckedChangeListener(this);
        switchCompatFour.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                break;

           /* case R.id.action_about:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;*/


        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        preferenceManager.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.light1_switch:
                if (isChecked) {
                    new SocketAsync(serverIP,Config.LIGHT1_ON_CMD).execute();
                    showMessage("Light One is On");
                } else {
                    new SocketAsync(serverIP,Config.LIGHT1_OFF_CMD).execute();
                    showMessage("Light One is Off");
                }
                break;
            case R.id.light2_switch:
                if (isChecked) {
                    new SocketAsync(serverIP,Config.LIGHT2_ON_CMD).execute();
                    showMessage("Light Two is On");
                } else {
                    new SocketAsync(serverIP,Config.LIGHT2_OFF_CMD).execute();
                    showMessage("Light Two is Off");
                }
                break;
            case R.id.light3_switch:
                if (isChecked) {
                    new SocketAsync(serverIP,Config.LIGHT3_ON_CMD).execute();
                    showMessage("Light Three is On");
                } else {
                    new SocketAsync(serverIP,Config.LIGHT3_OFF_CMD).execute();
                    showMessage("Light Three is Off");
                }
                break;
            case R.id.light4_switch:
                if (isChecked) {
                    new SocketAsync(serverIP,Config.LIGHT4_ON_CMD).execute();
                    showMessage("Light Four is On");
                } else {
                    new SocketAsync(serverIP,Config.LIGHT4_OFF_CMD).execute();
                    showMessage("Light Four is Off");
                }
                break;

        }
    }

    private void showMessage(String msg) {
        Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private class SocketAsync extends AsyncTask<Void, Void, Void> {

        String serverIP,cmd;

        public SocketAsync(String serverIP, String cmd) {
            this.serverIP = serverIP;
            this.cmd = cmd;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Socket s= new Socket(InetAddress.getByName(serverIP), Config.PORT);
                DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
                dataOutputStream.writeUTF(cmd);

                /*DataInputStream in = new DataInputStream(s.getInputStream());
                StringBuilder sb=new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String read;
                while((read=br.readLine()) != null) {
                    //System.out.println(read);
                    sb.append(read);
                    break;
                }
                String result = sb.toString();
                Log.i("Server says ", result);*/
                dataOutputStream.flush();
                dataOutputStream.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

}
}
