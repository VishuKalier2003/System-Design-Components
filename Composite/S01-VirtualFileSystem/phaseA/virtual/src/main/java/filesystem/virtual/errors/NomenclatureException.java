package filesystem.virtual.errors;

public class NomenclatureException extends RuntimeException {
    public NomenclatureException(String input) {
        super("The Nomenclature for the current external database path "+input+" is wrong");
    }
}
