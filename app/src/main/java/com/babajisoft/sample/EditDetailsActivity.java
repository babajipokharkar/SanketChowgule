package com.babajisoft.sample;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.babajisoft.sample.dto.PersonInfoDTO;
import com.babajisoft.sample.helper.Databasehelper;
import com.babajisoft.sample.helper.ToastHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class EditDetailsActivity extends AppCompatActivity {
    TextView txtfullname,voterNo,birthDate;
    EditText edtMobile,inputEmail,address;
    Button saveDetails;
    Databasehelper myDbHelper ;
    String mobile;
    String email,aliveDead,addresss,regreen;
    int votorno;
    Spinner dropdown,colordropdown;

    String dob;
    private int mYear;
    private int year1;
    private int mMonth;
    private int mDay;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        txtfullname=(TextView)findViewById(R.id.txtfullname);
        voterNo=(TextView)findViewById(R.id.voterNo);
        ActionBar actionBar=getSupportActionBar();
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        edtMobile=(EditText)findViewById(R.id.mobile);
        birthDate=(TextView)findViewById(R.id.birthDate);
        inputEmail=(EditText)findViewById(R.id.inputEmail);
        address=(EditText)findViewById(R.id.address);
        saveDetails=(Button)findViewById(R.id.saveDetails);
        myDbHelper = new Databasehelper(this);
        try {
            myDbHelper.openDatabase();
        }catch(SQLException sqle){

            throw sqle;
        }
     /*   ArrayList<Object> object = (ArrayList<Object>) getIntent().getExtras().getSerializable("ARRAYLIST");
        PersonInfoDTO info= (PersonInfoDTO) object.get(0);*/
  /*      i.putExtra("fullname", selectedperson.getFullName());
        i.putExtra("voterno", selectedperson.getVoterNo());
        i.putExtra("mobile", selectedperson.getMobileNo());
        i.putExtra("email", selectedperson.getEmail());
        i.putExtra("dob", selectedperson.getDob());
        i.putExtra("aliveDead", selectedperson.getAliveDead());*/
        Intent i=getIntent();
        String fullname= i.getExtras().getString("fullname")==null ? "" : i.getExtras().getString("fullname");
        votorno= i.getExtras().getInt("voterno");
        mobile = i.getExtras().getString("mobile") ==null ? "" :i.getExtras().getString("mobile");
        regreen = i.getExtras().getString("redgreen") ==null ? "" :i.getExtras().getString("redgreen");
         email  = i.getExtras().getString("email") ==null ? "" :i.getExtras().getString("email");
         dob   = i.getExtras().getString("dob") ==null ? "" : i.getExtras().getString("dob") ;
         aliveDead=i.getExtras().getString("aliveDead") ==null ? "" : i.getExtras().getString("aliveDead");
         addresss=i.getExtras().getString("address") ==null ? "" : i.getExtras().getString("address");
        txtfullname.setText(fullname);
        edtMobile.setText(mobile);
        voterNo.setText(votorno+"");
        birthDate.setText(dob+"");
        inputEmail.setText(email);
        address.setText(addresss);
         dropdown = (Spinner)findViewById(R.id.spinner1);
        colordropdown= (Spinner)findViewById(R.id.colors);
        String[] items = new String[]{"Alive", "Dead"};
        String[] colors = new String[]{"","A", "B","C", "D","E", "F","G"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colordropdown.setAdapter(adapter1);
        if(!aliveDead.equalsIgnoreCase("")){
            if(aliveDead.equalsIgnoreCase("Alive")) {
                dropdown.setSelection(0);
            }else{
                dropdown.setSelection(1);
            }
        }
        if(!regreen.equalsIgnoreCase("")){
            if(regreen.equalsIgnoreCase("A"))
                 colordropdown.setSelection(1);
            if(regreen.equalsIgnoreCase("B"))
                 colordropdown.setSelection(2);
            if(regreen.equalsIgnoreCase("C"))
                 colordropdown.setSelection(3);
            if(regreen.equalsIgnoreCase("D"))
                 colordropdown.setSelection(4);
            if(regreen.equalsIgnoreCase("E"))
                 colordropdown.setSelection(5);
            if(regreen.equalsIgnoreCase("F"))
                 colordropdown.setSelection(6);
            if(regreen.equalsIgnoreCase("G"))
                 colordropdown.setSelection(7);

        }else{
            colordropdown.setSelection(0);

        }

        birthDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("hello3");
                showDialog(DATE_DIALOG_ID);

            }
        });
        System.out.println("hello1");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR)-18;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //updateDisplay();
        year1 = mYear;
        month = mMonth;
        day = mDay;




        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!isValidEmail(inputEmail.getText().toString())) {
                inputEmail.setError("Please enter valid Email");
            }else if(!validCellPhone(edtMobile.getText().toString())){
                edtMobile.setError("Please enter valid mobile");
            }else {
                /*myDbHelper.UpdatePersonDetails(edtMobile.getText().toString(), inputEmail.getText().toString(), birthDate.getText().toString(), dropdown.getSelectedItem().toString(), votorno);
               */
                if(addresss.trim().equals(address.getText().toString().trim())){
                    myDbHelper.UpdatePersonDetails(edtMobile.getText().toString(), inputEmail.getText().toString(), birthDate.getText().toString(), dropdown.getSelectedItem().toString(), address.getText().toString().trim(), false, votorno,colordropdown.getSelectedItem().toString());
                }else{
                    myDbHelper.UpdatePersonDetails(edtMobile.getText().toString(), inputEmail.getText().toString(), birthDate.getText().toString(), dropdown.getSelectedItem().toString(), address.getText().toString().trim(),true, votorno,colordropdown.getSelectedItem().toString());
                }
                finish();
            }

            }
        });
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog _date = null;
        switch (id) {

            case DATE_DIALOG_ID:
                _date = new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay) {
                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        System.out.println("----------onDateChanged()-----------"
                                + mYear + "--" + year);
                        System.out.println("----------onDateChanged()-----------"
                                + mMonth + "--" + monthOfYear);
                        System.out.println("----------onDateChanged()-----------"
                                + mDay + "--" + dayOfMonth);

                    /*
                     * These lines of commented code used for only setting the
                     * maximum date on Date Picker..
                     *
                     * if (year > mYear && year) view.updateDate(mYear, mMonth,
                     * mDay);
                     *
                     * if (monthOfYear > mMonth && year == mYear )
                     * view.updateDate(mYear, mMonth, mDay);
                     *
                     * if (dayOfMonth > mDay && year == mYear && monthOfYear ==
                     * mMonth) view.updateDate(mYear, mMonth, mDay);
                     */

                        // these below lines of code used for setting the maximum as
                        // well as minimum dates on Date Picker Dialog..

                        if ((mYear < year)
                                || ((mMonth > monthOfYear) && (mYear == year))
                                || ((mDay > dayOfMonth) && (mYear == year) && (mMonth == monthOfYear))) {
                            view.updateDate(year1, month, day);

                        }

                    }
                };

        }
        System.out.println("hello5");
        return _date;
    }

    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {

            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

    private void updateDisplay() {
        birthDate.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mDay).append("-")
                .append(mMonth + 1).append("-")
                .append(mYear).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            System.out.println("hello7");

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            System.out.println("year=" + year);
            System.out.println("month=" + monthOfYear);
            System.out.println("day=" + dayOfMonth);
            updateDisplay();
        }
    };
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null || target.equals("")) {
            return true;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public boolean validCellPhone(String number)
    {
        if(number.length() == 10 ||number.length() == 0){
            return true;
        }else {
            return false;
        }

    }
}
