package net.krks.android.spc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SerializePerformanceCheck extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        countEdit = new EditText(this);
        onCreateDialog();

        for(int i = 0;i < 50;i++) {
            newStudent();
        }

        Button setObj = (Button) findViewById(R.id.Button04);
        setObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setAlert.show();  }
        });

        Button wrtObj = (Button) findViewById(R.id.wrtObjBtn);
        wrtObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeObject();
                setTime();
            }
        });

        Button redObj = (Button) findViewById(R.id.redObjBtn);
        redObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readObject();
                setTime(); }
        });

        Button wrtSQL = (Button) findViewById(R.id.wrtSQLBtn);
        wrtSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeSQLite();
                setTime(); }
        });

        Button redSQL = (Button) findViewById(R.id.redSQLBtn);
        redSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSQLite();
                setTime(); }
        });

        Button wrtXML = (Button) findViewById(R.id.wrtXMLBtn);
        wrtXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeXML();
                setTime(); }
        });

        Button redXML = (Button) findViewById(R.id.redXMLBtn);
        redXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readXML();
                setTime(); }
        });

        TextView tv03 = (TextView) findViewById(R.id.TextView03);
        tv03.setText("Student:" + school.getStudents().size());
    }

    private SQLiteDatabase mDb;

    private Date starttime;
    private Date endtime;

    private void setTime() {
        TextView tvs = (TextView) findViewById(R.id.TextView01);
        tvs.setText("start:" + df.format(starttime));

        TextView tve = (TextView) findViewById(R.id.TextView02);
        tve.setText(" end :" + df.format(endtime) + " // " + (endtime.getTime() - starttime.getTime()) + " msec");
    }

    private void onCreateDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setView(countEdit);
        ab.setPositiveButton("set", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                school = new School();
                Editable text = countEdit.getText();
                try {
                    if (text != null) {
                        Log.i("spc",text.toString());
                        long count = Long.valueOf(text.toString());
                        for(int i = 0; i < count; i++) {
                            newStudent();
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                TextView tv03 = (TextView) findViewById(R.id.TextView03);
//                Log.i("spc",tv03.toString());
                tv03.setText("Student:" + school.getStudents().size());
            }

        });
        ab.setCancelable(true);
        ab.setMessage("set count");
        setAlert = ab.create();
    }

    private AlertDialog setAlert;
    private School school = new School();
    private EditText countEdit;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    private void newStudent() {
        Student student = new Student();
        student.setId(school.getStudents().size() + 1);
        student.setName("name_" + student.getId());
        student.setSchool(school);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i ++) {
            sb.append("a");
        }
        student.setText(sb.toString());
        school.getStudents().add(student);
    }

    private void writeXML() {
        starttime = new Date();
        Log.i("SPC", "writeXML start " + df.format(starttime));
        try {
//             BufferedWriter bw =
//                new BufferedWriter(
//                        new OutputStreamWriter(
//                                new FileOutputStream("spc.xml"),
//                                "UTF-8"), 1024);
            FileOutputStream o = openFileOutput("spc.xml", MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(o),1024);
            XmlSerializer xmlse = Xml.newSerializer();
            xmlse.setOutput(bw);

            xmlse.startDocument(null, Boolean.valueOf(true));
            xmlse.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            xmlse.startTag(null, "root");
            school.toXML(xmlse);
            xmlse.endTag(null, "root");
            xmlse.endDocument();
            xmlse.flush();
            o.close();
        } catch (FileNotFoundException e) {
            logout(e);
        } catch (IOException e) {
            logout(e);
        }
        endtime = new Date();
        Log.i("SPC", "writeXML  end " + df.format(endtime));
    }

    private void readXML() {
        starttime = new Date();
        Log.i("SPC", "readXML start " + df.format(starttime));
        try {
//             BufferedWriter bw =
//                new BufferedWriter(
//                        new OutputStreamWriter(
//                                new FileOutputStream("spc.xml"),
//                                "UTF-8"), 1024);
            FileInputStream i = openFileInput("spc.xml");
            BufferedReader br = new BufferedReader( new InputStreamReader(i),1024);
            XmlPullParser xmlpull = Xml.newPullParser();
            xmlpull.setInput(br);

            school = new School();
            school.fromXML(xmlpull);

        } catch (FileNotFoundException e) {
            logout(e);
        } catch (IOException e) {
            logout(e);
        } catch (XmlPullParserException e) {
            logout(e);
        }
        endtime = new Date();
        Log.i("SPC", "readXML  end " + df.format(endtime));
        for(Student s:school.getStudents()) {
            Log.i("SPC",s.toString());
        }
        Log.i("SPC","count:"+school.getStudents().size());
    }

    private void writeObject() {
        starttime = new Date();
        Log.i("SPC", "writeObject start " + df.format(starttime));
        try {
            FileOutputStream o = openFileOutput("spc.obj", MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(o);
            out.writeObject(school);
            out.flush();
            o.flush();
        } catch (FileNotFoundException e) {
            logout(e);
        } catch (IOException e) {
            logout(e);
        }
        endtime = new Date();
        Log.i("SPC", "writeObject  end " + df.format(endtime));
    }

    private void readObject() {
        starttime = new Date();
        Log.i("SPC", "readObject start " + df.format(starttime));

        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput("spc.obj"));
            School readObject = (School) in.readObject();
            if (null != readObject) {
                school = readObject;
            }
        } catch (StreamCorruptedException e) {
            logout(e);
        } catch (FileNotFoundException e) {
            logout(e);
        } catch (IOException e) {
            logout(e);
        } catch (ClassNotFoundException e) {
            logout(e);
        }
        endtime = new Date();
        Log.i("SPC", "readObject  end " + df.format(endtime));
        for(Student s:school.getStudents()) {
            Log.i("SPC",s.toString());
        }
        Log.i("SPC","count:"+school.getStudents().size());
    }

    private void writeSQLite() {
        starttime = new Date();
        Log.i("SPC", "writeSqlite start " + df.format(starttime));

        mDb.execSQL("delete from spc_school;");
        mDb.execSQL("delete from spc_student;");

        ContentValues school_values = new ContentValues();
        school_values.put("name", school.getName());
        mDb.insert("spc_school", null, school_values);

        mDb.beginTransaction();
        for(Student s:school.getStudents()) {

            SQLiteStatement stmt = mDb.compileStatement("insert into spc_student(name,id,school,text) values (?,?,1,?);");
            stmt.bindString(1, s.getName());
            stmt.bindLong(2, s.getId());
            stmt.bindString(3, s.getText());
            stmt.execute();
//            ContentValues student_values = new ContentValues();
//            student_values.put("name", s.getName());
//            student_values.put("id", s.getId());
//            student_values.put("school", s.getSchool().getId());
//            student_values.put("text", s.getText());
//            mDb.insert("spc_student", null, school_values);

        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();

        endtime = new Date();
        Log.i("SPC", "writeSqlite  end " + df.format(endtime));
    }


    private void readSQLite() {
        starttime = new Date();
        Log.i("SPC", "writeSqlite start " + df.format(starttime));

        String[] scColumns = new String[] {
                "name"
        };

        Cursor scc = mDb.query("spc_school", scColumns, null, null,null,null,null);
        if(scc.moveToFirst()) {
            do {
                school = new School();
                school.setName(scc.getString(0));
            } while (scc.moveToNext());
        }

        String[] stColumns = new String[] {
                "name",
                "id",
                "school",
                "text",
        };

        Cursor stc = mDb.query("spc_student", stColumns, null, null,null,null,null);
        if(stc.moveToFirst()) {
            do {
                Student st = new Student();
                st.setName(stc.getString(0));
                st.setId(stc.getInt(1));
                st.setSchool(school);
                st.setText(stc.getString(3));
                school.getStudents().add(st);
            } while (stc.moveToNext());
        }
        endtime = new Date();
        Log.i("SPC", "writeSqlite  end " + df.format(endtime));
        for(Student s:school.getStudents()) {
            Log.i("SPC",s.toString());
        }
        Log.i("SPC","count:"+school.getStudents().size());
    }

    private static void logout(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        Log.e("SPC", sw.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDb.close();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        SPCOpenHelper sh = new SPCOpenHelper(this);

        mDb = sh.getWritableDatabase();
//        sh.drop(mDb);
    }

}