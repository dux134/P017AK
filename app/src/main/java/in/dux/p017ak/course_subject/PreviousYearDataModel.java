package in.dux.p017ak.course_subject;

public class PreviousYearDataModel {
    private String title;
    private String icon;
    private String link;

    public PreviousYearDataModel(String mTitle,String mIcon,String mLink) {
        title = mTitle;
        icon = mIcon;
        link = mLink;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }
}
