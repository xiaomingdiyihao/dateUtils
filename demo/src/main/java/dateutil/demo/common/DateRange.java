package dateutil.demo.common;

import lombok.Data;

import java.util.Date;

/**
 * @description 日期范围
 **/
@Data
public class DateRange {
    /**
     * 开始日期
     */
    private Date start;
    /**
     * 结束日期
     */
    private Date end;

    public DateRange(Date start, Date end) {
        this.start = start;
        this.end = end;
    }
}
