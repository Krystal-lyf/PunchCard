package com.example.punchcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.punchcard.bean.Energy;
import com.example.punchcard.utils.EnergyDBUtils;

public class EnergyStatisticsHelper extends SQLiteOpenHelper {

    private static final String[] columns = {
            EnergyDBUtils._ID,
            EnergyDBUtils.ENERGY,
            EnergyDBUtils.DATE,
            EnergyDBUtils.LOGIN,
            EnergyDBUtils.PUNCH_CARD
    };
    private SQLiteDatabase energyDatabase;

    public EnergyStatisticsHelper(Context context) {
        super(context, EnergyDBUtils.DATABASE_NAME, null, EnergyDBUtils.DATABASE_VERSION);
        energyDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EnergyDBUtils.DATABASE_TABLE + " ("
                + EnergyDBUtils._ID + " integer primary key autoincrement,"
                +EnergyDBUtils.ENERGY + " integer,"
                +EnergyDBUtils.DATE + " text,"
                +EnergyDBUtils.LOGIN  + " integer,"
                +EnergyDBUtils.PUNCH_CARD + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Energy GatherEnergy(Energy energy){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EnergyDBUtils.ENERGY,energy.getEnergy());
        contentValues.put(EnergyDBUtils.DATE,energy.getDate());
        contentValues.put(EnergyDBUtils.LOGIN,energy.getLogin());
        contentValues.put(EnergyDBUtils.PUNCH_CARD,energy.getPunchCard());
        long insertId = energyDatabase.insert(EnergyDBUtils.DATABASE_TABLE,null,contentValues);
        energy.setId((int)insertId);
        return energy;
    }

    public Energy getEnergy( int id){
        Cursor cursor = energyDatabase.query(EnergyDBUtils.DATABASE_TABLE,columns,EnergyDBUtils._ID+ "=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        Energy energy =new Energy(cursor.getInt(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
        return energy;
    }

    public int updateData(Energy energy){
        ContentValues values = new ContentValues();
        values.put(EnergyDBUtils.ENERGY,energy.getEnergy());
        values.put(EnergyDBUtils.DATE,energy.getDate());
        values.put(EnergyDBUtils.LOGIN,energy.getLogin());
        values.put(EnergyDBUtils.PUNCH_CARD,energy.getPunchCard());

        return energyDatabase.update(EnergyDBUtils.DATABASE_TABLE,values,
                EnergyDBUtils._ID + "=?",new String[]{String.valueOf(energy.getId())});
    }

    //遍历一个数据库，得到数据的条数
    public int judge(){
        Cursor c = energyDatabase.rawQuery("select * from "+EnergyDBUtils.DATABASE_TABLE ,null);
        int number = c.getCount();
        return  number;
    }
}
