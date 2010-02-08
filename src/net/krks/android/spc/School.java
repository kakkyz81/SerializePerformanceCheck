package net.krks.android.spc;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class School implements Serializable {
    public School() {
        id = 1; // for test only
    }

    private int id;

    private String name = "";

    List<Student> students = new ArrayList<Student>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void toXML(XmlSerializer xmlse) throws IllegalArgumentException, IllegalStateException, IOException {

        xmlse.startTag(null, "name");
        xmlse.text(name);
        xmlse.endTag(null, "name");

        for(Student s:students) {
            xmlse.startTag(null, "student");
            s.toXML(xmlse);
            xmlse.endTag(null, "student");
        }

    }

    public void fromXML(XmlPullParser xmlpull) throws XmlPullParserException, IOException {
        int eventType = xmlpull.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT ) {
            String name = xmlpull.getName();
            eventType = xmlpull.getEventType();

            switch (eventType) {
            case XmlPullParser.START_DOCUMENT:
                break;
            case XmlPullParser.START_TAG:
                if("name".equals(name)) {
                    name = xmlpull.nextText();
                }
                if("student".equals(name)) {
                    Student s = new Student();
                    s.fromXML(xmlpull);
                    students.add(s);
                }
                break;
            case XmlPullParser.END_TAG:
                break;
            case XmlPullParser.TEXT:
                break;
            case XmlPullParser.END_DOCUMENT:
                return;
            default:
                break;
            }
            xmlpull.next();
        }
    }

    @Override
    public String toString() {
        return "School [id=" + id + ", name=" + name + ", students=" + students
                + "]";
    }
}
