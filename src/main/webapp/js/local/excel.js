/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(g) {
	'use strict';
	var cellStyles = {
		header: {
			border: {
				bottom: { style: 'thick' },
				left: { style: 'thick' },
				right: { style: 'thick' },
				top: { style: 'thick' }
			},
			font: {
				color: {
					argb: 'FFFFFFFF'
				}
			},
			fill: {
				type: 'pattern',
				pattern: 'solid',
				fgColor: { argb: 'FF0000FF' }
			}
		},
		cell: {
			border: {
				bottom: { style: 'thin' },
				left: { style: 'thin' },
				right: { style: 'thin' },
				top: { style: 'thin' }
			}
		},
		cellRight: {
			border: {
				bottom: { style: 'thin' },
				left: { style: 'thin' },
				right: { style: 'thick' },
				top: { style: 'thin' }
			}
		},
		cellLast: {
			border: {
				bottom: { style: 'thick' },
				left: { style: 'thin' },
				right: { style: 'thin' },
				top: { style: 'thin' }
			}
		},
		cellLastRight: {
			border: {
				bottom: { style: 'thick' },
				left: { style: 'thin' },
				right: { style: 'thick' },
				top: { style: 'thin' }
			}
		}
	};
	
	g.estraiExcel = estraiExcel;
	
	function estraiExcel(data, title, sheets) {
		var workbook = new ExcelJS.Workbook();
		var i, j, k, sheetData, sheet, row, col, cell, datum, style, cellStyle;
		for(i = 0; i < sheets.length; i++) {
			sheetData = sheets[i];
			sheet = workbook.addWorksheet(sheetData.title);
			// Headers
			row = sheet.getRow(1);
			for(j = 0; j < sheetData.columns.length; j++) {
				col = sheetData.columns[j];
				cell = row.getCell(j + 1);
				cell.style = cellStyles.header;
				cell.value = col.header;
			}
			// Data
			for(k = 2; k < data.length + 2; k++) {
				datum = data[k - 2];
				style = k === data.length + 1 ? 'cellLast' : 'cell';
				row = sheet.getRow(k);
				for(j = 0; j < sheetData.columns.length; j++) {
					cellStyle = j === sheetData.columns.length - 1 ? (style + 'Right') : style;
					col = sheetData.columns[j];
					cell = row.getCell(j + 1);
					cell.style = cellStyles[cellStyle];
					cell.value = col.extract(datum);
				}
			}
			
			// Autofit
			sheet.columns.forEach(function (column) {
				var maxLength = 0;
				column["eachCell"](function (cell) {
					var columnLength = cell.value ? cell.value.toString().length : 10;
					if (columnLength > maxLength ) {
						maxLength = columnLength;
					}
				});
				column.width = maxLength < 10 ? 10 : maxLength;
			});
		}
		workbook.xlsx.writeBuffer({ base64: true })
			.then(function(xls64) {
				saveAs(
					new Blob([xls64], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }),
					title + '.xlsx'
				);
			});
	}
}(this);