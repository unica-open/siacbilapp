<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />

	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					<form method="post">
						<h3>Risultati di ricerca documenti iva di <s:property value="tipoSubdocumentoIvaTitolo" /></h3>
						<h4><span id="id_num_result" class="num_result"></span></h4>
						
						<table class="table table-hover tab_left dataTable" id="risultatiRicercaDocumentoIva">
							<thead>
								<tr role="row">									
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Documento">Documento</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Intracomunitario" class="span1">intra</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Soggetto">Soggetto</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Registrazione iva">Registrazione iva</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Tipo registrazione iva">Tipo registrazione iva</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Attivita iva">Attivit&agrave; iva</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Registro iva">Registro iva</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Protocollo provv.">Protocollo provv.</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Protocollo def.">Protocollo def.</th>
									<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="" class="tab_Right">&nbsp;</th>									
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>

						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
						<s:hidden id="HIDDEN_tipoDocumento" name="tipoSubdocumentoIva" />

					
						<div class="clear"></div>
						<div class="Border_line"></div>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/documentoIva/risultatiRicerca.js"></script>
</body>
</html>