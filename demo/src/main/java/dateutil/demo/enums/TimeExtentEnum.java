package dateutil.demo.enums;

import lombok.Getter;

@Getter
public enum TimeExtentEnum {
    ONE_HOUR("ONE_HOUR", "近1小时"),
    ONE_DAY("ONE_DAY", "近1天"),
    ONE_WEEK("ONE_WEEK", "近1周"),
    ONE_MONTH("ONE_MONTH", "近1个月"),
    ONE_MONTH_AGO("ONE_MONTH_AGO", "1个月以前"),
    HALF_YEAR_AGO("HALF_YEAR_AGO", "半年以前"),
    ONE_YEAR_AGO("ONE_YEAR_AGO", "1年以前");

    private String type;
    private String desc;

    TimeExtentEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
