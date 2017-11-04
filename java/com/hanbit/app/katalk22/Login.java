package com.hanbit.app.katalk22;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context ctx = Login.this;
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etID = findViewById(R.id.etID);
                EditText etPass = findViewById(R.id.etPass);
                final String inputID = etID.getText().toString();
                final String inputPass = etPass.getText().toString();
                Intro.SQLiteHelper helper = new Intro.SQLiteHelper(ctx);
                new Intro.LoginService(){

                    @Override
                    public void execute() {

                    }
                }.execute();
            }
        });
    }
   private class LoginQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context ctx) {
           super(ctx);
           helper = new Intro.SQLiteHelper(ctx);
       }

       @Override
       public SQLiteDatabase getDatabase() {
           return helper.getReadableDatabase();
       }
   }
   private class MemberExist extends LoginQuery{

       public MemberExist(Context ctx) {
           super(ctx);
       }
       public boolean execute(String seq,String pass){
           return super
                   .getDatabase()
                   .rawQuery(String.format(" SELECT * FROM %s WHERE %s = '%s' AND " +
                           " %s = '%s';", Intro.MEM_TABLE, Intro.MEM_SEQ, seq, Intro.MEM_PASS, pass),null)
                   .moveToNext();
       }
   }

}
