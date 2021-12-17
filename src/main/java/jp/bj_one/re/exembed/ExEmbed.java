package jp.bj_one.re.exembed;

import java.beans.PropertyDescriptor;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import jp.bj_one.re.exembed.ExDirective.Type;
import jp.bj_one.re.exembed.ExEmbedDirective.FoundType;

public class ExEmbed implements Closeable {
	ERROR error;

	public class ERROR {
		ERROR_CODE errorCode = ERROR_CODE.NO_ERROR;
		String addMessage = "";
		Exception exception = null;

		public ERROR(ERROR_CODE errorCode) {
			this.errorCode = errorCode;
		}

		public ERROR(ERROR_CODE errorCode, String addMessage) {
			this(errorCode);
			this.addMessage = addMessage;
		}

		public ERROR(ERROR_CODE errorCode, Exception ex) {
			this(errorCode);
			this.exception = ex;
		}

		@Override
		public String toString() {
			if (addMessage == null || addMessage.isEmpty())
				return errorCode.message;
			else
				return errorCode.message + " (" + addMessage + ")";
		}

		public ERROR_CODE getErrorCode() {
			return this.errorCode;
		}

		public String getAddMessage() {
			return this.addMessage;
		}

		public Exception getException() {
			return this.exception;
		}

		public boolean isError() {
			return errorCode.isError();
		}
	}

	public enum ERROR_CODE {
		NO_ERROR("エラーではない"),
		WORKBOOK_DONT_READ("テンプレートファイルを読み込めない"),
		WORKSHEET_NOT_FOUND("テンプレートワークシートを読み込めない");

		String message;

		ERROR_CODE(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return this.message;
		}

		public boolean isError() {
			return this != NO_ERROR;
		}
	}

	Map<String, Integer> values = new HashMap<String, Integer>();

	private ExEmbed() {
		this.error = new ERROR(ERROR_CODE.NO_ERROR);
	}

	@Override
	public void close() throws IOException {
		if (this.tempPath != null) {
			try {
				Files.delete(this.tempPath);
			} catch (IOException e) {
			}
			this.tempPath = null;
		}
	}

	static final String TEMPFILE_PREFIX = "REEX_";
	static final String TEMPLATE_DIR = "ReTemplate/";

	/** 一時ファイル. */
	private Path tempPath = null;

	/**
	 * テンプレート Excel 文書を読み込み、置換の準備を行う.
	 * 
	 * @param templateFile テンプレートとなる Excel文書。指定方法は二通り。
	 *                     <ul>
	 *                     <li>{@code /} で始まる文字列：ファイルの絶対Path。
	 *                     <li>{@code /} で始まらない文字列：resources/ReTemplate から検索する。
	 *                     </ul>
	 * @return
	 * @throws IOException
	 */
	public static ExEmbed readTemplate(String templateFile) throws IOException {

		final String[] TEMPLATE_EXT = { ".xlsx", ".xlsm" };

		if (templateFile == null || templateFile.isEmpty())
			throw new FileNotFoundException();

		ExEmbed exEmbed = new ExEmbed();

		String ext = "";
		if (templateFile.charAt(0) == '/') {
			int i = templateFile.lastIndexOf('.');
			if (i >= 0)
				ext = templateFile.substring(i);
			Path templatePath = Paths.get(templateFile);
			if (!Files.exists(templatePath))
				throw new FileNotFoundException(templateFile);
			exEmbed.tempPath = Files.createTempFile(TEMPFILE_PREFIX, ext);
			Files.copy(templatePath, exEmbed.tempPath, StandardCopyOption.REPLACE_EXISTING);
		} else {
			templateFile = TEMPLATE_DIR + templateFile;
			InputStream templateStream = null;
			Exception exp = null;
			for (String tx : TEMPLATE_EXT) {
				try {
				  if( templateFile.indexOf(".") < 0 ) {
	          templateStream = ExEmbed.class.getClassLoader().getResourceAsStream(templateFile + tx);
				  }else {
	          templateStream = ExEmbed.class.getClassLoader().getResourceAsStream(templateFile);
				  }
				} catch (Exception e) {
					exp = e;
				}
				if (templateStream != null) {
					ext = tx;
					break;
				}
			}
			if (templateStream == null) {
				exp.printStackTrace();
				throw new FileNotFoundException(templateFile);
			}

			exEmbed.tempPath = Files.createTempFile(TEMPFILE_PREFIX, ext);
			Files.copy(templateStream, exEmbed.tempPath, StandardCopyOption.REPLACE_EXISTING);
		}

		return exEmbed;
	}

