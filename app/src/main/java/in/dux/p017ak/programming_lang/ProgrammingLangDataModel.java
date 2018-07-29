package in.dux.p017ak.programming_lang;

public class ProgrammingLangDataModel {
    private String title;
    private String image_url;
    private String description;

    public ProgrammingLangDataModel(String mTitle,String description1, String mImage_url) {
        title = mTitle;
        image_url = mImage_url;
        description = description1;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDescription() {
        return description;
    }
}
