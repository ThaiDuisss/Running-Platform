package com.running_platform.util;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.exception.AppException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEnumConverter implements ConverterFactory<String, Enum> {
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> {
            try {
                return (T) Enum.valueOf(targetType, source.toUpperCase());
            } catch (Exception e) {
                throw new AppException(ErrorEnum.UNKNOWN_ERROR);
            }
        };
    }
}
