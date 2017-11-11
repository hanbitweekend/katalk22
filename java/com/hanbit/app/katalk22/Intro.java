package com.hanbit.app.katalk22;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        final Context ctx = Intro.this;
        SQLiteHelper helper = new SQLiteHelper(ctx);
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx,Login.class));
            }
        });
        findViewById(R.id.join_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    static String DB_NAME = "hanbit.db";
    static String MEM_TABLE = "Member";
    static String MEM_SEQ = "seq";
    static String MEM_NAME = "name";
    static String MEM_PASS = "pass";
    static String MEM_EMAIL = "email";
    static String MEM_PHONE = "phone";
    static String MEM_ADDR = "addr";
    static String MEM_PHOTO = "photo";
    static class Member{int seq;String name,pass,email,phone,addr,photo;};
    static interface LoginService{public void execute();}
    static interface AddService{public void execute();}
    static interface ListService{public List<?> execute();}
    static interface DetailService{public Object execute();}
    static interface UpdateService{public void execute();}
    static interface DeleteService{public void execute();}
    static abstract class QueryFactory{
        Context ctx;
        public QueryFactory(Context ctx) {this.ctx = ctx;}
        public abstract SQLiteDatabase getDatabase();
    }
    static class SQLiteHelper extends SQLiteOpenHelper{

        public SQLiteHelper(Context ctx) {
            super(ctx, DB_NAME, null, 1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format("  CREATE TABLE IF NOT EXISTS %s  " +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT," +
                    "  %s TEXT );",
                    MEM_TABLE, MEM_SEQ,MEM_NAME,MEM_PASS,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO
                    ));
            /*for(int i=0;i<5;i++){
                db.execSQL(String.format(" INSERT INTO %s (%s,%s,%s,%s,%s,%s) " +
                        " VALUES ('%s','%s','%s','%s','%s','%s');",
                        MEM_TABLE, MEM_NAME,MEM_PASS,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO,
                        "홍길동"+i,"1","hong"+i+"@test.com","010-1234-567"+i,"서울"+i,"hong"+i));
            }*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }


}
