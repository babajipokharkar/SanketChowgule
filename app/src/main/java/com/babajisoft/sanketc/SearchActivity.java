package com.babajisoft.sanketc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.babajisoft.sanketc.adapter.VotersAdapter;
import com.babajisoft.sanketc.dto.PersonInfoDTO;
import com.babajisoft.sanketc.helper.Databasehelper;
import com.babajisoft.sanketc.helper.SendSmsHelper;
import com.babajisoft.sanketc.helper.ToastHelper;
import com.babajisoft.sanketc.sdk.Command;
import com.babajisoft.sanketc.sdk.PrintPicture;
import com.babajisoft.sanketc.sdk.PrinterCommand;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
//import com.google.android.gms.analytics.internal.Command;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import zj.com.customize.sdk.Other;

import static android.support.v7.app.AlertDialog.*;

public class SearchActivity extends AppCompatActivity {
    ImageButton searchButton, editButton, btnsms, btncall, btnfamily;
    EditText input_lastname, input_layout_name, input_layout_middlename;
    String lastName, name, middleName;
    Databasehelper myDbHelper;
    ListView listView;
    private BluetoothAdapter mBluetoothAdapter = null;
    public static Bitmap bitmapImage;

    ArrayList<PersonInfoDTO> votersSearchinfo;
    PersonInfoDTO selectedperson;
    ImageButton btninfo;
    VotersAdapter votersAdapter;
    Tracker mTracker;
    private static final int REQUEST_CONNECT_DEVICE = 1;  //��ȡ�豸��Ϣ
    private static final int REQUEST_EDIT_INFO = 111;
    private static final int REQUEST_ENABLE_BT = 2;
    View dialoglayout;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;


    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = (ListView) findViewById(R.id.listView);
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
       // mService = new BluetoothService(this, mHandler);
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        Log.i("MainActivity", "Enter in MainActivity: " + name);
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());

        searchButton = (ImageButton) findViewById(R.id.searchButton);
        btncall = (ImageButton) findViewById(R.id.btncall);
        btnsms = (ImageButton) findViewById(R.id.btnsms);
        input_lastname = (EditText) findViewById(R.id.input_lastname);
        input_layout_name = (EditText) findViewById(R.id.input_name);
        editButton = (ImageButton) findViewById(R.id.btnedit);
        btnfamily = (ImageButton) findViewById(R.id.btnfamily);
        input_layout_middlename = (EditText) findViewById(R.id.middle_name);
        btninfo = (ImageButton) findViewById(R.id.btninfo);
        getSupportActionBar().setHomeButtonEnabled(true);
      //  mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /*if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        }*/
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedperson != null) {
                    Intent i = new Intent(SearchActivity.this, EditDetailsActivity.class);
                    i.putExtra("fullname", selectedperson.getFullName());
                    i.putExtra("voterno", selectedperson.getVoterNo());
                    i.putExtra("mobile", selectedperson.getMobileNo());
                    i.putExtra("email", selectedperson.getEmail());
                    i.putExtra("redgreen", selectedperson.getRedGreen());
                    String address;
                    if(selectedperson.getNewAddr() == null || selectedperson.getNewAddr().equalsIgnoreCase("")){
                        address = myDbHelper.getAddress(selectedperson.getSectionNo());
                    }else{
                        address = selectedperson.getNewAddr();
                    }
                    i.putExtra("address", address);
                    i.putExtra("dob", selectedperson.getDob());
                    i.putExtra("aliveDead", selectedperson.getAliveDead());
                    i.putExtra("sectionNo",selectedperson.getSectionNo());
                    i.putExtra("yadibhagNo", selectedperson.getPartNo());
                    startActivityForResult(i, REQUEST_EDIT_INFO, null);
                }
            }
        });
        myDbHelper = new Databasehelper(this);
        try {
            myDbHelper.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        //    System.out.println("Data count from VoterInfo**********"+myDbHelper.getVoterInfo().size());
        //final ArrayList<PersonInfoDTO> voterinfo=myDbHelper.getVoterInfo();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedperson = null;
                lastName = input_lastname.getText().toString() == null ? "" : input_lastname.getText().toString();
                name = input_layout_name.getText().toString() == null ? "" : input_layout_name.getText().toString();
                middleName = input_layout_middlename.getText().toString() == null ? "" : input_layout_middlename.getText().toString();
                if (lastName.equalsIgnoreCase("") && name.equalsIgnoreCase("") && middleName.equalsIgnoreCase("")) {
                    ToastHelper.showToast(SearchActivity.this, "Please enter Name", Toast.LENGTH_LONG);
                } else {
                   new GetSerchedResult().execute();
                }
            }
        });
        btnfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedperson != null) {
                   // votersSearchinfo = myDbHelper.getFamilyInfobyVoter(selectedperson);
                    ArrayList<PersonInfoDTO> votersSearchinfoTemp = new ArrayList<PersonInfoDTO>();
                    votersSearchinfo.clear();
                    votersSearchinfoTemp = myDbHelper.getFamilyInfobyVoter(selectedperson);
                    boolean flag = false;
                    int tempId = 0;
                    for(int i=0;i<votersSearchinfoTemp.size();i++){
                        if(flag){
                            if(votersSearchinfoTemp.get(i).getVoterNo() == tempId){
                                votersSearchinfo.add(votersSearchinfoTemp.get(i));
                                tempId = tempId + 1;
                            }else{
                                flag = false;
                            }
                        }
                        if(selectedperson.getVoterNo() == votersSearchinfoTemp.get(i).getVoterNo()){
                            votersSearchinfo.add(votersSearchinfoTemp.get(i));
                            tempId = votersSearchinfoTemp.get(i).getVoterNo() + 1;
                            flag = true;
                        }

                    }



                    if (votersSearchinfo != null) {
                        votersAdapter = new VotersAdapter(SearchActivity.this, R.layout.votorsinfolist_item, votersSearchinfo);
                        listView.invalidate();
                        listView.setAdapter(votersAdapter);
                    }
                } else {
                    ToastHelper.showToast(SearchActivity.this, "Please select Name", Toast.LENGTH_LONG);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedperson = votersSearchinfo.get(position);
                // When clicked, show a toast with the TextView text
/*                Toast.makeText(getApplicationContext(), selectedperson.getFullName(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });
        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedperson != null) {
                    if (isValidMobile(selectedperson.getMobileNo())) {
                        call(selectedperson.getMobileNo());
                    } else {
                        ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                    }
                } else {
                    ToastHelper.showToast(getApplicationContext(), "Please Select person", Toast.LENGTH_SHORT);
                }
            }
        });
        btnsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedperson != null) {
                    if (isValidMobile(selectedperson.getMobileNo())) {
                     //   final String address = myDbHelper.getAddress(selectedperson.getSectionNo());
                        final String booth = myDbHelper.getBooth(selectedperson.getPartNo());

                            sendSMS(booth);

//                        SendSmsHelper.sendSMS(SearchActivity.this, selectedperson.getMobileNo(), "" + selectedperson.getFullName());

                    } else {
                        ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                    }
                } else {
                    ToastHelper.showToast(getApplicationContext(), "Please Select person", Toast.LENGTH_SHORT);
                }
            }
        });
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedperson != null) {
                    LayoutInflater inflater = getLayoutInflater();
                    dialoglayout = inflater.inflate(R.layout.person_details_view, null);
                    TextView txtpartno = (TextView) dialoglayout.findViewById(R.id.txtpartno);
                    TextView txtFullname = (TextView) dialoglayout.findViewById(R.id.txtname);
                    TextView txtvoterno = (TextView) dialoglayout.findViewById(R.id.txtvoterno);
                    TextView txthno = (TextView) dialoglayout.findViewById(R.id.txthno);
                    TextView txtaddress = (TextView) dialoglayout.findViewById(R.id.txtaddress);
                    TextView txtBooth = (TextView) dialoglayout.findViewById(R.id.txtBooth);
                    TextView txtMo = (TextView) dialoglayout.findViewById(R.id.txtMo);
                    TextView txtId = (TextView) dialoglayout.findViewById(R.id.txtId);
                    String address;
                    if(selectedperson.getNewAddr()==null ||selectedperson.getNewAddr().equals("") ) {
                        address = myDbHelper.getAddress(selectedperson.getSectionNo());
                    }else{
                        address=selectedperson.getNewAddr();
                    }
                    final String booth = myDbHelper.getBooth(selectedperson.getPartNo());
                    txtFullname.setText(selectedperson.getFullName());
                    txtvoterno.setText(selectedperson.getVibhagNo() + "");
                    txtpartno.setText(selectedperson.getPartNo() + "");
                    txthno.setText(selectedperson.getHno() + "");
                    txtMo.setText(selectedperson.getMobileNo() + "");
                    txtBooth.setText(booth + "");
                    txtId.setText(selectedperson.getCardNo() + "");
                    txtaddress.setText(address);
                    Builder builder = new Builder(SearchActivity.this);
                    builder.setView(dialoglayout).setPositiveButton("Send SMS", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (isValidMobile(selectedperson.getMobileNo())) {
                                sendSMS(booth);
                               /* SendSmsHelper.sendSMS(SearchActivity.this, selectedperson.getMobileNo(),selectedperson.getFullName()
                                        + "\n" + booth
                                        + "\n CNo:" + selectedperson.getCardNo()
                                        + "\n PNo:" + selectedperson.getPartNo()
                                        + "\n SrNo:" + selectedperson.getId());
*/
                            } else {
                                ToastHelper.showToast(getApplicationContext(), "Please Check mobile number", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                    builder.setView(dialoglayout).setNegativeButton("PRINT", new OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent i=new Intent(SearchActivity.this,Main_Activity.class);
                            dialoglayout.setDrawingCacheEnabled(true);
                            i.putExtra("partno",selectedperson.getPartNo()+"");
                            i.putExtra("srno",selectedperson.getVibhagNo()+"");
                            i.putExtra("name",selectedperson.getFullName());
                            i.putExtra("booth",booth);
                            i.putExtra("cardno",selectedperson.getCardNo());
                            /*Bitmap bitmap = dialoglayout.getDrawingCache();
                            BitmapDrawable drawable = new BitmapDrawable(bitmap);*/

                           // LinearLayout view = (LinearLayout )dialoglayout.findViewById(R.id.ovterinfoprint);


                            bitmapImage=getViewBitmap(dialoglayout);
                           // i.putExtra("imagebitmap",bitmap);
                            startActivity(i);
                           //Bitmap imagebit = loadBitmapFromView(dialoglayout);

                        /*        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                            }else {*/
                           /*     dialoglayout.setDrawingCacheEnabled(true);
                                Bitmap bitmap = dialoglayout.getDrawingCache();
                                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                                Intent serverIntent = new Intent(SearchActivity.this, DeviceListActivity.class);
                                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);*/
                           // }
                        }
                    });
                    builder.show();
                    Toast.makeText(getApplicationContext(), selectedperson.getFullName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Selecte Person", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // http://developer.android.com/intl/zh-TW/resources/tutorials/vi

/*
        VotersAdapter votersAdapter=new VotersAdapter(this,R.layout.votorsinfolist_item,voterinfo);
        listView.setAdapter(votersAdapter);*/


    }
    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
        view.draw(canvas);

        return returnedBitmap;
    }