	public void doEmbed(ExEmbedCall values, Path writePath) throws Exception, IOException, EncryptedDocumentException {
		if (tempPath == null)
			throw new FileNotFoundException();
		if (!Files.exists(tempPath))
			throw new FileNotFoundException(tempPath.toString());

		try (Workbook workbook = WorkbookFactory.create(tempPath.toFile())) {
			if (workbook.getNumberOfSheets() < 1) {
				this.error = new ERROR(ERROR_CODE.WORKSHEET_NOT_FOUND);
				return;
			}

			// 1 シート目の置換項目毎の数を数える
			Sheet sheet = workbook.getSheetAt(0);
			Map<ExDirective, Integer> embedCount = new HashMap<ExDirective, Integer>();
			for (Row row : sheet) {
				for (Cell cell : row) {
					if (cell.getCellType() == CellType.STRING) {
						embedString(cell.getStringCellValue(), values, embedCount);
					}
				}
			}
			
			// Report の対応する配列の要素数からページ数を求める
			int numOfPage = 1;
			for (ExDirective directive : embedCount.keySet()) {
				if (directive.type == Type.NO_INDEX_ARRAY_TYPE) {
				  //int setting = ((Report)values).getInfo().getPageLines();
				  int v = embedCount.get(directive);
					//int v = setting == 0 ? embedCount.get(directive): setting;
					int p = (countDestination(directive.directive, values) + v - 1) / v;
					if (p > numOfPage)
						numOfPage = p;
				}
			}
			
			// 1 シート目の複製
			if (numOfPage > 1) {
				workbook.setSheetName(0, "1");
				// シートの印刷設定を保持
				PrintSetup ps = workbook.getSheetAt(0).getPrintSetup();
				for (int i = 1; i < numOfPage; i++) {
					workbook.cloneSheet(0);
					if (workbook.getNumberOfSheets() > i + 1)
						workbook.setSheetOrder(workbook.getSheetName(workbook.getNumberOfSheets() - 1), i);
					workbook.setSheetName(i, String.valueOf(i + 1));
					// コピーしたシートに印刷設定を適用
					PrintSetup cps = workbook.getSheetAt(i).getPrintSetup();
					cps.setLandscape(ps.getLandscape());
					cps.setPaperSize(ps.getPaperSize());
				}
			}
			
			// 置換動作
			for (int p = 0; p < numOfPage; p++) {
				sheet = workbook.getSheetAt(p);
				for (Row row : sheet) {
					for (Cell cell : row) {
						if (cell.getCellType() == CellType.STRING) {
							Object obj = embedString(cell.getStringCellValue(), values, null);
							if (obj instanceof String)
								cell.setCellValue((String) obj);
							if (obj instanceof Boolean)
								cell.setCellValue((Boolean) obj);
							if (obj instanceof Double)
								cell.setCellValue((Double) obj);
							if (obj instanceof java.util.Date)
								cell.setCellValue((java.util.Date) obj);
						}
					}
				}
			}

			if (!values.exEmbedPostProcess(workbook))
				throw new Exception();

			try (OutputStream writeStream = new FileOutputStream(writePath.toString())) {
				workbook.write(writeStream);
			}
		}
	}
	
	public class EmbedItem {
		String name;
		int row;
		int col;
		String array;
	}
	
	public List<ExEmbed.EmbedItem> getInfo(Sheet sheet) {
		List<ExEmbed.EmbedItem> embedList = new ArrayList<ExEmbed.EmbedItem>();
		
		return embedList;
	}
	
	protected Object embedString(String source, Object values, Map<ExDirective, Integer> embedCount) {
		ExEmbedDirective f = ExEmbedDirective.findDirective(source);
		if (f.isError())
			return null;

		String resultStr = "";
		Object result = null;

		do {
			if (f.type == FoundType.NOT_DIRECTIVE || f.type == FoundType.IS_COMMENT) {
				resultStr += source.substring(0, f.start) + f.directive;
			} else if (embedCount == null) {
				Object obj = getDestination(f, values);
				if (obj instanceof String) {
					resultStr += source.substring(0, f.start) + (String) obj;
				} else if (obj instanceof Boolean) {
					result =  obj;
				} else if (obj instanceof Double) {
					result =  obj;
				} else if (obj instanceof java.util.Date) {
					result =  obj;
				} else
					resultStr += source.substring(0, f.end);
			} else if (f.exDirective != null) {
				if (embedCount.containsKey(f.exDirective))
					embedCount.put(f.exDirective, embedCount.get(f.exDirective) + 1);
				else
					embedCount.put(f.exDirective, 1);
			}
			source = source.substring(f.end);

			f = ExEmbedDirective.findDirective(source);
			if (f.isError()) {
				resultStr += source;
				source = "";
			}
		} while (!source.isEmpty());

		return embedCount != null ? embedCount : result != null ? result : resultStr;
	}

