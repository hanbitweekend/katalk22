package com.hanbit.app.katalk22;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.hanbit.app.katalk22.Intro.MEM_ADDR;
import static com.hanbit.app.katalk22.Intro.MEM_EMAIL;
import static com.hanbit.app.katalk22.Intro.MEM_NAME;
import static com.hanbit.app.katalk22.Intro.MEM_PASS;
import static com.hanbit.app.katalk22.Intro.MEM_PHONE;
import static com.hanbit.app.katalk22.Intro.MEM_PHOTO;
import static com.hanbit.app.katalk22.Intro.MEM_SEQ;
import static com.hanbit.app.katalk22.Intro.MEM_TABLE;
import static com.hanbit.app.katalk22.Intro.Member;
import static com.hanbit.app.katalk22.Intro.ListService;
import static com.hanbit.app.katalk22.Intro.QueryFactory;
import static com.hanbit.app.katalk22.Intro.SQLiteHelper;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context ctx = MemberList.this;
        final ListView listView = findViewById(R.id.listView);
        final MemberItemList items = new MemberItemList(ctx);
        ArrayList<Member> memberList = (ArrayList<Member>)new ListService() {
            @Override
            public List<?> execute() {
                return items.list();
            }
        }.execute();
        listView.setAdapter(new MemberItem(ctx,memberList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Member selectedMember = (Member)listView.getItemAtPosition(i);
                Intent intent = new Intent(ctx,MemberDetail.class);
                intent.putExtra("seq",String.valueOf(selectedMember.seq));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Member selectedMember = (Member)listView.getItemAtPosition(i);
                new AlertDialog.Builder(ctx)
                        .setTitle("DELETE")
                        .setMessage("정말로 삭제합니까?")
                        .setPositiveButton(
                                android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final MemberItemDelete del = new MemberItemDelete(ctx);
                                        new Intro.DeleteService(){

                                            @Override
                                            public void execute() {
                                                del.execute(String.valueOf(selectedMember.seq));
                                            }
                                        }.execute();
                                        startActivity(new Intent(ctx, MemberList.class));
                                    }
                                }
                        )
                        .setNegativeButton(
                                android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }
                        )
                        .show();
                return true;
            }
        });
        findViewById(R.id.member_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx, MemberAdd.class));
            }
        });

    }
    private abstract class ListQuery extends QueryFactory{
        SQLiteOpenHelper helper;
        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberItemList extends ListQuery{

        public MemberItemList(Context ctx) {
            super(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }
        public ArrayList<Member> list(){
            ArrayList<Member> members = new ArrayList<>();
            String sql = String.format(" SELECT %s,%s,%s,%s,%s,%s,%s FROM %s ",
                    MEM_SEQ,MEM_NAME,MEM_PASS,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO,MEM_TABLE
            );
            Cursor cursor = this.getDatabase()
                    .rawQuery(sql,null);
            Member member = null;
            if(cursor != null){
                while(cursor.moveToNext()){
                    member = new Member();
                    member.seq =Integer.parseInt( cursor.getString(cursor.getColumnIndex(MEM_SEQ)));
                    member.name = cursor.getString(cursor.getColumnIndex(MEM_NAME));
                    member.pass = cursor.getString(cursor.getColumnIndex(MEM_PASS));
                    member.email = cursor.getString(cursor.getColumnIndex(MEM_EMAIL));
                    member.phone = cursor.getString(cursor.getColumnIndex(MEM_PHONE));
                    member.addr = cursor.getString(cursor.getColumnIndex(MEM_ADDR));
                    member.photo = cursor.getString(cursor.getColumnIndex(MEM_PHOTO));
                    members.add(member);
                }
            }
            return members;
        }





    }
    class MemberItem extends BaseAdapter{
        ArrayList<Member> list;
        LayoutInflater inflater;

        public MemberItem(Context ctx,ArrayList<Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(ctx);
        }
        private int[] photos = {
                R.drawable.cupcake,
                R.drawable.donut,
                R.drawable.eclair,
                R.drawable.froyo,
                R.drawable.gingerbread,
                R.drawable.honeycomb
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v = inflater.inflate(R.layout.member_item,null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            holder.photo.setImageResource(photos[i]);
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }
    static class ViewHolder{
        ImageView photo;
        TextView name;
        TextView phone;
    }
   // private abstract class DeleteQuery extends
    private abstract class DeleteQuery extends QueryFactory{
        SQLiteOpenHelper helper;
       public DeleteQuery(Context ctx) {
           super(ctx);
           helper = new SQLiteHelper(ctx);
        }

       @Override
       public SQLiteDatabase getDatabase() {
           return helper.getWritableDatabase();
       }
   }
   private class MemberItemDelete extends DeleteQuery{

       public MemberItemDelete(Context ctx) {
           super(ctx);
       }

       @Override
       public SQLiteDatabase getDatabase() {
           return super.getDatabase();
       }
       public void execute(String seq){
           String sql = String.format("DELETE FROM %s WHERE %s = '%s';"
                                    , MEM_TABLE, MEM_SEQ, seq
           );
           this.getDatabase().execSQL(sql);
           this.getDatabase().close();
       }
   }
}
