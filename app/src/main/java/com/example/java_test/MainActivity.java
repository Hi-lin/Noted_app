package com.example.java_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //WritetoFile("Assignments;;;", "");
        try {
            FileInputStream in = openFileInput("Assignments;;;");
            String temp = "";
            int add = in.read();
            while(add!=-1){
                char val = (char)add;
                temp+=val;
                add = in.read();
            }
            String[] subs = temp.split(";;;");
            if(subs[0].equals("")){
                subs = new String[0];
            }
            Log.d("val", temp);
            num = subs.length;
            int i = 0;
            int j = 0;
            for(String s: subs) {
                //WritetoFile(s,"");
                subject sub = new subject(s, i);
                i++;
                assignment_manager.sublist.put(sub.name, sub);
                remsubject rsub = new remsubject(s,j);
                j++;
                reminder_manager.sublist.put(rsub.name,rsub);
                Log.d("rrr", String.valueOf(assignment_manager.sublist.size()));
                FileInputStream tempin = openFileInput(s);

                String temp1 = "";
                add = tempin.read();
                while (add != -1) {
                    char val = (char) add;
                    temp1 += val;
                    add = tempin.read();
                }
                Log.d("sus", temp1);
                String[] split = temp1.split("~~~");
                String[] options = split[0].substring(1).split(";;;");
                String[] assignments = split[1].substring(1).split(";;;");
                String[] reminders = split[2].substring(1).split(";;;");
                if (options[0].equals(""))
                    options = new String[0];
                if (assignments[0].equals(""))
                    assignments = new String[0];
                if(reminders[0].equals("")){
                    reminders = new String[0];
                }
                for (String o : options) {
                    sub.options.add(o);
                }
                Log.d("shit", String.valueOf(assignments.length));
                for (String a : assignments) {
                    String[] datediv = a.split("<<<");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    Log.d("shit", String.valueOf(datediv.length));
                    String ret = "";
                    if(datediv.length==2) {
                        date = format.parse(datediv[1]);
                    }
                    if(datediv.length>0){
                        ret+=datediv[0];
                    }
                    assignment ass = new assignment(s, ret, date);
                    assignment_manager.add(ass);
                    Log.d("shit", String.valueOf(assignment_manager.sublist.get(s).list.size()));
                }
                String ret = " ";
                for (String a : reminders) {
                    String[] datediv = a.split("<<<");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format.parse(datediv[1]);
                    reminder ass = new reminder(s, datediv[0], date);
                    if(reminder_manager.add(ass)){
                        ret+=a+";;;";
                    }
                    Log.d("shit", "stop");
                    Log.d("shit", ret);
                }
                WritetoFile(s, split[0]+"~~~"+split[1]+"~~~"+ret);

            }
        }
        catch (Exception e){
        }


      /*  ChipGroup test = (ChipGroup) findViewById(R.id.test);
        Chip chip = new Chip(this);
        chip.setText("hi");
       chip.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                chip.setTag("chip");
                ClipData.Item item = new ClipData.Item((CharSequence) chip.getTag());
                ClipData Clipdata = new ClipData((CharSequence) chip.getTag(), new String[] {
                ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(chip);
                chip.startDragAndDrop(Clipdata, myShadow, null, 0);
                return true;
            }
        });
        test.addView(chip);*/
        reDrawAssignments();
    }
    public void reDrawAssignments() {
        LinearLayout layout = findViewById(R.id.AssignmentList);
        for(subject s: assignment_manager.sublist.values()){
            for(assignment a: s.list){
                Log.d("yay", a.task);
                Log.d("yay", "sob1");
                CheckBox check = new CheckBox(this);
                check.setTextSize(24);
                if(a.time!=null) {

                    DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                    String text = a.subject + ":" + a.task + format.format(a.time);
                    check.setText((CharSequence) text);
                    if (a.urgency == 1) {
                        check.setBackgroundColor(getResources().getColor(R.color.warning));
                    }
                    if (a.urgency == 2) {
                        check.setBackgroundColor(getResources().getColor(R.color.late));
                    }
                }
                else{
                    Log.d("yay", "sob");
                    String text = a.subject+a.task;
                    check.setText((CharSequence) text);
                }
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            CrossOut(check, layout, a);

                        }
                    }
                });
                layout.addView(check);
            }
        }
        LinearLayout rems = findViewById(R.id.ReminderList);
        Log.d("boohoo", reminder_manager.sublist.toString());
        for(remsubject s: reminder_manager.sublist.values()){
            for(reminder a: s.list){
                Log.d("boohoo", "reDrawAssignments: ");
                CheckBox check = new CheckBox(this);
                check.setTextSize(24);
                DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
                String text = a.subject+":"+a.task+format.format(a.time);
                check.setText((CharSequence) text);
                Log.d("boohoo", "oof");
                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            remCrossOut(check, rems, a);

                        }
                    }
                });
                rems.addView(check);
            }
        }

    }
    protected void CrossOut(CheckBox v, LinearLayout l, assignment a){
        l.removeView(v);
        subject temp = assignment_manager.sublist.get(a.subject);
        for(int i = 0; i<temp.list.size(); i++){
            if(a.toString().equals(temp.list.get(i).toString())) {
                temp.list.remove(i);
                break;
            }
        }
        String str = "";
        ArrayList<assignment> arr = temp.list;
        for(int i = 0; i<temp.list.size(); i++){
            str+=arr.get(i).toString()+";;;";
        }
        String[] split = GetFromFile(temp.name).split("~~~");
    WritetoFile(temp.name, split[0]+"~~~"+" "+str+"~~~"+split[2]);
    }

    protected void remCrossOut(CheckBox v, LinearLayout l, reminder a){
        l.removeView(v);
        String ans = "";
        remsubject temp = reminder_manager.sublist.get(a.subject);
        for(int i = 0; i<temp.list.size(); i++){
            if(a.toString().equals(temp.list.get(i).toString())) {
                temp.list.remove(i);
                i--;
                continue;
            }
            ans+=temp.list.get(i).toString()+";;;";
        }
        String[] split = GetFromFile(temp.name).split("~~~");
        WritetoFile(temp.name, split[0]+"~~~"+split[1]+"~~~"+" "+ans);
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
    public void Switch(View v){
        Intent intent = new Intent(this, Settings.class);
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

    public void GoToCreate(View v){
        Intent intent = new Intent(this, Assignment_create.class);
        startActivity(intent);
    }
}