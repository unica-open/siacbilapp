/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.stampa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter.EntitaConsultabileDataAdapter;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;

/**
 * Componente per la generazione della stampa delle entita consultabili
 * 
 * @author Domenico
 */
public class StampaEntitaConsultabili {

	/** Utility per il log */
	private final LogUtil log = new LogUtil(StampaEntitaConsultabili.class);
	private final EntitaConsultabileDataAdapter dataAdapter;
	private final boolean isXLSX;
	
	private Workbook workbook;
	private Sheet sheet;
	private Map<String, CellStyle> styles;
	
	private List<String> headerTitles;
	private int rows;
	
	/**
	 * Costruttore per la stampa.
	 * <br/>
	 * <strong>ATTENZIONE!</strong>
	 * La classe NON &eacute; thread-safe.
	 * 
	 * @param dataAdapter l'adapter
	 */
	public StampaEntitaConsultabili(EntitaConsultabileDataAdapter dataAdapter) {
		this(dataAdapter, false);
	}
	
	/**
	 * Costruttore per la stampa.
	 * <br/>
	 * <strong>ATTENZIONE!</strong>
	 * La classe NON &eacute; thread-safe.
	 * 
	 * @param dataAdapter l'adapter
	 * @param isXLSX se gestire l'esportazione con l'XSLX
	 */
	public StampaEntitaConsultabili(EntitaConsultabileDataAdapter dataAdapter, boolean isXLSX) {
		this.dataAdapter = dataAdapter;
		this.isXLSX = isXLSX;
	}
	
