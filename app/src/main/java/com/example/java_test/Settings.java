package com.example.java_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Settings extends AppCompatActivity {
    protected boolean AssSubSwitch = false;
    protected subject curSub;
    protected boolean removing = false;
    protected boolean removingop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Assredraw();
        AssClassReturn(new View(this));
        EditText text = (EditText) findViewById(R.id.WarningTime);
        String temp = String.valueOf(Assignment_create.warning);
        text.setText((CharSequence)temp);
    }

    public void setWarningTime(View v){
        EditText temp = findViewById(R.id.WarningTime);
        String val = temp.getText().toString();
        int num = Integer.parseInt(val);
        Assignment_create.warning = num;
        temp.setText((CharSequence) String.valueOf(num));
    }
    public void GoToCreate(View v){
        Intent intent = new Intent(this, Assignment_create.class);
        startActivity(intent);
    }
    public void GoToMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void WritetoFile(String name, String cont){
        FileOutputStream out;
        try {
            File path = getApplicationContext().getFilesDir();
            out = new FileOutputStream(new File(path, name));
            out.write(cont.getBytes());
            out.close();

        }
        catch (Exception e){
            ////log.d("try", "no u");
        }

    }

    public String GetFromFile(String name){
        FileInputStream in;
        try {
            in = openFileInput(name);
            String temp = "";
            int add = in.read();
            while(add!=-1){
                char val = (char)add;
                temp+=val;
                add = in.read();
            }
            in.close();
            return temp;
        }
        catch (Exception e){
            //log.d("try", "no u");
            return null;
        }

    }


    public void AssClassCreate(View v){
        Button b = findViewById(R.id.AssClassAdd);
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);
        b = findViewById(R.id.AssClassSwitch);
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);

        int vis = View.VISIBLE;
        boolean ena = true;
        findViewById(R.id.AssClassCreateCreate).setVisibility(vis);
        findViewById(R.id.AssClassCreateCreate).setEnabled(true);
        findViewById(R.id.AssClassCreateNo).setVisibility(vis);
        findViewById(R.id.AssClassCreateNo).setEnabled(true);
        findViewById(R.id.AssClassCreateT).setVisibility(vis);
        findViewById(R.id.AssClassCreateIT).setVisibility(vis);
        findViewById(R.id.AssClassCreateIT).setEnabled(true);
    }

    public void AssClassCreateYes(View v) throws Exception{
        TextInputEditText text = findViewById(R.id.AssClassCreateIT);
        String temp = text.getText().toString();
        if(!check(temp)) {
            AssClassReturn(v);
            return;
        }
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput(temp, MODE_PRIVATE);
        }
        catch (Exception e){
            AssClassReturn(v);
            return;
        }
        File newfile = getFileStreamPath(temp);
        PrintWriter out1 = new PrintWriter(new FileWriter(newfile));
        out1.print(" ~~~ ~~~ ");
        out1.close();
        String empty = "";
        text.setText((CharSequence) empty);
        subject sub = new subject(temp, MainActivity.num++);
        assignment_manager.sublist.put(sub.name, sub);
        WritetoFile("Assignments;;;", GetFromFile("Assignments;;;")+temp+";;;");
        Log.d("fuk", GetFromFile("Assignments;;;"));
        MainActivity.num++;
        AssClassReturn(v);
        Assredraw();
    }

    public boolean check(String s){
        if(s.equals(""))
            return false;
        if(s.indexOf(";;;")!=-1)
            return false;
        if(s.indexOf("~~~")!=-1){
            return false;
        }
        if(s.indexOf(("<<<"))!=-1)
            return false;
        if(assignment_manager.sublist.get(s)!=null)
            return false;
        return true;
    }


    public void AssClassReturn(View v){
        Button b = findViewById(R.id.AssClassAdd);
        b.setEnabled(true);
        b.setVisibility(View.VISIBLE);
        b = findViewById(R.id.AssClassSwitch);
        b.setEnabled(true);
        b.setVisibility(View.VISIBLE);

        int vis = View.INVISIBLE;
        boolean ena = false;
        findViewById(R.id.AssClassCreateCreate).setVisibility(vis);
        findViewById(R.id.AssClassCreateCreate).setEnabled(true);
        findViewById(R.id.AssClassCreateNo).setVisibility(vis);
        findViewById(R.id.AssClassCreateNo).setEnabled(true);
        findViewById(R.id.AssClassCreateT).setVisibility(vis);
        findViewById(R.id.AssClassCreateIT).setVisibility(vis);
        findViewById(R.id.AssClassCreateIT).setEnabled(true);
    }

    public void Assredraw(){
        ChipGroup group1 = findViewById(R.id.SettingsClass);
        group1.removeAllViews();
        ChipGroup group2 = findViewById(R.id.SettingsClassOp);
        group2.removeAllViews();
        for(subject sub: assignment_manager.sublist.values()){
            Chip chip = new Chip(this);
            chip.setText(sub.name);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChipGroup group = findViewById(R.id.SettingsClass);
                    if(removing){
                        Chip chip = (Chip)v;
                        Log.d("logd", "onClick: ");
                        for(String i: assignment_manager.sublist.keySet()){
                            assignment_manager.sublist.get(i);
                            Log.d("logd", "onClicked: ");
                            if(i.equals(chip.getText().toString())){
                                assignment_manager.sublist.remove(i);
                                String temp = GetFromFile("Assignments;;;");
                                String[] val = temp.split(";;;");
                                String ret = "";
                                for(int j = 0; j<val.length; j++){
                                    if(!val[j].equals(i)){
                                        ret+=val[j]+";;;";
                                    }
                                }
                                WritetoFile("Assignments;;;", ret);
                                break;
                            }
                            Log.d("logd", "onClicks: ");
                        }
                        AssClassReturn(v);
                        Assredraw();
                        removing = false;
                        return;
                    }
                    Chip v1 = findViewById(group.getCheckedChipId());
                    if(v1!=null){
                        v1.setChipBackgroundColorResource(R.color.white);
                    }
                     curSub = sub;
                    Chip v2 = (Chip)v;
                    v2.setChipBackgroundColorResource(R.color.topBar);
                     group.check(v.getId());
                    AssSubChip(sub);
                }
            });
            group1.addView(chip);
            curSub = null;
        }
    }

    public void AssSubChip(subject c){
        ChipGroup group2 = findViewById(R.id.SettingsClassOp);
        group2.removeAllViews();
        //log.d("try", "AssSubChip: ");
        for(String s: c.options){
            Chip chip = new Chip(this);
            chip.setText(s);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(removingop) {
                        TextView notused = (TextView)v;
                        String temp = (String)notused.getText();
                        LinkedList<String>list = curSub.options;
                        String add = "";
                        for(int i = 0; i<list.size(); i++) {
                            if(list.get(i).equals(temp)) {
                                list.remove(i);
                                i--;
                                continue;
                            }
                            add+=list.get(i)+";;;";
                        }
                        String[] split = GetFromFile(curSub.name).split("~~~");
                        WritetoFile(curSub.name, add+"~~~"+split[1]+"~~~"+split[2]);
                        removingop = false;
                        AssClassReturnOp(v);
                        Assredraw();
                    }
                }
            });
            group2.addView(chip);
        }
    }

    public void AssClassCreateOp(View v){
        if(curSub==null)
            return;
        Button b = findViewById(R.id.AssClassAddOp);
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);
        b = findViewById(R.id.AssClassSwitchOp);
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);

        int vis = View.VISIBLE;
        boolean ena = true;
        findViewById(R.id.AssClassCreateCreateOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateCreateOp).setEnabled(true);
        findViewById(R.id.AssClassCreateNoOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateNoOp).setEnabled(true);
        findViewById(R.id.AssClassCreateTOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateITOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateITOp).setEnabled(true);
    }

    public void removeOption(View v){
        if(curSub==null)
            return;
        Button b = findViewById(R.id.AssClassAddOp);
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);
        removingop = true;
        }


    public void AssClassCreateYesOp(View v){
        TextInputEditText text = findViewById(R.id.AssClassCreateITOp);
        String temp = text.getText().toString();
        if(!check(temp)){
            AssClassReturnOp(v);
            return;
        }

        String empty = "";
        text.setText((CharSequence) empty);
        String sub = temp;
        curSub.options.add(sub);
        String val = GetFromFile(curSub.name);
        String[] add = val.split("~~~");
        add[0] = add[0]+temp+";;;"+"~~~"+add[1]+"~~~"+add[2];
        WritetoFile(curSub.name, add[0]);

        AssClassReturnOp(v);
        Assredraw();
    }

    public void AssClassReturnOp(View v){
        Button b = findViewById(R.id.AssClassAddOp);
        b.setEnabled(true);
        b.setVisibility(View.VISIBLE);
        b = findViewById(R.id.AssClassSwitchOp);
        b.setEnabled(true);
        b.setVisibility(View.VISIBLE);

        int vis = View.INVISIBLE;
        boolean ena = false;
        findViewById(R.id.AssClassCreateCreateOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateCreateOp).setEnabled(true);
        findViewById(R.id.AssClassCreateNoOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateNoOp).setEnabled(true);
        findViewById(R.id.AssClassCreateTOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateITOp).setVisibility(vis);
        findViewById(R.id.AssClassCreateITOp).setEnabled(true);
    }

    public void AssClassRemove(View v){
        curSub=null;
        Button b = findViewById(R.id.AssClassAdd);
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);
        removing = true;
    }
}