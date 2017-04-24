package com.example.zabik.vkLikeCounter;

import android.accounts.Account;
import android.app.*;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zabik on 19.03.17.
 */

public class ParsePhoto extends android.app.Application {
    private int id;
    int []userId;
    DB DbHelper;
    private int sizeOfdb;
    public ParsePhoto(DB database){
        DbHelper = database;
    }

    public int[] getUsersWhichLikePhoto(int photoId){
        final VKRequest request = new VKRequest("likes.getList", VKParameters.from("item_id","330813474","type","photo",VKApiConst.OWNER_ID,"26240273",VKApiConst.FILTERS,"likes",VKApiConst.EXTENDED,"true",VKApiConst.COUNT,"1000"));
        request.attempts = 10;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                int counterOfuser = 0;
                try {
                    JSONObject object = new JSONObject(response.responseString);
                    JSONObject responseObject = object.getJSONObject("response");
                    JSONArray arrayOfUsersLiked = responseObject.getJSONArray("items");
                    int []tmpUserId = new int[500];
                    for(int i = 0; i < arrayOfUsersLiked.length();++i){
                        final String tmp = arrayOfUsersLiked.getJSONObject(i).getString("id");
                        addUserToDb(Integer.valueOf(tmp));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
        return userId;

    }
    public int[] getLikes(int photoId){
        return getUsersWhichLikePhoto(photoId);

    }
    public void setArray(int[] tmpUserId, DB DbHelper){
        userId = tmpUserId;
        return;
    }
    public void addUserToDb(int userId){
        SQLiteDatabase dataB = DbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        boolean flag = false;
        String sel = "userid = ?";
        String[] args = new String[]{String.valueOf(userId)};
        Cursor cur = dataB.query(DbHelper.TABLE_NAME,null,sel,args,null,null,null);
        if(cur.moveToFirst()){
            flag = true;
        }
        if(flag == true){
            contentValues.put(
                    DB.KEY_USER_ID,
                    cur.getInt(cur.getColumnIndex(DbHelper.KEY_USER_ID))
            );
            int tmp = cur.getInt(cur.getColumnIndex(DbHelper.KEY_COUNT_LIKE));
            tmp++;
            contentValues.put(DB.KEY_COUNT_LIKE,tmp);
            int tmp2 = cur.getInt(cur.getColumnIndex(DbHelper.KEY_ID));
            dataB.update(DbHelper.TABLE_NAME,contentValues,DbHelper.KEY_ID + "= ?", new String[]{String.valueOf(tmp2)});
            return;
        }



        contentValues.put(DB.KEY_USER_ID,userId);
        contentValues.put(DB.KEY_COUNT_LIKE, 1);
        dataB.insert(DbHelper.TABLE_NAME,null,contentValues);
    }
    public void deleteTable(){
        SQLiteDatabase dataB = DbHelper.getReadableDatabase();
        dataB.delete(DbHelper.TABLE_NAME,null,null);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showDB(TextView textView){
        SQLiteDatabase dataB = DbHelper.getReadableDatabase();
        Cursor cursor = dataB.query(DbHelper.TABLE_NAME,null,null,null,null,null,null);
        textView.setText("");
        textView.setTextColor(Color.BLACK);
        sizeOfdb = cursor.getCount();
        if(cursor.moveToFirst()){
            int idx = cursor.getColumnIndex(DbHelper.KEY_ID);
            int userIdx = cursor.getColumnIndex(DbHelper.KEY_USER_ID);
            int likeIdx = cursor.getColumnIndex(DbHelper.KEY_COUNT_LIKE);
            do{
                textView.setText(textView.getText() + "id= " + cursor.getInt(idx) +
                        "user id= " + cursor.getString(userIdx) +
                        "like count= " + cursor.getInt(likeIdx) +" "+
                        System.lineSeparator());
            }while (cursor.moveToNext());
        }
        cursor.close();

    }
    public int getSize(){
        int size = sizeOfdb;
        return size;

    }


}
