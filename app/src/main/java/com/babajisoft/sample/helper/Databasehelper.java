package com.babajisoft.sample.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.babajisoft.sample.dto.PersonInfoDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by babaji on 28/8/16.
 */

public class Databasehelper extends SQLiteOpenHelper
{
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "person_db";
    public final static String DATABASE_PATH ="/data/data/com.babajisoft.sample/databases/";
    public static final int DATABASE_VERSION = 1;
    //public static final int DATABASE_VERSION_old = 1;

    //Constructor
    public Databasehelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if(!dbExist1)
        {
            this.getReadableDatabase();
            try
            {
                this.close();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    public boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    //delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public void onCreate(SQLiteDatabase db)
    {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (newVersion > oldVersion)
        {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }

    public int getCount(){
        String countQuery = "SELECT  * FROM MyTable" ;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public ArrayList<PersonInfoDTO> getVoterInfo(){
        String countQuery = "SELECT  vno,sectionno,yadibhag,fullname,surname,Name,middle,sexage FROM MyTable LIMIT 100" ;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        ArrayList<PersonInfoDTO> allvotersinfo=new ArrayList<>();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                PersonInfoDTO personInfoDTO=new PersonInfoDTO();
                personInfoDTO.setVibhagNo(cursor.getInt(cursor.getColumnIndex("vno")));
                personInfoDTO.setFullName(cursor.getString(cursor.getColumnIndex("fullname")));
                personInfoDTO.setPartNo(cursor.getInt(cursor.getColumnIndex("yadibhag")));
                personInfoDTO.setAgeSex(cursor.getString(cursor.getColumnIndex("sexage")));
                personInfoDTO.setSectionNo(cursor.getInt(cursor.getColumnIndex("sectionno")));
                allvotersinfo.add(personInfoDTO);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return allvotersinfo;
    }

    public PersonInfoDTO getPerticularVoterInfo(int voterNo){
        String countQuery = "SELECT  familycode,vno,sectionno,_id,email,newadd,dob,alivedead,mobile,hno,yadibhag,cardno,fullname,surname,Name,middle,sexage FROM MyTable WHERE _id = '"+voterNo+"' LIMIT 100";
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        PersonInfoDTO allvotersinfo= new PersonInfoDTO();
        if (cursor .moveToFirst()) {
            allvotersinfo.setFamilycode(cursor.getInt(cursor.getColumnIndex("familycode")));
            allvotersinfo.setVibhagNo(cursor.getInt(cursor.getColumnIndex("vno")));
            allvotersinfo.setFullName(cursor.getString(cursor.getColumnIndex("fullname")));
            allvotersinfo.setPartNo(cursor.getInt(cursor.getColumnIndex("yadibhag")));
            allvotersinfo.setAgeSex(cursor.getString(cursor.getColumnIndex("sexage")));
            allvotersinfo.setVoterNo(cursor.getInt(cursor.getColumnIndex("_id")));
            allvotersinfo.setSectionNo(cursor.getInt(cursor.getColumnIndex("sectionno")));
            allvotersinfo.setNewAddr(cursor.getString(cursor.getColumnIndex("newadd")));
            allvotersinfo.setHno(cursor.getString(cursor.getColumnIndex("hno")));
            allvotersinfo.setMobileNo(cursor.getString(cursor.getColumnIndex("mobile")));
            allvotersinfo.setCardNo(cursor.getString(cursor.getColumnIndex("cardno")));
            allvotersinfo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            allvotersinfo.setDob(cursor.getString(cursor.getColumnIndex("dob")));
            allvotersinfo.setAliveDead(cursor.getString(cursor.getColumnIndex("alivedead")));
        }
        cursor.close();
        return allvotersinfo;
    }

    public String getAddress(int sectionno){
        String countQuery = "SELECT  address FROM partdetail WHERE sectionno="+sectionno;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        String Address="";
        if (cursor.moveToFirst()) {
            Address=cursor.getString(cursor.getColumnIndex("address"));
        }
        cursor.close();
        return Address;
    }

    public boolean isValidUser(String userid, String password){
        boolean flage=false;
        String countQuery = "SELECT  * FROM usertable WHERE username ='"+userid+"' and password ='"+password+"'";
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            flage = true;
        }
        cursor.close();
        return flage;
    }

    public String getBooth(int sectionno){

        String countQuery = "SELECT  booth FROM booth WHERE yadibhag="+sectionno;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        String booth="";
        if (cursor.moveToFirst()) {
            booth=cursor.getString(cursor.getColumnIndex("booth"));
        }
        cursor.close();
        return booth;
    }
    public ArrayList<PersonInfoDTO> getSearchedVotersInfo(String lastName,String name,String middle){
        String countQuery = "SELECT  familycode,vno,voting,sectionno,newadd,_id,email,dob,alivedead,mobile,hno,yadibhag,cardno,fullname,surname,Name,middle,sexage FROM MyTable WHERE surname like '"+lastName+"%' and name like '"+name+"%' and middle like '"+middle+"%' LIMIT 100";
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        ArrayList<PersonInfoDTO> allvotersinfo=new ArrayList<>();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                PersonInfoDTO personInfoDTO=new PersonInfoDTO();
                personInfoDTO.setFamilycode(cursor.getInt(cursor.getColumnIndex("familycode")));
                personInfoDTO.setVibhagNo(cursor.getInt(cursor.getColumnIndex("vno")));
                personInfoDTO.setFullName(cursor.getString(cursor.getColumnIndex("fullname")));
                personInfoDTO.setPartNo(cursor.getInt(cursor.getColumnIndex("yadibhag")));
                personInfoDTO.setAgeSex(cursor.getString(cursor.getColumnIndex("sexage")));
                personInfoDTO.setVoterNo(cursor.getInt(cursor.getColumnIndex("_id")));
                personInfoDTO.setSectionNo(cursor.getInt(cursor.getColumnIndex("sectionno")));
                personInfoDTO.setHno(cursor.getString(cursor.getColumnIndex("hno")));
                personInfoDTO.setNewAddr(cursor.getString(cursor.getColumnIndex("newadd")));
                personInfoDTO.setMobileNo(cursor.getString(cursor.getColumnIndex("mobile")));
                personInfoDTO.setCardNo(cursor.getString(cursor.getColumnIndex("cardno")));
                personInfoDTO.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                personInfoDTO.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                personInfoDTO.setAliveDead(cursor.getString(cursor.getColumnIndex("alivedead")));
                personInfoDTO.setVotedone(cursor.getInt(cursor.getColumnIndex("voting")));
                allvotersinfo.add(personInfoDTO);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return allvotersinfo;
    }

    public void updateRecord(ArrayList<PersonInfoDTO> personinfo){


        for(int i=0; i<personinfo.size();i++){
            String countQuery = "";
            PersonInfoDTO person = personinfo.get(i);
            try {
                ContentValues row = new ContentValues();
                if (isAvailable(personinfo.get(i).getId())) {
                    row.put("familycode", person.getFamilycode() == 0 ? 0 : person.getFamilycode());
                    row.put("cardno"    , person.getCardNo() == null ? "" : person.getCardNo());
                    row.put("hno",        person.getHno() == null ? "" : person.getHno());
                    row.put("sectionno"     , person.getSectionNo() == 0 ? 0 : person.getSectionNo());
                    row.put("email",         person.getEmail() == null ? "" : person.getEmail());
                    row.put("dob",          person.getDob() == null ? "" : person.getDob());
                    row.put("alivedead"     , person.getAliveDead() == null ? "" : person.getAliveDead());
                    row.put("vno", person.getVoterNo() == 0 ? 0 : person.getVoterNo());
                    row.put("yadibhag", person.getPartNo() == 0 ? 0 : person.getPartNo());
                    row.put("voting", person.getVotedone() == 0 ? 0 : person.getVotedone());
                    row.put("newadd", person.getNewAddr() == null ? "" : person.getNewAddr());
                    row.put("redgreen", 0);
                    row.put("jat", person.getJat() == null ? "" : person.getJat());
                    row.put("mobile", person.getMobileNo() == null ? "" : person.getMobileNo());
                    row.put("_id", person.getId() == 0 ? 0 : person.getId());
                    row.put("fullname", person.getFullName() == null ? "" : person.getFullName());
                    row.put("surname", person.getLastName() == null ? "" : person.getLastName());
                    row.put("Name", person.getFirstName() == null ? "" : person.getFirstName());
                    row.put("middle", person.getMiddleName() == null ? "" : person.getMiddleName());
                    row.put("sexage", person.getAgeSex() == null ? "" : person.getAgeSex());
                    //long id = myDataBase.insert("MyTable", null, row);
                    myDataBase.update("MyTable", row, "_id = ?", new String[]{String.valueOf(person.getId())});

                    //  countQuery = "UPDATE MyTable SET familycode="+person.getFamilycode()+",vno="+person.getVoterNo()+",fullname='"+person.getFullName()+"',yadibhag="+person.getPartNo()+",sexage='"+person.getAgeSex()+"', sectionno = "+person.getSectionNo()+", hno = '"+person.getHno()+", mobile = '"+person.getMobileNo()+", cardno = '"+person.getCardNo()+", email = '"+person.getEmail()+", dob = '"+person.getDob()+", alivedead = '"+person.getAliveDead()+", voting = "+person.getVotedone()+",newadd='"+person.getNewAddr()+"',jat='"+person.getJat()+"', surname = '"+person.getLastName()+", name = '"+person.getFirstName()+"', middle = '"+person.getMiddleName()+"  WHERE _id="+person.getId();
                } else {
                    row.put("familycode", person.getFamilycode() == 0 ? 0 : person.getFamilycode());
                    row.put("cardno", person.getCardNo() == null ? "" : person.getCardNo());
                    row.put("hno", person.getHno() == null ? "" : person.getHno());
                    row.put("sectionno", person.getSectionNo() == 0 ? 0 : person.getSectionNo());
                    row.put("email", person.getEmail() == null ? "" : person.getEmail());
                    row.put("dob", person.getDob() == null ? "" : person.getDob());
                    row.put("alivedead", person.getAliveDead() == null ? "" : person.getAliveDead());
                    row.put("vno", person.getVoterNo() == 0 ? 0 : person.getVoterNo());
                    row.put("yadibhag", person.getPartNo() == 0 ? 0 : person.getPartNo());
                    row.put("voting", person.getVotedone() == 0 ? 0 : person.getVotedone());
                    row.put("newadd", person.getNewAddr() == null ? "" : person.getNewAddr());
                    row.put("redgreen", 0);
                    row.put("jat", person.getJat() == null ? "" : person.getJat());
                    row.put("mobile", person.getMobileNo() == null ? "" : person.getMobileNo());
                    row.put("_id", person.getId() == 0 ? 0 : person.getId());
                    row.put("fullname", person.getFullName() == null ? "" : person.getFullName());
                    row.put("surname", person.getLastName() == null ? "" : person.getLastName());
                    row.put("Name", person.getFirstName() == null ? "" : person.getFirstName());
                    row.put("middle", person.getMiddleName() == null ? "" : person.getMiddleName());
                    row.put("sexage", person.getAgeSex() == null ? "" : person.getAgeSex());
                    long id = myDataBase.insert("MyTable", null, row);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    boolean isAvailable(int id){
        boolean flag=false;
        String countQuery = "SELECT  * FROM MyTable WHERE _id ="+id;
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            flag = true;
        }
        cursor.close();
        return flag;
    }

    public ArrayList<PersonInfoDTO> getFamilyInfobyVoter(PersonInfoDTO voterInfo){
        String countQuery = "SELECT  familycode,vno,sectionno,voting,_id,email,dob,alivedead,mobile,hno,yadibhag,cardno,fullname,surname,Name,middle,sexage FROM MyTable WHERE familycode = '"+voterInfo.getFamilycode()+"' LIMIT 100";
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        ArrayList<PersonInfoDTO> allvotersinfo=new ArrayList<>();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                PersonInfoDTO personInfoDTO=new PersonInfoDTO();
                personInfoDTO.setFamilycode(cursor.getInt(cursor.getColumnIndex("familycode")));
                personInfoDTO.setVibhagNo(cursor.getInt(cursor.getColumnIndex("vno")));
                personInfoDTO.setFullName(cursor.getString(cursor.getColumnIndex("fullname")));
                personInfoDTO.setPartNo(cursor.getInt(cursor.getColumnIndex("yadibhag")));
                personInfoDTO.setAgeSex(cursor.getString(cursor.getColumnIndex("sexage")));
                personInfoDTO.setVoterNo(cursor.getInt(cursor.getColumnIndex("_id")));
                personInfoDTO.setSectionNo(cursor.getInt(cursor.getColumnIndex("sectionno")));
                personInfoDTO.setHno(cursor.getString(cursor.getColumnIndex("hno")));
                personInfoDTO.setMobileNo(cursor.getString(cursor.getColumnIndex("mobile")));
                personInfoDTO.setCardNo(cursor.getString(cursor.getColumnIndex("cardno")));
                personInfoDTO.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                personInfoDTO.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                personInfoDTO.setAliveDead(cursor.getString(cursor.getColumnIndex("alivedead")));
                personInfoDTO.setVotedone(cursor.getInt(cursor.getColumnIndex("voting")));
                allvotersinfo.add(personInfoDTO);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return allvotersinfo;
    }

    public int deleteRecords(){
        String countQuery = "delete from MyTable where vno > 300001 and vno <317100";
        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public void UpdatePersonDetails(String mobile,String email,String dob, String alivedead, String newAddr, boolean isAddresChange, int id){
        //  UPDATE MyTable SET mobile='9730970036',email='abcd@gmail.com',dob='11-12-1993',alivedead='Alive' WHERE _id=2
        ContentValues row = new ContentValues();
        row.put("mobile", mobile);
        row.put("email", email);
        row.put("dob", dob);
        row.put("alivedead", alivedead);
        row.put("isUpdated", 1);
        if(isAddresChange) {
            row.put("newadd", newAddr);
        }
        myDataBase.update("MyTable", row, "_id = ?", new String[]{String.valueOf(id)});
    }



/*
    public void UpdateVoteDone(int id,int vstatus){
        //  UPDATE MyTable SET mobile='9730970036',email='abcd@gmail.com',dob='11-12-1993',alivedead='Alive' WHERE _id=2
        String countQuery = "UPDATE MyTable SET voting="+vstatus+" WHERE _id="+id;
        Cursor cursor=null;
        try{
            cursor = myDataBase.rawQuery(countQuery, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cursor !=null)
           cursor.close();
    }
*/

    public void UpdateVoteDone(int id, int status) {

        ContentValues row = new ContentValues();
        row.put("voting", status);
        row.put("isUpdated", 1);

        myDataBase.update("MyTable", row, "_id = ?", new String[] { String.valueOf(id) });
        //myDataBase.close();
    }
    public void UpdateFlag() {

        ContentValues row = new ContentValues();
        row.put("isUpdated", 0);
        myDataBase.update("MyTable", row, "isUpdated = ?", new String[] { String.valueOf("1") });
        //myDataBase.close();
    }
    public ArrayList<PersonInfoDTO> getVotingDoneVotersInfo(int startvalue,int fromvalue,int frompage){
        String countQuery;
        if(frompage==0)
         countQuery = "SELECT familycode,vno,voting,sectionno,_id,email,dob,alivedead,mobile,hno,yadibhag,cardno,fullname,surname,Name,middle,sexage FROM MyTable WHERE yadibhag BETWEEN "+startvalue+" AND "+fromvalue+" AND voting=1";
        else
         countQuery = "SELECT  familycode,vno,voting,sectionno,_id,email,dob,alivedead,mobile,hno,yadibhag,cardno,fullname,surname,Name,middle,sexage FROM MyTable WHERE yadibhag BETWEEN "+startvalue+" AND "+fromvalue+" AND (ifnull(length(voting), 0) = 0 OR voting=0)";

        Cursor cursor = myDataBase.rawQuery(countQuery, null);
        ArrayList<PersonInfoDTO> allvotersinfo=new ArrayList<>();
        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                PersonInfoDTO personInfoDTO=new PersonInfoDTO();
                personInfoDTO.setFamilycode(cursor.getInt(cursor.getColumnIndex("familycode")));
                personInfoDTO.setVibhagNo(cursor.getInt(cursor.getColumnIndex("vno")));
                personInfoDTO.setFullName(cursor.getString(cursor.getColumnIndex("fullname")));
                personInfoDTO.setPartNo(cursor.getInt(cursor.getColumnIndex("yadibhag")));
                personInfoDTO.setAgeSex(cursor.getString(cursor.getColumnIndex("sexage")));
                personInfoDTO.setVoterNo(cursor.getInt(cursor.getColumnIndex("_id")));
                personInfoDTO.setSectionNo(cursor.getInt(cursor.getColumnIndex("sectionno")));
                personInfoDTO.setHno(cursor.getString(cursor.getColumnIndex("hno")));
                personInfoDTO.setMobileNo(cursor.getString(cursor.getColumnIndex("mobile")));
                personInfoDTO.setCardNo(cursor.getString(cursor.getColumnIndex("cardno")));
                personInfoDTO.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                personInfoDTO.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                personInfoDTO.setAliveDead(cursor.getString(cursor.getColumnIndex("alivedead")));
                personInfoDTO.setVotedone(cursor.getInt(cursor.getColumnIndex("voting")));
                personInfoDTO.setNewAddr(cursor.getString(cursor.getColumnIndex("newadd")).trim());
                allvotersinfo.add(personInfoDTO);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return allvotersinfo;
    }


        public JSONArray getJsonFromLocal() {
            Cursor cursor = myDataBase.rawQuery("SELECT * FROM MyTable WHERE isUpdated=1", null);
            JSONArray resultSet = new JSONArray();
            cursor.moveToFirst();
            JSONObject jsonObject=new JSONObject();
            while (cursor.isAfterLast() == false) {
                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();
                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            rowObject.put(cursor.getColumnName(i),
                                    cursor.getString(i));
                        } catch (Exception e) {
                            Log.d("JSON", e.getMessage());
                        }
                    }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }

            cursor.close();

            return resultSet;

        }
}
