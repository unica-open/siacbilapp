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
			<div class="span12 ">
				<s:form id="formInserisciDocumentoPerProvvisori" novalidate="novalidate" action="%{nomeAzioneSuccessiva}" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Elenco provvisori cassa</h3>
					<h4><span id="id_num_result" class="num_result"></span></h4>
					<h4>Totale provvisori selezionati: <span class="NumInfo" id="spanTotaleProvvisoriSelezionati"><s:property value="totaleProvvisoriSelezionati"/></span> 
					</h4>
					<s:hidden id="totaleProvvisoriSelezionati" name="totaleProvvisoriSelezionati" />
					
					<table class="table table-hover tab_left" id="risultatiRicercaProvvisorioCassa">
						<thead>
							<tr role="row">
								<th class="span1">
									<input type="checkbox" class="tooltip-test check-all" data-original-title="Seleziona tutti" data-referred-table="#risultatiRicercaProvvisorioCassa" />
								</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Tipo">Tipo</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="N. provvisorio"><abbr title="Numero">N.</abbr> provvisorio</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Data emissione">Data emissione</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Data annullamento">Data annullamento</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Descrizione versante">Descrizione versante</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Descrizione causale">Descrizione causale</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Importo" class="tab_Right">Importo</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Da regolarizzare" class="tab_Right">Da Regolarizzare</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Da emettere" class="tab_Right">Da emettere</th>
								<th scope="col" role="columnheader" tabindex="0" aria-controls="risultatiricerca" rowspan="1" colspan="1" aria-label="Azioni" class="tab_Right"></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>

					<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
					
					<div class="clear"></div>
					<div class="Border_line"></div>
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
						<s:submit cssClass="btn btn-primary" value="inserisci documento" />
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}provvisorioDiCassa/risultatiRicercaSinteticaProvvisorioCassa.js"></script>
</body>
</html>