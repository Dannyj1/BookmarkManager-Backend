package nl.dannyj.bookmarkmanager.transformer;

public interface Transformer<T, S> {

    T toModel(S dto);

    S toDto(T model);

}
