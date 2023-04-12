package my.dynamictp.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigFileTypeEnum {

    /**
     * Config file type.
     */
    PROPERTIES("properties"),
    XML("xml"),
    JSON("json"),
    YML("yml"),
    YAML("yaml"),
    TXT("txt");

    private final String value;

    public static ConfigFileTypeEnum of(String value) {
        for (ConfigFileTypeEnum typeEnum : ConfigFileTypeEnum.values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return PROPERTIES;
    }
}
