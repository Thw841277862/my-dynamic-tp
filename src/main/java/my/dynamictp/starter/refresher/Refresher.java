package my.dynamictp.starter.refresher;


import my.dynamictp.starter.enums.ConfigFileTypeEnum;


public interface Refresher {

    /**
     * Refresh with specify content.
     *
     * @param content content
     * @param fileType file type
     */
    void refresh(String content, ConfigFileTypeEnum fileType);
}
