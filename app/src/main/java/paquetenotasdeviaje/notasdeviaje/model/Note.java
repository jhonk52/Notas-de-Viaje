package paquetenotasdeviaje.notasdeviaje.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Note implements Serializable {

    private String _id;
    private String title;
    private String description;

    public Note(String _id, String title, String description) {
        this._id = _id;
        this.title = title;
        this.description = description;
    }

    public Note(String title, String description) {
        this._id = "";
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(@NonNull Object obj) {
        return (((Note) obj).getTitle().equals(this.title) && ((Note) obj).description.equals(this.description));
    }

    public Boolean isEmpty(){
        return this.title.isEmpty() && this.description.isEmpty();
    }
}