	protected Object getDestination(ExEmbedDirective directive, Object bean) {
		Object obj;
		switch (directive.type) {
		case IS_BASIC_TYPE:
			obj = getDestinationObject(directive.directive, bean);
			break;
		case IS_ARRAY_TYPE:
		case IS_NO_INDEX_ARRAY_TYPE:
			int index = directive.index;
			if (index < 0) {
				if (values.containsKey(directive.directive))
					index = values.get(directive.directive) + 1;
				else
					index = 0;
			}
			obj = getDestination(directive.directive, bean, index);
			values.put(directive.directive, index);
			break;
		default:
			obj = null;
			break;
		}
		
		// 型の単純化
		if (obj instanceof String)
			return obj;
		if (obj instanceof Character)
			return ((Character) obj).toString();
		if (obj instanceof Boolean)
			return obj;
		if (obj instanceof Byte)
			return Double.valueOf((Byte) obj);
		if (obj instanceof Short)
			return Double.valueOf((Short) obj);
		if (obj instanceof Integer)
			return Double.valueOf((Integer) obj);
		if (obj instanceof Long)
			return Double.valueOf((Long) obj);
		if (obj instanceof Float)
			return Double.valueOf((Float) obj);
		if (obj instanceof Double)
			return obj;
		if (obj instanceof BigDecimal)
			return ((BigDecimal) obj).doubleValue();
		if (obj instanceof java.util.Date)
			return obj;
		if (obj instanceof Calendar)
			return ((Calendar) obj).getTime();
		if (obj instanceof LocalDateTime) {
			ZonedDateTime zdt = ((LocalDateTime) obj).atZone(ZoneId.systemDefault());
			return java.util.Date.from(zdt.toInstant());			
		}
		
		return "";
	}

	/**
	 * 配列、List の要素数取得.
	 * @param keyword
	 * @param bean
	 * @return 配列、List がなかった場合は 0、有った場合は要素数
	 */
	protected int countDestination(String keyword, Object bean) {
		Object obj = getDestinationObject(keyword, bean);
		if (obj != null && obj instanceof Object[]) {
			return ((Object[]) obj).length;
		} else if (obj != null && obj instanceof List) {
			return ((List<?>) obj).size();
		} else
			return 0;
	}

	/**
	 * 配列、List の値取得.
	 * @param keyword
	 * @param bean
	 * @param index
	 * @return 配列、List がなかった場合は null、index を超えていた場合は "" を返す
	 */
	protected Object getDestination(String keyword, Object bean, int index) {
		Object obj = getDestinationObject(keyword, bean);
		if (obj != null && obj instanceof Object[]) {
			if (index >= 0 && index < ((Object[]) obj).length) {
				return ((Object[]) obj)[index];
			} else {
				return "";
			}
		} else if (obj != null && obj instanceof List) {
			if (index >= 0 && index < ((List<?>) obj).size()) {
				return ((List<?>) obj).get(index);
			} else {
				return "";
			}
		} else
			return null;
	}

	/**
	 * Bean からの値取得
	 * @param keyword
	 * @param bean
	 * @return 値がなかった場合は null
	 */
	protected Object getDestinationObject(String keyword, Object bean) {
		try {
			PropertyDescriptor pd;
			pd = new PropertyDescriptor(keyword, bean.getClass());
			Method getter = pd.getReadMethod();
			return getter.invoke(bean, (Object[]) null);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isError() {
		return error.isError();
	}

	public ERROR getError() {
		return this.error;
	}

	public interface ExEmbedCall {
		boolean exEmbedPostProcess(Workbook workbook);
	}

	/*
	 * ページ送り制御メモ書き Report.ReportInfo.pageLines が 1 以上の場合はページ制御を行う。
	 * 
	 */
}
