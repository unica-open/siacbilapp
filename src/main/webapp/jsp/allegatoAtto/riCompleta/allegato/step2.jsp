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
				<s:form id="formRiCompletaAllegatoAttoStep2" cssClass="form-horizontal" novalidate="novalidate" action="#" method="post">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden id="HIDDEN_uidElenco" name="elencoDocumentiAllegato.uid" />
					<s:hidden id="HIDDEN_flagAttoAutomatico" name="attoAutomatico" />
					
					<h3>Riporta a Completato Atto Contabile / allegato <s:property value="descrizioneRiCompletaAllegatoAtto" /></h3>
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
								<h4>Elenchi collegati : <s:property value="totaleElenchiCollegati" /> - Totale: <s:property value="totaleElenchi" /></h4>
								<table class="table table-hover tab_left dataTable" id="tabellaRiCompletabili">
									<thead>
										<tr>
											<th>
												<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti" id="checkboxSelezionaTutti" />
											</th>
											<th class="span2">Elenco</th>
											<th class="span1">Stato</th>
											<th class="span1"></th>
											<th class="span2">Data trasmissione</th>
											<th class="tab_Right">Totale spese</th>
											<th class="tab_Right">Totale entrate</th>
											<th class="tab_Right">Validato Spese</th>
											<th class="tab_Right">Validato Entrate</th>
											<th class="tab_Right span1">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="5">Totali</th>
											<th class="tab_Right" id="totaleSpeseRiCompletabili"><s:property value="totaleSpeseRiCompletabili"/></th>
											<th class="tab_Right" id="totaleEntrateRiCompletabili"><s:property value="totaleEntrateRiCompletabili"/></th>
											<th class="tab_Right" id="validatoSpeseRiCompletabili"><s:property value="validatoSpeseRiCompletabili"/></th>
											<th class="tab_Right" id="validatoEntrateRiCompletabili"><s:property value="validatoEntrateRiCompletabili"/></th>
											<th class="tab_Right">&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<div class="Border_line"></div>
								<p>
									<span class="pull-right">
									<s:if test="%{disabledButtons}">
										<button type="button" class="btn btn-primary" id="pulsantePortaAcompletatoElenco" disabled="disabled" >Porta a Completato 
														&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsantePortaAcompletatoElenco"></i>
										</button>
									</s:if>
									<s:else>
										<button type="button" class="btn btn-primary" id="pulsantePortaAcompletatoElenco" >Porta a Completato 
														&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsantePortaAcompletatoElenco"></i>
										</button>
									</s:else>
									</span>
								</p>
								<div class="clear"></div>
								<br/>
								<div class="accordion" id="accordionNonElaborabiliHead">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#accordionNonElaborabili" data-parent="#accordionNonElaborabiliHead" data-toggle="collapse" class="accordion-toggle collapsed">
												Risultati non Elaborabili<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="accordionNonElaborabili">
											<div class="accordion-inner">
												<table class="table table-hover tab_left dataTable" id="tabellaNonElaborabili">
													<thead>
														<tr>
															<th class="span2">Elenco</th>
															<th class="span1">Stato</th>
															<th class="span1"></th>
															<th class="span2">Data trasmissione</th>
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
															<th colspan="4">Totali</th>
															<th class="tab_Right"><s:property value="totaleSpeseNonElaborabili"/></th>
															<th class="tab_Right"><s:property value="totaleEntrateNonElaborabili"/></th>
															<th class="tab_Right"><s:property value="nonValidatoSpeseNonElaborabili"/></th>
															<th class="tab_Right"><s:property value="nonValidatoEntrateNonElaborabili"/></th>
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
						<s:a cssClass="btn" action="riCompletaAllegatoAtto_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
					</p>
					<s:include value="/jsp/allegatoAtto/convalida/modaleTipoConvalida.jsp" />
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}allegatoAtto/riCompletaElenco_step2.js"></script>
	
</body>
</html>