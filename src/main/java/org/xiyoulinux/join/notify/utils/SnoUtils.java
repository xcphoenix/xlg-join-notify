package org.xiyoulinux.join.notify.utils;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/11/6 下午10:44
 */
@Log4j2
public class SnoUtils {

    private SnoUtils() {
    }

    private static final int SNO_LEN = 8;
    /// private static final Pattern SNO_PATTERN = Pattern.compile("^[\\d]{2}([\\d]{2})[\\d]*$");

    /**
     * 获取年级
     *
     * @param sno 学号
     * @return 年级，-1表示无效
     */
    public static int getGrade(@NonNull String sno) {
        if (sno.length() == SNO_LEN) {
            try {
                return Integer.parseInt(sno.substring(2, 4));
            } catch (NumberFormatException ne) {
                log.error(ne);
            }
        }
        return -1;
    }

    public static String getGradeStr(@NonNull String sno) {
        int res = getGrade(sno);
        if (res < 0) {
            return null;
        }
        return String.valueOf(res);
    }

    /**
     * 从专业班级中解析出专业，直接过滤后面的班级数字
     *
     * @param adminClass 专业班级
     * @return 专业信息
     */
    public static String getMajor(@NonNull String adminClass) {
        return adminClass.replaceAll("[0-9]", "");
    }

}
