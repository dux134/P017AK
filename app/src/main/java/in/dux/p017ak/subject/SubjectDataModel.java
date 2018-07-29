package in.dux.p017ak.subject;

public class SubjectDataModel {
    private String title;
    private String link;
    private String image_url;

    public SubjectDataModel(String mTitle,String imageUrl, String mLink) {
        title = mTitle;
        link = mLink;
        image_url = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage_url() {
        return image_url;
    }
}
