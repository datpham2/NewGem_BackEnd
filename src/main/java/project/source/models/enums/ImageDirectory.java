package project.source.models.enums;

public enum ImageDirectory {
    User ("profile picture"),
    Blog ("blog's background"),
    Room ("room preview"),
    Hotel ("hotel preview");

    private final String directoryReference;

    ImageDirectory(String directoryReference){
        this.directoryReference = directoryReference;
    }

    public String getDirectoryReference(){
        return directoryReference;
    }


}
