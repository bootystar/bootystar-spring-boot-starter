package io.github.bootystar.starter.prop.support;

import lombok.Data;

/**
 * @author bootystar
 */
@Data
public class ExcelProperties {
    private boolean initFastExcelConverter = true;
    private boolean initEasyExcelConverter = true;
    private ConverterProperties converter = new ConverterProperties();
    
    @Data
    public static class ConverterProperties {
        private boolean bigDecimalToString = true;
        private boolean bigIntegerToString = true;

        private boolean longToString = true;
        private boolean booleanToString = true;

        private boolean floatToString = true;
        private boolean doubleToString = true;

        private boolean sqlTimestampToString = true;
        private boolean sqlDateToString = true;
        private boolean sqlTimeToString = true;

        private boolean localDateTimeToString = true;
        private boolean localDateToString = true;
        private boolean localTimeToString = true;

        private boolean dateToString = true;
    }
    
    
}
