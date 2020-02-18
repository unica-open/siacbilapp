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
				<form id="formConvalidaAllegatoAttoStep2_unused" class="form-horizontal" novalidate="novalidate" action="#" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden id="HIDDEN_uidElenco" name="elencoDocumentiAllegato.uid" />
					<h3>Valuta atto contabile / allegato <s:property value="descrizioneCompletaAllegatoAtto" /></h3>
					<div class="wizard">
						<ul class="steps">
							<li class="complete" data-target="#step1"><span class="badge badge-success">1</span>Valuta atto<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Convalida elenchi<span class="chevron"></span></li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<p>
									<b>Oggetto:&nbsp;</b><span class="infoTitle"><s:property value="attoAmministrativo.oggetto" /></span>
									<br/>
									<b>Tipo:&nbsp;</b><span class="infoTitle"><s:property value="%{attoAmministrativo.tipoAtto.codice + '-' + attoAmministrativo.tipoAtto.descrizione}" /></span>
									<s:if test="%{attoAmministrativo.strutturaAmmContabile != null}">
										- <b>Struttura:&nbsp;</b><span class="infoStrutt"><s:property value="%{attoAmministrativo.strutturaAmmContabile.codice + '-' + attoAmministrativo.strutturaAmmContabile.descrizione}" /></span>
									</s:if>
								</p>
								<h4>Elenchi collegati : <s:property value="totaleElenchiCollegati" /> - Totale: <span id="totaleElenchi"><s:property value="totaleElenchi" /></span></h4>
								<table class="table table-hover tab_left dataTable" id="tabellaConvalidabili">
									<thead>
										<tr>
											<th>
												<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti nella pagina corrente" id="checkboxSelezionaTutti" />
											</th>
											<th class="span2">Elenco</th>
											<th class="span1">Stato</th>
											<th class="span1"></th>
											<th class="span2">Data trasmissione</th>
											<th>Rich.DURC</th>
											<th>Data fine validita DURC</th>
											<th class="tab_Right">Totale spese</th>
											<th class="tab_Right">Totale entrate</th>
											<th class="tab_Right">Non validato Spese</th>
											<th class="tab_Right">Non validato Entrate</th>
											<th class="tab_Right span1">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="7">Totali</th>
											<th class="tab_Right" id="totaleSpeseConvalidabili"><s:property value="totaleSpeseConvalidabili"/></th>
											<th class="tab_Right" id="totaleEntrateConvalidabili"><s:property value="totaleEntrateConvalidabili"/></th>
											<th class="tab_Right" id="nonValidatoSpeseConvalidabili"><s:property value="nonValidatoSpeseConvalidabili"/></th>
											<th class="tab_Right" id="nonValidatoEntrateConvalidabili"><s:property value="nonValidatoEntrateConvalidabili"/></th>
											<th class="tab_Right">&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<div class="Border_line"></div>
								<p>
									<span class="pull-right">
										<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modaleTipoConvalida">convalida</button>
										<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modaleRifiutaElenco">richiedi modifica elenco</button>
									</span>
								</p>
								<div class="clear"></div>
								<br/>
								<div class="accordion" id="accordionNonConvalidabiliHead">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#accordionNonConvalidabili" data-parent="#accordionNonConvalidabiliHead" data-toggle="collapse" class="accordion-toggle collapsed">
												Risultati non convalidabili<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="accordionNonConvalidabili">
											<div class="accordion-inner">
												<table class="table table-hover tab_left dataTable" id="tabellaNonConvalidabili">
													<thead>
														<tr>
															<th class="span2">Elenco</th>
															<th class="span1">Stato</th>
															<th class="span1"></th>
															<th class="span2">Data trasmissione</th>
															<th>Rich.DURC</th>
															<th>Data fine validita DURC</th>
															<th class="tab_Right">Totale spese</th>
															<th class="tab_Right">Totale entrate</th>
															<th class="tab_Right">Non validato Spese</th>
															<th class="tab_Right">Non validato Entrate</th>
														</tr>
													</thead>
													<tbody>
													</tbody>
													<tfoot>
														<tr>
															<th colspan="6">Totali</th>
															<th class="tab_Right"><s:property value="totaleSpeseNonConvalidabili"/></th>
															<th class="tab_Right"><s:property value="totaleEntrateNonConvalidabili"/></th>
															<th class="tab_Right"><s:property value="nonValidatoSpeseNonConvalidabili"/></th>
															<th class="tab_Right"><s:property value="nonValidatoEntrateNonConvalidabili"/></th>
														</tr>
													</tfoot>
												</table>
											</div>
										</div>
									</div>
								</div>
								
							</fieldset>
						</div>
					</div>

					<p class="margin-medium">
						<s:a cssClass="btn" action="convalidaAllegatoAtto_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<span class="pull-right">
							<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modaleRifiutaAllegato">rifiuta per annullamento atto</button>
						</span>
					</p>
					<s:include value="/jsp/allegatoAtto/convalida/modaleTipoConvalida.jsp" />
					<s:include value="/jsp/allegatoAtto/convalida/allegato/modaleRifiutaElenco.jsp" />
					<s:include value="/jsp/allegatoAtto/convalida/allegato/modaleRifiutaAllegato.jsp" />

				</form>
				<s:form id="formConvalidaAllegatoAttoStep2" cssClass="hide" novalidate="novalidate" action="#" method="post">
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}allegatoAtto/convalidaElenco_step2.js"></script>
	
</body>
</html>