package com.vimdream.htool.word;

import com.deepoove.poi.util.TableTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.util.Arrays;
import java.util.List;

/**
 * @Title: WordUtil
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/8/10 16:41
 */
public class WordUtil {

    /**
     * poi-tl 根据format进行单元格合并
     * @param table
     * @param row
     * @param format  eg. "* ** ***** **"
     */
    public static void merge(XWPFTable table, int row, String format) {

        if (StringUtils.isBlank(format)) {
            return;
        }

        List<String> zoneBlock = Arrays.asList(format.trim().split(" "));
        int length = zoneBlock.stream().map(zone -> zone.length()).reduce(0, (pre, next) -> pre + next).intValue();

        for (int i = zoneBlock.size() - 1; i >= 0; i--) {
            String zone = zoneBlock.get(i);
            if (zone.length() > 1) {
                TableTools.mergeCellsHorizonal(table, row, length - zone.length(), length - 1);
                length -= zone.length();
            } else {
                length--;
            }
        }
    }

}
