package notasdeviaje.notasdeviaje.modelos;

public class Nota {

    private String titulo;
    private String Descripcion;

    public Nota(String titulo, String descripcion) {
        this.titulo = titulo;
        this.Descripcion = descripcion;
    }

    public Nota() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }


    public boolean equals(Nota nota) {
        return (nota.getTitulo().equals(this.titulo) && nota.getDescripcion().equals(this.Descripcion));
    }
}
