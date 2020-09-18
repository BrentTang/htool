package com.tzh.word;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.MiniTableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.Arrays;
import java.util.List;

public class DetailTablePolicy extends DynamicTableRenderPolicy {

  // 货品填充数据所在行数
//  int goodsStartRow = 2;
  // 人工费填充数据所在行数
  int laborsStartRow = 7;

  @Override
  public void render(XWPFTable table, Object data) {
    if (null == data) return;
    List<RowRenderData> rowRenderDataList = (List<RowRenderData>) data;

    // 人工费循环渲染
//    List<RowRenderData> labors = detailData.getLabors();

    if (null != rowRenderDataList) {
      table.removeRow(laborsStartRow);
      // 循环插入行
      for (int i = 0; i < rowRenderDataList.size(); i++) {
        XWPFTableRow insertNewTableRow = table.insertNewTableRow(laborsStartRow);
        insertNewTableRow.setHeight(500 * 2);
        for (int j = 0; j < 10; j++) insertNewTableRow.createCell();

        // 合并单元格
        TableTools.mergeCellsHorizonal(table, laborsStartRow, 0, 1);
        TableTools.mergeCellsHorizonal(table, laborsStartRow, 1, 3);
        TableTools.mergeCellsHorizonal(table, laborsStartRow, 2, 5);
        if (i > 0) {
          TableTools.mergeCellsVertically(table, 0, laborsStartRow, laborsStartRow + 1);
          TableTools.mergeCellsVertically(table, 3, laborsStartRow, laborsStartRow + 1);
        }

//        TableTools.mergeCellsHorizonal(table, laborsStartRow, 5, 8);
//        TableTools.mergeCellsHorizonal(table, laborsStartRow, 6, 7);
//        TableTools.mergeCellsVertically(table, laborsStartRow, laborsStartRow, laborsStartRow + rowRenderDataList.size());
        // 渲染单行人工费数据
        MiniTableRenderPolicy.Helper.renderRow(table, laborsStartRow, rowRenderDataList.get(i));
      }
//      TableTools.mergeCellsVertically(table, 0, laborsStartRow, laborsStartRow + rowRenderDataList.size());
//      TableTools.mergeCellsVertically(table, 9, laborsStartRow, laborsStartRow + rowRenderDataList.size());


    }

    // 货品明细
    /*List<RowRenderData> goods = detailData.getGoods();
    if (null != goods) {
      table.removeRow(goodsStartRow);
      for (int i = 0; i < goods.size(); i++) {
        XWPFTableRow insertNewTableRow = table.insertNewTableRow(goodsStartRow);
        for (int j = 0; j < 7; j++) insertNewTableRow.createCell();
        // 渲染单行货品明细数据
        MiniTableRenderPolicy.Helper.renderRow(table, goodsStartRow, goods.get(i));
      }
    }*/
  }
}