package app_utility;

public class MovieData {

    String sMovieName;
    String sLanguage;
    String sVideoLink;
    String sDescription;
    String sPhoto;

    public MovieData() {

    }


    public MovieData(String sMovieName, String sLanguage, String sVideoLink, String sDescription, String sPhoto){
        this.sMovieName = sMovieName;
        this.sLanguage = sLanguage;
        this.sVideoLink = sVideoLink;
        this.sDescription = sDescription;
        this.sPhoto = sPhoto;
    }

    public String getMovieName(){
        return this.sMovieName;
    }
    public String getLanguage(){
        return this.sLanguage;
    }
    public String getVideoLink(){
        return this.sVideoLink;
    }
    public String getDescription(){
        return this.sDescription;
    }
    public String getPhoto(){
        return this.sPhoto;
    }



}
