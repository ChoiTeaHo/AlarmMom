package com.example.layup.alarmmom.useprogram;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

/**
 * Created by layup on 2018-12-16.
 */

public class DatabaseSQLite {

    static Cursor cu = null;

    static SQLiteDatabase sqliteDB;

    static int hours, minutes;
    private int id;
    static boolean powerMode;


    //생성자
    public DatabaseSQLite(Context context){


        // Database 초기화
        sqliteDB = init_database(context);

        // Table 초기화
        init_tables();

        // Table 내용값 로드
        load_values();
    }


    private SQLiteDatabase init_database(Context context){
        SQLiteDatabase db = null;
        File file = new File(context.getFilesDir(), "contact.db");

        System.out.println("System.out.println( DB초기화 파일 경로 PATH ) : " + file.toString());
        try{
            db = SQLiteDatabase.openOrCreateDatabase(file, null);    //openOrCreateDatabase( 파일이 없으면 새로 생성하여 DB를 연다.)
        }catch (SQLException e){

            e.printStackTrace();
        }

        if(db == null){
            System.out.println("DB creation failed. " + file.getAbsolutePath());
        }
        return db;
    }


    // Table 생성
    private void init_tables(){

        // 최초 DB초기화시에 File이 새로 생성되어 Table 이 없는 경우.
        if ( sqliteDB != null){
            Log.d("TAG", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 테이블 생성중 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS CONTACT_T (" +
                    "_id "           + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "hour "           + "INTEGER, " +
                    "minutes "           + "INTEGER" +  ");";

            System.out.println("System.out.println(테이블초기화): "+sqlCreateTbl);
            sqliteDB.execSQL(sqlCreateTbl); // execSQL() 을 쏴주면 Table 이 생성.
        }
    }

    //현재 등록된 전체 Table 검색하여 load
    private void load_values() {
        if( sqliteDB != null) {
            Log.d("TAG", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 테이블 등록된 내용  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            String sqlQueryTbl = "SELECT * FROM CONTACT_T";
            Cursor cursor = null;

            //쿼리실행 ( sqlQueryTbl 테이블전체 조회한 레코드셋쿼리함. )
            cursor = sqliteDB.rawQuery(sqlQueryTbl, null);

            // cursor.getPosition() 하면 첨엔 -1을 반환하므로 이걸사용. 다음 데이터 줄로 이동하여 유(true)/무(false)를 반환
            if (cursor.moveToNext()){

                // no (Integer) 값 가져오기.
                int id = cursor.getInt(0);
                int hour = cursor.getInt(1);
                int minutes = cursor.getInt(2);
                Log.d("TAG", "읽은 _id 값: " + id +
                            "읽은 hour 값: " + hour +
                            "읽은 minutes 값: " + minutes);
            }

            if(id >= 1){
                setPowerMode(true);
            }else{
                setPowerMode(false);
            }

            }
    }


    //데이터 저장하기
    public void save_values(int hours, int minutes){
        if( sqliteDB != null ){

            Log.d("TAG", "################################SqlLite Save메소드   " + "시: " + hours + " 분: " + minutes);

            this.id = id;
            this.hours = hours;
            this.minutes = minutes;

            //우선 Table의 데이터를 모두삭제.
            sqliteDB.execSQL("DELETE FROM CONTACT_T");


            Log.d("TAG", "SqlLite 콘텍트T");
            String sqlInsert = "INSERT INTO CONTACT_T (hour, minutes) VALUES (" +
                    Integer.toString(this.hours) + "," +
                    Integer.toString(this.minutes) +
                    ")";

            System.out.println(sqlInsert);
            sqliteDB.execSQL(sqlInsert);

        }
    }

    // 테이터 삭제하기
    public void delete_values() {
        if (sqliteDB != null) {
            String sqlDelete = "DELETE FROM CONTACT_T";
            sqliteDB.execSQL(sqlDelete);

        }
    }

    // Setter
    public static void setPowerMode(boolean powerMode) {

        DatabaseSQLite.powerMode = powerMode;
    }

    // Getter
    public static boolean getPowerMode() {
        String sqlQueryTbl = "SELECT * FROM CONTACT_T";
        cu = sqliteDB.rawQuery(sqlQueryTbl, null);
        // cursor.getPosition() 하면 첨엔 -1을 반환하므로 이걸사용. 다음 데이터 줄로 이동하여 유(true)/무(false)를 반환
        if (  cu.moveToNext() == true) {
            return true;
        }else{
            return false;
        }
    }

 

}
