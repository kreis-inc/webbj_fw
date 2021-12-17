package jp.bj_one.fw.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BjExcelWriter implements AutoCloseable {

  private XSSFWorkbook workbook = null;
  private Sheet sheet = null;

  private List<String> fieldNames = null;
  private OutputStream out = null;
  private Integer rowCount = null;
  private Integer columnCount = null;
  private InputStream template = null;

  /**
   * 帳票出力時のコンストラクタ。
   *
   * @param out
   */
  public BjExcelWriter(OutputStream out) {
    this(out, 0, 0, null, null);
  }

  /**
   * 帳票出力時のコンストラクタ（テンプレート有）。
   *
   * @param out
   * @param template
   */
  public BjExcelWriter(OutputStream out, InputStream template) {
    this(out, 0, 0, null, template);
  }

  /**
   * 一覧データ出力時のコンストラクタ。
   *
   * @param out
   * @param startRow
   * @param startColumn
   * @param fieldNames
   */
  public BjExcelWriter(OutputStream out, Integer startRow, Integer startColumn, String[] fieldNames) {
    this(out, startRow, startColumn, Arrays.asList(fieldNames), null);
  }

  /**
   * 一覧データ出力時のコンストラクタ（テンプレート有）。
   *
   * @param out
   * @param startRow
   * @param startColumn
   * @param fieldNames
   * @param template
   */
  public BjExcelWriter(OutputStream out, Integer startRow, Integer startColumn, List<String> fieldNames, InputStream template) {

    try {
      this.out = out;
      this.rowCount = startRow;
      this.columnCount = startColumn;
      this.fieldNames = fieldNames;
      this.template = template;

      if (template != null) {
        this.workbook = new XSSFWorkbook(this.template);
        this.sheet = this.workbook.getSheetAt(0);
      } else {
        this.workbook = new XSSFWorkbook();
        this.sheet = this.workbook.createSheet();
      }
    } catch (IOException e) {
      e.printStackTrace();
      this.close();
    }

  }

  /**
   * 指定シートのコピーを行う。
   *
   * @param sheetIndex
   * @return
   */
  public Integer sheetCopy(Integer sheetIndex) {
    Sheet sheet = this.workbook.cloneSheet(sheetIndex);
    return this.workbook.getSheetIndex(sheet);
  }

  /**
   * 指定シートをアクティブにする。
   *
   * @param sheetIndex シートインデックス
   */
  public void sheetActivate(Integer sheetIndex) {
    this.sheet = this.workbook.getSheetAt(sheetIndex);
  }

  /**
   * 指定シートをコピーし、コピーされたシートをアクティブにする。
   *
   * @param sheetIndex シートインデックス
   * @return 追加したシートインデックス
   */
  public Integer sheetCopyAndActivate(Integer sheetIndex) {
    Integer index = this.sheetCopy(sheetIndex);
    this.sheetActivate(index);
    return index;
  }

  /**
   * シート名を変更する。
   *
   * @param sheetIndex シートインデックス
   * @param sheetName シート名
   */
  public void setSheetName(Integer sheetIndex, String sheetName) {
    this.workbook.setSheetName(sheetIndex, sheetName);
  }

  /**
   * 指定シートを削除する。
   *
   * @param sheetIndex シートインデックス
   */
  public void sheetDelete(Integer sheetIndex) {
    this.workbook.removeSheetAt(sheetIndex);
  }

  /**
   * 見出行の出力。
   *
   * @param columnArgs 出力値
   */
  public void writeHeader(String[] columnArgs) {
    this.writeHeader(Arrays.asList(columnArgs));
  }

  /**
   * 見出行の出力。
   *
   * @param columnNames 出力値
   */
  public void writeHeader(List<String> columnNames) {
    Integer columnIdx = this.columnCount;
    this.rowCount = writeHeader(this.rowCount, columnIdx, columnNames);
  }

  /**
   * 見出行の出力。
   *
   * @param rowIdx 出力開始行
   * @param columnIdx 出力開始列
   * @param columnNames 出力値
   * @return 出力後の出力開始行
   */
  public Integer writeHeader(Integer rowIdx, Integer columnIdx, List<String> columnNames) {
    if (columnNames == null || columnNames.isEmpty()) return rowIdx;

    Row row = this.sheet.createRow(rowIdx);
    for (String columnName : columnNames) {
      Cell cell = row.createCell(columnIdx);
      writeData(cell, columnName);
      columnIdx++;
    }
    return ++rowIdx;
  }

  /**
   * 行データ出力処理。
   *
   * @param entity
   * @return
   */
  public <T> void writeLine(T entity) {
    if (this.rowCount == null || this.columnCount == null) return;

    Integer columnIdx = this.columnCount;
    this.rowCount = this.writeLine(this.rowCount, columnIdx, entity);
  }

  /**
   * 行データ出力処理。
   *
   * @param rowIdx
   * @param columnIdx
   * @param entity
   * @return
   */
  public <T> Integer writeLine(Integer rowIdx, Integer columnIdx, T entity) {
    if (entity == null) return rowIdx;

    try {
      Row row = CellUtil.getRow(rowIdx, this.sheet);
      List<Method> methods = getEntityGetMethods(entity);
      for (Method method : methods) {
        Cell cell = CellUtil.getCell(row, columnIdx);
        Object value = method.invoke(entity, (Object[]) null);
        writeData(cell, value);
        columnIdx++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ++rowIdx;

  }

  /**
   * データ出力処理。
   *
   * @param rowIdx 出力行
   * @param columnIdx 出力列
   * @param value 出力値
   */
  public void writeData(int rowIdx, int columnIdx, Object value) {
    Row row = CellUtil.getRow(rowIdx, this.sheet);
    Cell cell = CellUtil.getCell(row, columnIdx);
    this.writeData(cell, value);
  }

  /**
   * データ出力処理。
   *
   * @param cell 出力セル
   * @param value 出力値
   */
  public void writeData(Cell cell, Object value) {
    if (value == null) return;

    if (value instanceof String) {
      cell.setCellValue((String) value);
    } else if (value instanceof Integer) {
      cell.setCellValue((Integer) value);
    } else if (value instanceof Long) {
      cell.setCellValue((Long) value);
    } else if (value instanceof Float) {
      cell.setCellValue((Float) value);
    } else if (value instanceof Double) {
      cell.setCellValue((Double) value);
    } else if (value instanceof BigDecimal) {
      cell.setCellValue(((BigDecimal) value).doubleValue());
    } else if (value instanceof Boolean) {
      cell.setCellValue((Boolean) value);
    } else if (value instanceof Date) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
      cell.setCellValue(dateFormat.format((Date) value));
    } else if (value instanceof Timestamp) {
      SimpleDateFormat tsFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
      cell.setCellValue(tsFormat.format((Timestamp) value));
    }
  }

  @Override
  public void close() {
    try {
      if (this.out != null) {
        this.workbook.write(this.out);
      }
      this.out.close();
      if (this.template != null) {
        this.template.close();
      }
      this.out = null;
      this.template = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 出力対象オブジェクトのGETメソッドを取得。
   * （出力対象データが指定されていない場合は、出力対象オブジェクトの全て
   *
   * @param entity 出力対象オブジェクト
   * @return メソッドリスト
   * @throws SecurityException
   * @throws NoSuchMethodException
   */
  private <T> List<Method> getEntityGetMethods(T entity) throws NoSuchMethodException, SecurityException {
    List<Method> methods = new ArrayList<Method>();
    if (this.fieldNames != null) {
      for (String name : this.fieldNames) {
        Method method = getMethod(name, entity);
        methods.add(method);
      }
    } else {
      for (Field field : entity.getClass().getDeclaredFields()) {
        String name = field.getName();
        Method method = getMethod(name, entity);
        methods.add(method);
      }
    }
    return methods;
  }

  /**
   * フィールド名からメソッドを取得。
   *
   * @param name フィールド名
   * @param entity 出力対象オブジェクト
   * @return
   * @throws NoSuchMethodException
   * @throws SecurityException
   */
  private <T> Method getMethod(String name, T entity) throws NoSuchMethodException, SecurityException {
    if (StringUtils.isBlank(name)) return null;
    String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    return entity.getClass().getMethod(methodName);
  }
}