	/**
	 * Inizializzazione della stampa.
	 */
	public void init() {
		// xls ...for xlsx use XSSFWorkbook.
		// TODO: XLSX non ancora supportato
		if(isXLSX) {
			workbook = new XSSFWorkbook();
		} else {
			workbook = new HSSFWorkbook();
		}
		
		sheet = workbook.createSheet("Entita Consultabili");
		createStyles();
		 
		//turn off gridlines
		sheet.setDisplayGridlines(false);
		sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		//the following three statements are required only for HSSF
		sheet.setAutobreaks(true);
		printSetup.setFitHeight((short)1);
		printSetup.setFitWidth((short)1);
		
		//Creo la riga di intestazione
		rows = 0;
		Row headerRow = sheet.createRow(rows++);
		headerRow.setHeightInPoints(12.75f);
		headerTitles = dataAdapter.getListaIntestazioneColonneExportTabella();
		
		int i = 0;
		for (String title : headerTitles) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(title);
			cell.setCellStyle(styles.get("header"));
			i++;
		}
	}
	
	/**
	 * Aggiunta delle righe.
	 * 
	 * @param entitaConsultabili le righe delle entit&agrave; consultabili
	 */
	public void addRows(List<EntitaConsultabile> entitaConsultabili) {
		if(entitaConsultabili == null) {
			throw new IllegalArgumentException("Le entita' consultabili devono essere popolate");
		}
		
		//Creo le righe con i dati...
		for(EntitaConsultabile ec : entitaConsultabili){
			Map<String, Object> campiColonne = dataAdapter.elaboraDatiColonneExport(ec.getCampi());
			Row row = sheet.createRow(rows++);
			int i = 0; 
			for (String key : headerTitles) {
				Cell cell = row.createCell(i++);
				setCellValueAndStyle(cell, campiColonne.get(key));
			}
		}
	}
	
	/**
	 * Trasfornamzione in bytes.
	 * @param isRisultatoLimitato se il risultato sia limitato
	 * @return i bytes corrispondenti al file excel
	 * @throws IOException in caso di errore di IO nella generazione del file
	 */
	public byte[] toBytes(boolean isRisultatoLimitato) throws IOException {
		final String methodName = "toBytes";
		if(isRisultatoLimitato){
			rows++;
			Row warnRow = sheet.createRow(rows);
			warnRow.setHeightInPoints(30f);
			Cell cell = warnRow.createCell(0);
			//sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 2));
			cell.setCellValue("Attenzione! Risultato limitato a " + (rows - 2) + " elementi.");
			cell.setCellStyle(styles.get("cell_warn"));
		}
		
		//Autosize delle colonne...
		for (int j = 0; j < headerTitles.size(); j++) {
			sheet.autoSizeColumn(j, false);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			workbook.write(baos);
		} catch (IOException ioe) {
			log.debug(methodName, "Errore durante la scrittura del file excel: " + ioe.getMessage());
			throw new IOException("Errore generazione Excel entita consultabili.", ioe);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				log.error(methodName, "Errore durante la chiusura del file: " + e.getMessage(), e);
			}
		}
		
		return baos.toByteArray();
	}
	
	/**
	 * Genera l'excel della stampa
	 * @param entitaConsultabili le entit&agrave; per cui generare l'excel
	 * @param dataAdapter l'adapter 
	 * @param isRisultatoLimitato true se il risultato e' stato troncato al massimo di elementi possibili. (se true mostra un warn).
	 * @return byte array dell'xls
	 * @throws IOException in caso di eccezione nella generazione del report
	 */
	public static byte[] generaExcel(List<EntitaConsultabile> entitaConsultabili, EntitaConsultabileDataAdapter dataAdapter, boolean isRisultatoLimitato) throws IOException {
		return generaExcel(entitaConsultabili, dataAdapter, isRisultatoLimitato, false);
	}
	
	/**
	 * Genera l'excel della stampa
	 * @param entitaConsultabili le entit&agrave; per cui generare l'excel
	 * @param dataAdapter l'adapter 
	 * @param isRisultatoLimitato true se il risultato e' stato troncato al massimo di elementi possibili. (se true mostra un warn).
	 * @param isXLSX true se il formato deve essere XLSX
	 * @return byte array dell'xls
	 * @throws IOException in caso di eccezione nella generazione del report
	 */
	public static byte[] generaExcel(List<EntitaConsultabile> entitaConsultabili, EntitaConsultabileDataAdapter dataAdapter, boolean isRisultatoLimitato, boolean isXLSX) throws IOException {
		
		if(entitaConsultabili == null || entitaConsultabili.isEmpty()){
			throw new IllegalArgumentException("Lista delle entitaConsultabili non puo' essere vuota.");
		}
		
		StampaEntitaConsultabili sec = new StampaEntitaConsultabili(dataAdapter, isXLSX);
		sec.init();
		sec.addRows(entitaConsultabili);
		return sec.toBytes(isRisultatoLimitato);
	}

	/**
	 * Imposta il valore e lo stile nella cella.
	 * 
	 * @param cell la cella da popolare
	 * @param o l'oggetto popolante
	 */
	private void setCellValueAndStyle(Cell cell, Object o) {
		if(o == null){
			cell.setCellValue("ND");
			cell.setCellStyle(styles.get("cell_normal"));
		} else if(o instanceof String){
			cell.setCellValue((String)o);
			cell.setCellStyle(styles.get("cell_normal"));
		} else if(o instanceof Date){
			cell.setCellValue((Date)o);
			cell.setCellStyle(styles.get("cell_normal_date"));
		} else if(o instanceof Calendar){
			cell.setCellValue((Calendar)o);
			cell.setCellStyle(styles.get("cell_normal_date"));
		} else if(o instanceof BigDecimal){
			cell.setCellValue(((BigDecimal)o).doubleValue());
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(styles.get("cell_decimal"));
		} else if(o instanceof Boolean){
			cell.setCellValue(((Boolean)o).booleanValue());
			cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
			cell.setCellStyle(styles.get("cell_normal"));
		} else if(o instanceof Integer){
			cell.setCellValue(((Integer)o).intValue());
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(styles.get("cell_normal"));
		} else {
			throw new IllegalArgumentException("Tipo di valore per la cella non supportato: " + o.getClass());
		}
		
	}

	/**
	 * Creazione degli stili.
	 */
	private void createStyles(){
		styles = new HashMap<String, CellStyle>();
		DataFormat df = workbook.createDataFormat();

		CellStyle style;
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setWrapText(true);
		styles.put("cell_normal", style);
		
		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("###,##0.00"));
		styles.put("cell_decimal", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("dd/mm/YYYY"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short)1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle();
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);
		
		style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		styles.put("cell_warn", style);
	}
	
	/**
	 * Crea uno stile con bordo.
	 * @return lo stile creato
	 */
	private CellStyle createBorderedStyle(){
		CellStyle style = workbook.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}
	
	/**
	 * @return the content type
	 */
	public String getContentType() {
		return isXLSX ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" : "application/vnd.ms-excel";
	}
	
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return isXLSX ? "xlsx" : "xls";
	}
}
