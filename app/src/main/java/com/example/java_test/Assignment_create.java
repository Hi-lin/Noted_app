package com.example.java_test;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

public class Assignment_create extends AppCompatActivity {
    public int mode = 1;
    public static int warning = 1;
    public static Date today = new Date();;
    private boolean useDate = true;
    private Chip chip1;
    private Chip chip2;
    //public static Date finDate;
    public static File Assignments;

    private boolean called = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_create);

        Assignments = new File(this.getFilesDir(), "Assignments;;;");
        Spinner spinner = findViewById(R.id.AssCalDropdown);
        String[] DaysOW = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, DaysOW);
        spinner.setAdapter(adapter);

        mode = 1;
        EditText edt = findViewById(R.id.AssCalText);
        edt.setText((CharSequence) String.valueOf(0));
        //EditText e1 = (EditText) findViewById(R.id.AssCalDayT);
        //e1.setText((CharSequence) String.valueOf(today.getMonth()));
        //e1.setText((CharSequence) String.valueOf(12));

        TreeMap<String, subject> temp = assignment_manager.sublist;
        ChipGroup subChip = findViewById(R.id.AssignmentClass);
        ChipGroup assChip = findViewById(R.id.AssignmentAss);
        for (subject i : temp.values()) {
            Chip chip = new Chip(this);
            chip.setText(i.name);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("works", "onClick: ");
                    findViewById(R.id.AssError).setVisibility(View.INVISIBLE);
                    Log.d("sux", String.valueOf(view.getId())+" "+subChip.getCheckedChipId());
                    chip1 = (Chip)view;
                    ReplaceColor(subChip, view);
                    assChip.removeAllViews();
                    LinkedList<String> temp = i.options;
                    for (String j : temp) {
                        Chip chips = new Chip(chip.getContext());
                        chips.setText(j);
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                findViewById(R.id.AssError).setVisibility(View.INVISIBLE);
                                Chip t1 = (Chip) view;
                                ReplaceColor(assChip, view);
                            }
                        });
                        assChip.addView(chips);
                    }
                    Log.d("works2", "onClick: ");
                }
            });
            subChip.addView(chip);
        }


        spinner.setSelection(today.getDay());

        EditText editTextm3 = (EditText) findViewById(R.id.AssCalYearT);
        editTextm3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                MonthTimeChange(called);
            }
        });EditText editTextm2 = (EditText) findViewById(R.id.AssCalMonthT);
        editTextm2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                MonthTimeChange(called);
            }
        });
        EditText editTextm1 = (EditText) findViewById(R.id.AssCalDayT);
        editTextm1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                MonthTimeChange(called);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WeekTimeChange(i+1);}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        EditText editText = (EditText) findViewById(R.id.AssCalText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Spinner spin = findViewById(R.id.AssCalDropdown);
                WeekTimeChange(spin.getSelectedItemPosition()+1);}
        });

        WeekTimeChange(spinner.getSelectedItemPosition());

    }

    public void ReplaceColor(ChipGroup subChip, View view){
        Chip v1 = findViewById(subChip.getCheckedChipId());
        if(v1!=null){
            v1.setChipBackgroundColorResource(R.color.white);
        }
        Chip v2 = (Chip)view;
        v2.setChipBackgroundColorResource(R.color.topBar);
        subChip.check(view.getId());
    }
    public void WeekTimeChange(int day){
        TextView temp = findViewById(R.id.AssCalText);
        if (temp.getText().toString().equals(""))
            return;
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int d1 = cal.get(Calendar.DAY_OF_WEEK);
        Log.d("try", day+" "+d1);
        Log.d("try",cal.toString());
        d1 = day-d1;
        int d2 = 0;
        try {
            if (!temp.getText().toString().equals("")) {
                d2 = Integer.parseInt(temp.getText().toString());
            }
        }
        catch (Exception e){
            return;
        }
        d1+=(7*d2);
        cal.add(Calendar.DATE, d1);
        EditText t1 = findViewById(R.id.AssCalMonthT);
        //String temp1 = String.valueOf(cal.get(Calendar.MONTH));
        String temp1 = String.valueOf(cal.get(Calendar.MONTH)+1);
        EditText t2 = findViewById(R.id.AssCalDayT);
        String temp2 = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        EditText t3 = findViewById(R.id.AssCalYearT);
        String temp3 = String.valueOf(cal.get(Calendar.YEAR));
        called = false;
        try {
            t1.setText((CharSequence) temp1);
            t2.setText((CharSequence) temp2);
            t3.setText((CharSequence) temp3);
            findViewById(R.id.AssError).setVisibility(View.INVISIBLE);
        }
        catch (Exception e){
            //Log.d("WeekChangeError", temp1+": "+temp2+": "+temp3+": ");
            System.out.println(temp1+": "+temp2+": "+temp3+": ");
        }
        called = true;
    }

    public void MonthTimeChange(boolean called){
        EditText textView = findViewById(R.id.AssCalText);
        if(called) {
            String thing = "";
            CharSequence chars = (CharSequence) thing;
            if (!textView.getText().toString().equals(""))
                textView.setText(chars);
            findViewById(R.id.AssError).setVisibility(View.INVISIBLE);
            return;
        }

    }
    public void AssCreate(View v){
        Date finDate = null;
            Log.d("fuk", "1 ");
            String text = "";
            boolean create = true;
            if (chip1 == null) {
                text += "no class; ";
                create = false;
            }
            try {
                if (useDate) {
                    DateFormat df = new SimpleDateFormat("MM-dd-yy");
                    df.setLenient(false);
                    TextView t1 = findViewById(R.id.AssCalMonthT);
                    TextView t2 = findViewById(R.id.AssCalDayT);
                    TextView t3 = findViewById(R.id.AssCalYearT);
                    String dateString = t1.getText() + "-" + t2.getText() + "-" + t3.getText();
                    finDate = df.parse(dateString);
                    DateFormat format = new SimpleDateFormat("MM-dd-yy");
                    Log.d("TAG", format.format(finDate));
                }
            } catch (Exception e) {
                text += "invalid date";
                create = false;
            }
            if (!create) {
                if (text.length() < 12)
                    text = text.substring(0, 8);
                TextView Error = findViewById(R.id.AssError);
                Error.setText(text);
                Error.setVisibility(View.VISIBLE);
            }
            assignment newAss = null;
            if (chip1 != null && useDate) {
                TextInputEditText Add = findViewById(R.id.AssAdditionalInfo);
                ChipGroup group = findViewById(R.id.AssignmentAss);
                Chip temp = findViewById(group.getCheckedChipId());
                String ans = "";
                if (temp != null) {
                    ans = temp.getText().toString();
                }
                Log.d("sups", ans);
                newAss = new assignment((String) chip1.getText(), ans, Add.getText().toString(), finDate);
                Log.d("fuk", "2 ");
            }
            if (chip1 != null && !useDate) {
                TextInputEditText Add = findViewById(R.id.AssAdditionalInfo);
                ChipGroup group = findViewById(R.id.AssignmentAss);
                Chip temp = findViewById(group.getCheckedChipId());
                String ans = "";
                if (temp != null) {
                    ans = temp.getText().toString();
                }
                newAss = new assignment((String) chip1.getText(), ans, Add.getText().toString(), null);
            }
            if(mode==1) {
                assignment_manager.add(newAss);
            }
            else{
                Log.d("sups", "AssCreate1: ");
                reminder rem = new reminder(newAss.subject, newAss.task, newAss.time);
                reminder_manager.add(rem);
            }
            String add = "";
            if(useDate) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String str = format.format(finDate);
                add = newAss.task + "<<<" + str + ";;;";
            }
            else{
                add = newAss.task+"<<<"+";;;";
            }
            String[] temps = GetFromFile(newAss.subject).split("~~~");
            if(mode==1) {
                WritetoFile(newAss.subject, temps[0] + "~~~" + temps[1] + add + "~~~" + temps[2]);
            }
            else{
                WritetoFile(newAss.subject, temps[0] + "~~~" + temps[1] + "~~~" + temps[2]+add);
            }
            Log.d("fuk", "3 ");
            AssExit();

    }
    public void switchMode(View v){
        mode*=-1;
        TextView t = (TextView) findViewById(R.id.TitleText);
        if(mode==1){
            String s = "New Assign.";
            t.setText((CharSequence) s);
        }
        else{
            String s = "New Remind";
            t.setText((CharSequence) s);
        }

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
    public void noDate(View view){
        CheckBox check = (CheckBox)view;
        if (check.isChecked()){
            findViewById(R.id.AssCalMonthT).setEnabled(false);
            findViewById(R.id.AssCalDayT).setEnabled(false);
            findViewById(R.id.AssCalYearT).setEnabled(false);
            findViewById(R.id.AssCalText).setEnabled(false);
            findViewById(R.id.AssCalDropdown).setEnabled(false);
            useDate = false;
            findViewById(R.id.AssError).setVisibility(View.INVISIBLE);
        }
        else{
            findViewById(R.id.AssCalMonthT).setEnabled(true);
            findViewById(R.id.AssCalDayT).setEnabled(true);
            findViewById(R.id.AssCalYearT).setEnabled(true);
            findViewById(R.id.AssCalText).setEnabled(true);
            findViewById(R.id.AssCalDropdown).setEnabled(true);
            useDate = true;
        }
    }
    public void AssExit(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void Switch(View v){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }


}

class assignment{
    public String subject;
    public String task;
    public Date time;
    public boolean overdue = false;
    public int urgency = 0;
    public assignment(String a, String b, String c, Date t){
        subject = a;
        time = t;
        if(b==null||b.equals("")){
            task = c;
        }
        else {
            task = b + ": " + c+": ";
        }
    }
    public String toString(){
        String str = task;
        if(time!=null){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            str += "<<<"+format.format(time);
        }
        return str;
    }
    public assignment(String a, String b, Date t){
        subject = a;
        time = t;
        task = b;
        }
    }


class subject implements Comparable<subject> {
    public String name;
    public int ind;
    LinkedList<String> options = new LinkedList<>();
    ArrayList<assignment> list  = new ArrayList<>();
    public subject(String a, int i){
        name = a;
        ind = i;
    }

    @Override
    public int compareTo(subject o) {
        return Integer.compare(ind, o.ind);
    }
}

class assignment_manager{
    public static TreeMap<String, subject> sublist = new TreeMap<>();
    public static ArrayList<assignment> timelist = new ArrayList<>();
    public static LinkedList<assignment> nonTimeList = new LinkedList<>();
    public static void add(assignment a){
        if(a.time!=null) {
            Date temp = a.time;
            Calendar cal = Calendar.getInstance();
            cal.setTime(temp);
            cal.add(Calendar.DATE, 1);
            if (Assignment_create.today.compareTo(cal.getTime()) > 0) {
                a.urgency = 2;
            }
            cal.add(Calendar.DATE, -Assignment_create.warning);
            if (Assignment_create.today.compareTo(cal.getTime()) > 0 && a.urgency == 0) {
                a.urgency = 1;
            }
        }
        sublist.get(a.subject).list.add(a);
        /*if(a.time!= null) {
            int beg = -1;
            int end = timelist.size();
            while (beg - end != 1) {
                int mid = (end - beg) / 2;
                assignment temp = timelist.get(mid);
                int compared = temp.time.compareTo(a.time);
                if (compared == 1)
                    beg = mid;
                if (compared == -1)
                    end = mid;
                else {
                    end = mid;
                    break;
                }
            }
            timelist.add(end, a);
        }
        else
            nonTimeList.add(a);
        for(subject i: sublist){
            if(i.name.equals(a.subject)){
                i.list.add(a);
                break;
            }
        }*/
    }


}
