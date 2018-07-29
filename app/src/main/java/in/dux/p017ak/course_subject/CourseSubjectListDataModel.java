package in.dux.p017ak.course_subject;

public class CourseSubjectListDataModel {
    private String title;
    private String image;
    private String syllabusURL;
    private String subject_title;
    private String icon;

    public CourseSubjectListDataModel(String mTitle,String mSubject_title,String mIcon, String mImage, String mSyllabusURL) {
        title = mTitle;
        image = mImage;
        syllabusURL = mSyllabusURL;
        icon = mIcon;
        subject_title = mSubject_title;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getSyllabusURL() {
        return syllabusURL;
    }

    public String getIcon() {
        return icon;
    }

    public String getSubject_title() {
        return subject_title;
    }
}
