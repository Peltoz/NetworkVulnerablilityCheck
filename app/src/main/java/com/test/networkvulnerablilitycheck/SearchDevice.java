package com.test.networkvulnerablilitycheck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SearchDevice extends AppCompatActivity {

    TextView tvWifiState;
    TextView tvScanning;
    ArrayList<InetAddress> inetAddresses;
    ArrayList<String> items;
    ListView tvResult;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.iotcheck_activity);

        final Intent intent = new Intent(this, Progress.class);

        //tvWifiState = (TextView)findViewById(R.id.WifiState);
        tvScanning = (TextView)findViewById(R.id.Scanning);

        items = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);

        tvResult = (ListView)findViewById(R.id.Result);
        tvResult.setAdapter(adapter);

        //To prevent memory leaks on devices prior to Android N,
        //retrieve WifiManager with
        //getApplicationContext().getSystemService(Context.WIFI_SERVICE),
        //instead of getSystemService(Context.WIFI_SERVICE)
//        WifiManager wifiManager =
//                (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        tvWifiState.setText(readtvWifiState(wifiManager));

        new ScanTask(tvScanning, tvResult).execute();


        Button confButton = (Button)findViewById(R.id.confirm);

        confButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                SparseBooleanArray checkedItems = tvResult.getCheckedItemPositions();
                int count = adapter.getCount();

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        //test.setText(items.remove(i)) ;
                        //String [] ip
                    }
                }

                // 모든 선택 상태 초기화.
                tvResult.clearChoices() ;

                adapter.notifyDataSetChanged();

                startActivity(intent);
            }
        });

        Button cancButton = (Button)findViewById(R.id.cancel);

        cancButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }

    // "android.permission.ACCESS_WIFI_STATE" is needed
//    private String readtvWifiState(WifiManager wm){
//        String result = "";
//        switch (wm.getWifiState()){
//            case WifiManager.WIFI_STATE_DISABLED:
//                result = "WIFI_STATE_DISABLED";
//                break;
//            case WifiManager.WIFI_STATE_DISABLING:
//                result = "WIFI_STATE_DISABLING";
//                break;
//            case WifiManager.WIFI_STATE_ENABLED:
//                result = "WIFI_STATE_ENABLED";
//                break;
//            case WifiManager.WIFI_STATE_ENABLING:
//                result = "WIFI_STATE_ENABLING";
//                break;
//            case WifiManager.WIFI_STATE_UNKNOWN:
//                result = "WIFI_STATE_UNKNOWN";
//                break;
//            default:
//        }
//        return result;
//    }


    private class ScanTask extends AsyncTask<Void, String, Void> {

        TextView tvCurrentScanning;
        ListView tvScanResullt;
        ArrayList<String> canonicalHostNames;

        ProgressDialog asyncDialog = new ProgressDialog(
                SearchDevice.this);

        @Override
        protected void onPreExecute() {

            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("디바이스 검색중..");

            // show dialog
            asyncDialog.show();


            super.onPreExecute();
        }

        public ScanTask(TextView tvCurrentScanning, ListView tvScanResullt) {
            this.tvCurrentScanning = tvCurrentScanning;
            this.tvScanResullt = tvScanResullt;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tvCurrentScanning.setText("검사 할 디바이스를 선택해 주세요.");
           // tvScanResullt.setText("");
            //for(int i = 0; i < inetAddresses.size(); i++){
             //   tvScanResullt.append(canonicalHostNames.get(i) + "\n");
            //}
            for(int i = 0; i < inetAddresses.size(); i++) {
                items.add(canonicalHostNames.get(i));
            }

            adapter.notifyDataSetChanged();

            asyncDialog.dismiss();
            super.onPostExecute(aVoid);


        }

        @Override
        protected Void doInBackground(Void... voids) {
            scanInetAddresses();
            return null;

        }

//        @Override
//        protected void onProgressUpdate(String... values) {
//            tvCurrentScanning.setText(values[0]);
//        }

        private void scanInetAddresses(){
            //May be you have to adjust the timeout
            final int timeout = 500;

            if(inetAddresses == null){
                inetAddresses = new ArrayList<>();
            }
            inetAddresses.clear();

            if(canonicalHostNames == null){
                canonicalHostNames = new ArrayList<>();
            }
            canonicalHostNames.clear();

            //For demonstration, scan 192.168.1.xxx only
            byte[] ip = {(byte) 192, (byte) 168, (byte) 0, 0};
            for (int j = 0; j < 255; j++) {
                ip[3] = (byte) j;
                try {
                    //asyncDialog.setProgress(j * 30);

                    InetAddress checkAddress = InetAddress.getByAddress(ip);
                    publishProgress(checkAddress.getCanonicalHostName());
                    if (checkAddress.isReachable(timeout)) {
                        inetAddresses.add(checkAddress);
                        canonicalHostNames.add(checkAddress.getCanonicalHostName());
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    publishProgress(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    publishProgress(e.getMessage());
                }
            }
        }
    }

}