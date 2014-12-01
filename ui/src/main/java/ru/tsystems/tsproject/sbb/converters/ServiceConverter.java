package ru.tsystems.tsproject.sbb.converters;
import org.springframework.core.convert.converter.Converter;
import ru.tsystems.tsproject.sbb.transferObjects.ServiceTO;

/**
 * Created by apple on 29.11.14.
 */
public class ServiceConverter implements Converter<String, ServiceTO> {
    @Override
    public ServiceTO convert(String s) {
        int i = 0;
        i = Integer.parseInt(s);
        ServiceTO serviceTO = new ServiceTO();
        serviceTO.setId(i);
        return serviceTO;
    }
}
