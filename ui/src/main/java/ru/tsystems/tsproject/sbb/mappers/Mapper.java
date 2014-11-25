package ru.tsystems.tsproject.sbb.mappers;
import javax.servlet.http.HttpServletRequest;

public interface Mapper<T> {
    T map(HttpServletRequest request);
}