/*

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
           // if (mService == null)
               // KeyListenerInit();//监听
        }
    }
*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_EDIT_INFO:      //���������
                    if (selectedperson != null) {
                    int itemIndex = votersSearchinfo.indexOf(selectedperson);
                    int voterNo = selectedperson.getVoterNo();
                    selectedperson = myDbHelper.getPerticularVoterInfo(voterNo);
                    votersSearchinfo.set(itemIndex, selectedperson);
                    }
                    break;
               /* case REQUEST_ENABLE_BT:      //���������
                    if (resultCode == Activity.RESULT_OK) {   //�����Ѿ���
                        Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                    } else {                 //�û������������
                        Toast.makeText(this, "Please Start Bluetooth..", Toast.LENGTH_LONG).show();
                    }
                    break;
                case  REQUEST_CONNECT_DEVICE:     //��������ĳһ�����豸
                    if (resultCode == Activity.RESULT_OK) {   //�ѵ�������б��е�ĳ���豸��
                        // Get the device MAC address
                        String address = data.getExtras().getString(
                                DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        // Get the BLuetoothDevice object
                        if (BluetoothAdapter.checkBluetoothAddress(address)) {
                            BluetoothDevice device = mBluetoothAdapter
                                    .getRemoteDevice(address);
                            // Attempt to connect to the device
                            mService.connect(device);

                        }

                    }
                    break;*/
            }
        } catch (Exception e) {

        }
    }
    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("IMAGE", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }
    private void GraphicalPrint(){

            Bitmap bm1 = getImageFromAssetsFile("demo.jpg");
                Bitmap bmp = Other.createAppIconText(bm1,"messsage",25,true,200);
                int nMode = 0;
                int nPaperWidth = 384;
                if(bmp != null)
                {
                    byte[] data = PrintPicture.POS_PrintBMP(bmp, nPaperWidth, nMode);
                    SendDataByte(Command.ESC_Init);
                    SendDataByte(Command.LF);
                    SendDataByte(data);
                    SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
                    SendDataByte(PrinterCommand.POS_Set_Cut(1));
                    SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }

    private void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mService.write(data);
    }


    private static boolean isValidMobile(String mobile) {
        return !TextUtils.isEmpty(mobile) && Patterns.PHONE.matcher(mobile).matches();
    }
    public static Bitmap loadBitmapFromView(View v) {
        System.out.print("width*******"+v.getLayoutParams().width );
        System.out.print("height*******"+v.getLayoutParams().height );
        Bitmap b = Bitmap.createBitmap(
                v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    /**
     * ����һ��Handlerʵ�������ڽ���BluetoothService�෵�ػ�������Ϣ
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:

                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:

                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //  mTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            // mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    // mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected ",
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();

                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    private void call(String mobile) {
        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        try {
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
       // SendSmsHelper.unRegisterReceiver(SearchActivity.this);
    }
    ProgressDialog progressDialog;

    private class GetSerchedResult extends AsyncTask<ArrayList<PersonInfoDTO>, Void, ArrayList<PersonInfoDTO>> {
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(SearchActivity.this);
            progressDialog.setMessage("Please wait a moment...");
            progressDialog.show();
        }
        @Override
        protected ArrayList<PersonInfoDTO> doInBackground(ArrayList<PersonInfoDTO>... params) {
          return votersSearchinfo = myDbHelper.getSearchedVotersInfoNew(lastName, name, middleName);
        }

        @Override
        protected void onPostExecute(ArrayList<PersonInfoDTO> result) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            if (result != null) {
                if (result != null) {
                    votersAdapter = new VotersAdapter(SearchActivity.this, R.layout.votorsinfolist_item, result);
                    listView.setAdapter(votersAdapter);
                }
            }
        }
    }


public void sendSMS(String booth){
    SendSmsHelper.sendSMS(SearchActivity.this, selectedperson.getMobileNo(),selectedperson.getFullName()
            + "\n" + booth
            + "\n CNo:" + selectedperson.getCardNo()
            + "\n PNo:" + selectedperson.getPartNo()
            + "\n SrNo:" + selectedperson.getSectionNo());
}
}

