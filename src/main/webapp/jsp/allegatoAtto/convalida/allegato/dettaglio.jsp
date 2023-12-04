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
				<form id="formConvalidaAllegatoAttoStep2_unused" class="form-horizontal" novalidate="novalidate" method="post" onsubmit="return false">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Quote in convalida elenco <s:property value="elencoDocumentiAllegato.anno"/>/<s:property value="elencoDocumentiAllegato.numero"/></h3>
					<fieldset class="form-horizontal">
						<s:if test="%{elencoDocumentiAllegato.dataTrasmissione != null && elencoDocumentiAllegato.annoSysEsterno != null && elencoDocumentiAllegato.numeroSysEsterno != null}">
							<b>Trasmesso il:</b>&nbsp;<s:property value="elencoDocumentiAllegato.dataTrasmissione" />&nbsp;
							<b>da:</b>&nbsp;<s:property value="elencoDocumentiAllegato.annoSysEsterno" />&nbsp;
							<b>n.</b>&nbsp;<s:property value="elencoDocumentiAllegato.numeroSysEsterno" />&nbsp;
							<br/>
						</s:if>
						<b>Provvedimento:</b>&nbsp;<s:property value="attoAmministrativo.anno" />/<s:property value="attoAmministrativo.numero" />&nbsp;-&nbsp;
						<b><s:property value="attoAmministrativo.tipoAtto.descrizione"/></b>
						<s:if test="%{attoAmministrativo.strutturaAmmContabile != null}">
							&nbsp;-&nbsp;<b>struttura:</b>&nbsp;
							<s:property value="attoAmministrativo.strutturaAmmContabile.codice"/>/<s:property value="attoAmministrativo.strutturaAmmContabile.descrizione"/>
						</s:if>
						<s:if test="%{attoAmministrativo.oggetto != null}">
							&nbsp;-&nbsp;
							<b>oggetto:</b>&nbsp;<s:property value="attoAmministrativo.oggetto"/>
						</s:if>
						<p>
							<a data-toggle="modal" href="#modaleTipoConvalida" class="btn btn-primary pull-right">convalida</a>
						</p>
						<h4 class="step-pane">
							Dettaglio elementi da convalidare &nbsp;
							<span class="">
								<a data-original-title="Visualizza dettagli totali" data-toggle="modal" href="#modaleTotaliElenco" class="tooltip-test">
									<i class="icon-info-sign icon-1x">&nbsp;<span class="nascosto">Visualizza dettagli</span></i>
								</a>
							</span>
						</h4>
						<table class="table table-hover tab_left dataTable" id="tabellaSubdocumentiDaConvalidare">
							<thead>
								<tr>
									<th>
										<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti nella pagina corrente" id="pulsanteSelezionaTuttiDaConvalidare" data-table-selector="#tabellaSubdocumentiDaConvalidare" />
									</th>
									<th>&nbsp;</th>
									<th>Soggetto</th>
									<th>Documento-Quota</th>
									<th>Capitolo</th>
									<th>Movimento</th>
									<th>Liq.</th>
									<th>Distinta</th>
									<th>Conto tesoriere</th>
									<th>&nbsp;</th>
									<th>IVA</th>
									<th>Annotazioni</th>
									<th class="tab_Right">Importo entrata</th>
									<th class="tab_Right">Importo spesa</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="listaSubdocumentiConvalidabili" var="sub">
									<tr>
										<td>
											<input type="checkbox" name="listaUid" value='<s:property value="%{#sub.uid}"/>' <s:if test="#sub.inibisciSelezione"> disabled </s:if> />
										</td>
										<td><s:property value="#sub.domStringDatiDurc" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringSoggetto" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringDocumento" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringCapitolo" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringMovimento" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringLiquidazione" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringDistinta" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringContoTesoreria" escapeHtml="false"/></td>
										<%-- ***************************************JIRA-2911********************************************** --%>
										<td><s:property value="#sub.domStringQuoteACopertura" escapeHtml="false"/></td>
										<%-- ***************************************JIRA-2911********************************************** --%>
										<td><s:property value="#sub.domStringImportoSplitReverse" escapeHtml="false"/></td>
										<td><s:property value="#sub.domStringAnnotazioni" escapeHtml="false" /></td>
										<td class="tab_Right importoEntrata"><s:property value="#sub.importoEntrata"/></td>
										<td class="tab_Right importoSpesa"><s:property value="#sub.importoSpesa"/></td>
									</tr>
								</s:iterator>
							</tbody>
							<tfoot>
								<tr class="borderBottomLight">
									<th colspan="11">Totale</th>
									<th class="tab_Right"><s:property value="totaleEntrataDaConvalidareSubdocumenti"/></th>
									<th class="tab_Right"><s:property value="totaleSpesaDaConvalidareSubdocumenti"/></th>
								</tr>
								<tr class="borderBottomLight">
									<th colspan="11" data-numero-elementi-selezionati>In convalida</th>
									<th class="tab_Right" data-totale-in-convalida="entrata">0,00</th>
									<th class="tab_Right" data-totale-in-convalida="spesa">0,00</th>
								</tr>
							</tfoot>
						</table>
						<div class="Border_line"></div>
						<p>
							<a data-toggle="modal" href="#modaleTipoConvalida" class="btn btn-primary pull-right">convalida</a>
						</p>
						<div class="clear"></div>
						<br/>
						<div class="accordion" id="accordionNonConvalidabili">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a href="#collapseNonConvalidabili" data-parent="#accordionNonConvalidabili" data-toggle="collapse" class="accordion-toggle collapsed">
										Elementi non convalidabili<span class="icon">&nbsp;</span>
									</a>
								</div>
								<div class="accordion-body collapse" id="collapseNonConvalidabili">
									<div class="accordion-inner">
										<table class="table table-hover tab_left dataTable" id="tabellaSubdocumentiNonConvalidabili">
											<thead>
												<tr>
													<th>&nbsp;</th>
													<th>Soggetto</th>
													<th>Documento-Quota</th>
													<th>Convalida</th>
													<th>&nbsp;</th>
													<th>Capitolo</th>
													<th>Movimento</th>
													<th>Liq.</th>
													<th>Distinta</th>
													<th>Conto tesoriere</th>
													<th>IVA</th>
									   				<th>Annotazioni</th>
													<th class="tab_Right">Importo entrata</th>
													<th class="tab_Right">Importo spesa</th>
												</tr>
											</thead>
											<tbody>
												<s:iterator value="listaSubdocumentiNonConvalidabili" var="sub">
													<tr>
														<td><s:property value="#sub.domStringDatiDurc" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringSoggetto" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringDocumento" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringConvalida" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringQuoteACopertura" escapeHtml="false" /></td>
														<td><s:property value="#sub.domStringCapitolo" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringMovimento" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringLiquidazione" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringDistinta" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringContoTesoreria" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringImportoSplitReverse" escapeHtml="false"/></td>
														<td><s:property value="#sub.domStringAnnotazioni" escapeHtml="false" /></td>
														<td class="tab_Right"><s:property value="#sub.importoEntrata"/></td>
														<td class="tab_Right"><s:property value="#sub.importoSpesa"/></td>
													</tr>
												</s:iterator>
											</tbody>
											<tfoot>
												<tr class="borderBottomLight">
													<th colspan="9">Totale</th>
													<th class="tab_Right"><s:property value="totaleEntrataNonConvalidabiliSubdocumenti"/></th>
													<th class="tab_Right"><s:property value="totaleSpesaNonConvalidabiliSubdocumenti"/></th>
												</tr>
											</tfoot>
										</table>
									</div>
								</div>
							</div>
						</div>
					</fieldset>
					<div class="Border_line"></div>

					<p class="margin-medium">
						<s:a cssClass="btn" action="convalidaAllegatoAtto_backToStep2" id="pulsanteRedirezioneIndietro">indietro</s:a>
					</p>
					<s:include value="/jsp/allegatoAtto/convalida/modaleTipoConvalida.jsp" />
					<s:include value="/jsp/allegatoAtto/convalida/allegato/modaleTotaliElenco.jsp" />

				</form>
				<s:form id="formConvalidaAllegatoAttoStep2" cssClass="hide" novalidate="novalidate" action="convalidaAllegatoAtto_convalidaSubdocumento" method="post">
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/convalidaElenco_dettaglio.js"></script>
	
</body>
</html>