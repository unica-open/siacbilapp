<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="formConvalidaAllegatoAttoPerProvvisorioCassaStep2" cssClass="form-horizontal" novalidate="novalidate" action="convalidaAllegatoAttoPerProvvisorioCassa_completeStep2" method="post">
					<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
					<s:hidden id="HIDDEN_provvisorioDiEntrata" name="provvisorioDiCassaDiEntrata" />
					<s:hidden id="HIDDEN_annoEsercizio" name="annoEsercizioInt" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Convalida per provvisorio di cassa <s:property value="descrizioneCompletaProvvisorioDiCassa"/></h3>
					<div class="wizard">
						<ul class="steps">
							<li data-target="#step1"><span class="badge badge-success">1</span>Ricerca provvisorio<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Convalida<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<p>
									<b>Causale:</b> <span class="infoTitle"><s:property value="provvisorioDiCassa.causale"/></span>
									<br/>
									<b>Soggetto:</b> <span class="infoTitle"><s:property value="provvisorioDiCassa.denominazioneSoggetto"/></span>
								</p>
								<h4>Importo provvisorio: <s:property value="provvisorioDiCassa.importo"/> - Totale documenti collegati: <s:property value="totaleDocumentiCollegati"/></h4>
								<table class="table table-hover tab_left dataTable" id="tabellaDocumentiProvvisorio">
									<thead>
										<tr>
											<th>Provvedimento</th>
											<th>Soggetto</th>
											<th>Documento-Quota</th>
											<th>Capitolo</th>
											<th>Movimento</th>
											<s:if test="%{!provvisorioDiCassaDiEntrata}">
												<th>Liq.</th>
											</s:if>
											<th>Distinta</th>
											<th>Conto tesoriere</th>
											<th class="tab_Right">Importo entrata</th>
											<th class="tab_Right">Importo spesa</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="8">Totale</th>
											<th class="tab_Right"><s:property value="totaleDocumentiCollegatiEntrata"/></th>
											<th class="tab_Right"><s:property value="totaleDocumentiCollegatiSpesa"/></th>
										</tr>
									</tfoot>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:a cssClass="btn" action="convalidaAllegatoAttoPerProvvisorioCassa_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:if test="convalidaEffettuabile">
							<a class="btn btn-primary pull-right" data-toggle="modal" href="#modaleTipoConvalida">convalida</a>
						</s:if>
					</p>
					<s:include value="/jsp/allegatoAtto/convalida/modaleTipoConvalida.jsp" />
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}allegatoAtto/convalidaProvvisorio_step2.js"></script>
	
</body>
</html>