package org.example.model;

public class SearchInput {

    private String searchString;

    public SearchInput(String searchString) {
        this.searchString = searchString;
    }

    public SearchInput(){}

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
