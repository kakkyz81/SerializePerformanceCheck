package net.krks.android.spc;

import java.io.IOException;
import java.io.Serializable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class Student implements Serializable {
    private String name;

    private long id;

    private School school;

    private String text;

    public String getName() {
        if(name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getText() {
        if(text == null) {
            return "";
        }
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void toXML(XmlSerializer xmlse) throws IllegalArgumentException, IllegalStateException, IOException {
        xmlse.startTag(null, "name");
        if(name != null) {
            xmlse.text(name);
        }else {
            xmlse.text("dummy");
        }
        xmlse.endTag(null, "name");

        xmlse.startTag(null, "id");
        xmlse.text(String.valueOf(id));
        xmlse.endTag(null, "id");

        xmlse.startTag(null, "school");
//        xmlse.text(String.valueOf(school));
        xmlse.text("dummy");
        xmlse.endTag(null, "school");

        xmlse.startTag(null, "text");
        if(text != null) {
            xmlse.text(text);
        }else {
            xmlse.text("dummy");
        }

        xmlse.endTag(null, "text");
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
                if("id".equals(name)) {
                    id = Long.valueOf(xmlpull.nextText());
                }
                if("school".equals(name)) {
//                    school = Long.valueOf(xmlpull.nextText());
                    school = new School();
                }
                if("text".equals(name)) {
                    text = xmlpull.nextText();
                }
                break;
            case XmlPullParser.END_TAG:
                if("student".equals(name)) {
                    return;
                }
                break;
            case XmlPullParser.TEXT:
                break;
            default:
                break;
            }
            xmlpull.next();

        }

    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name // + ", school=" + school
                + ", text=" + text + "]";
    }

}
