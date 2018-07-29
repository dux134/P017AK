package in.dux.p017ak.notification;

public class NotificationDataModel {
    private String title;
    private String link;

    public NotificationDataModel(String mTitle,String mLink) {
        title = mTitle;
        link = mLink;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
