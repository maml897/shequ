package com.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoiExcelReader
{
	private static final Logger logger = LoggerFactory.getLogger(PoiExcelReader.class);

	private Workbook workBook;

	private FormulaEvaluator formulaEvaluator;

	private Sheet sheet;

	private Row row;

	private Cell cell;

	private int rows;

	private int columns;

	private int tmp = 0;

	/**
	 * 构造函数，Excel文件对象变量作为参数
	 * @param file
	 *            文件对象变量
	 * @throws Exception
	 */
	public PoiExcelReader(File file) throws Exception
	{
		try (InputStream is = new FileInputStream(file))
		{
			workBook = WorkbookFactory.create(is);
			formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();
			selSheet(0);// 是否需要默认选中第一个工作区？
		}
		catch (Exception e)
		{
			logger.error(e.toString());
		}
	}

	public PoiExcelReader(InputStream is) throws Exception
	{
		workBook = WorkbookFactory.create(is);
		formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();
	}

	/**
	 * 构造函数，Excel文件的绝对路径
	 * @param strFilePath
	 *            绝对路径
	 * @throws Exception
	 */
	public PoiExcelReader(String strFilePath) throws Exception
	{
		this(new File(strFilePath));
	}

	/**
	 * 以index变量(以‘0’开始)，选中某个sheet。
	 * @param sheetIndex
	 */
	public PoiExcelReader selSheet(int sheetIndex)
	{
		sheet = workBook.getSheetAt(sheetIndex);
		computeRowsCols();
		return this;
	}

	/**
	 * 以name变量，选中某个sheet。
	 * @param strSheetName
	 */
	public void selSheet(String strSheetName)
	{
		sheet = workBook.getSheet(strSheetName);
		computeRowsCols();
	}

	/**
	 * 获取单元格的值
	 * @param y
	 *            列index，以‘0’开始
	 * @param x
	 *            行index，以‘0’开始
	 * @return
	 */
	public String getOneCell(int y, int x)
	{
		String strOneCell = "";
		if (y < columns && x < rows)
		{
			row = sheet.getRow(x);
			if (row != null)
			{
				cell = row.getCell(y);
				if (cell != null)
				{
					strOneCell = getValue(cell);
				}
			}
		}
		return strOneCell;
	}

	/**
	 * 获取单元格的值
	 * @param y
	 *            列index，以‘0’开始
	 * @param x
	 *            行index，以‘0’开始
	 * @param exclueCenterWhite
	 *            true：过滤中间的全角半角空格 ，false 不过滤
	 * @return
	 */
	public String getOneCell(int y, int x, boolean exclueCenterWhite)
	{
		String value = getOneCell(y, x);
		if (exclueCenterWhite)
		{
			return value.replaceAll("[\u3000\\s]+", "");
		}
		return value;
	}

	/**
	 * 获取实际有数据的行数
	 * @return
	 */
	public int getCurRows()
	{
		return rows;
	}

	/**
	 * 获取实际有数据的列数
	 * @return
	 */
	public int getCurColumns()
	{
		return columns;
	}

	/**
	 * 获取单元格的值，不管什么类型，都将最后的值以string形式返回
	 * @param cell
	 *            单元格对象
	 * @return
	 */
	private String getValue(Cell cell)
	{
		String retString;
		CellType key = cell.getCellType();
		switch (key)
		{
		case STRING:
			retString = cell.getStringCellValue();
			break;
		case NUMERIC:
			retString = String.valueOf(cell.getNumericCellValue());
			break;
		case BOOLEAN:
			retString = String.valueOf(cell.getBooleanCellValue());
			break;

		case FORMULA:
			CellValue evaluateValue = formulaEvaluator.evaluate(cell);
			switch (evaluateValue.getCellType())
			{
			case STRING:
				retString = evaluateValue.getStringValue();
				break;
			case NUMERIC:
				double d = new BigDecimal(evaluateValue.getNumberValue()).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				if (d == (long) d)
				{
					retString = Long.toString((long) d);
				}
				else
				{
					retString = Double.toString(d);
				}
				break;
			case BOOLEAN:
				retString = String.valueOf(evaluateValue.getBooleanValue());
				break;
			case BLANK:
				retString = evaluateValue.getStringValue();
				break;
			case ERROR:
				retString = String.valueOf(cell.getErrorCellValue());
				break;
			default:
				retString = evaluateValue.getStringValue();
				break;
			}
			break;
		case BLANK:
			retString = cell.getStringCellValue();
			break;
		case ERROR:
			retString = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			retString = cell.getStringCellValue();
			break;
		}
		return retString.trim();
	}

	/**
	 * 计算真实的行列数
	 */
	private void computeRowsCols()
	{
		if (sheet == null)
			return;

		rows = sheet.getLastRowNum() + 1;
		for (int i = 0; i < 10 || i < rows; i++)
		{
			row = sheet.getRow(i);
			if (row != null)
			{
				tmp = sheet.getRow(i).getLastCellNum();
				if (tmp > columns)
					columns = tmp;
			}
		}
		rows = getRealExcelRows();
	}

	/**
	 * 获取实际有数据的行数
	 * @return
	 */
	private int getRealExcelRows()
	{
		for (int x = rows - 1; x >= 0; x--)// 行
		{
			for (int y = 0; y < columns; y++)// 列
			{
				row = sheet.getRow(x);
				if (row != null)
				{
					cell = row.getCell(y);
					if (cell != null)
					{
						String content = getValue(cell).replaceAll("[\u3000\\s]+", "");
						if (!content.trim().equals(""))
						{
							return x + 1;
						}
					}
				}
			}
		}
		return 0;
	}


	public List<List<String>> getDatas()
	{
		List<List<String>> datas = new ArrayList<>();
		for (int i = 0; i < this.getCurRows(); i++)
		{
			List<String> row = new ArrayList<>();
			for (int j = 0; j < this.getCurColumns(); j++)
			{
				row.add(this.getOneCell(j, i).trim());
			}
			datas.add(row);
		}
		return datas;
	}
}
