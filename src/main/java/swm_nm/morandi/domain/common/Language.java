package swm_nm.morandi.domain.common;

public enum Language {
    Python("Python"),
    Java("Java"),
    Cpp("Cpp");

    private String language;

    Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

}
