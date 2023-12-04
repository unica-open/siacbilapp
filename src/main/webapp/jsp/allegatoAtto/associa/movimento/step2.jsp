<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
				<s:form id="formAssociaMovimentoAllegatoAttoStep2" cssClass="form-horizontal" novalidate="novalidate" action="associaMovimentoAllegatoAtto_completeStep2" method="post">
					<s:hidden name="soggetto.uid" id="idSoggetto" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3><s:property value="denominazioneAllegatoAtto"/></h3>
					<h4>Associa movimenti</h4>
					<div class="wizard">
						<ul class="steps">
							<li data-target="#step1"><span class="badge badge-success">1</span>Ricerca soggetto<span class="chevron"></span></li>
							<li class="active" data-target="#step2"><span class="badge">2</span>Associa movimenti<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step2" class="step-pane active">
							<fieldset class="form-horizontal">
								<h4>Soggetto: <s:property value="descrizioneCompletaSoggetto" /></h4>
								<p>
									<button type="button" class="btn collapsed" id="pulsanteAggiungiMovimento">aggiungi movimento</button>
								</p>
								<div class="collapse" id="collapseAggiungiMovimento">
									<div class="accordion_info">
										<div class="step-pane active">
											<h4>Movimento<span id="movimentoDati"></span></h4>
											<fieldset class="form-horizontal" data-overlay>
												<div class="control-group">
													<label class="control-label">Tipo</label>
													<div class="controls">
														<label class="radio inline">
															<input type="radio" value="Accertamento" name="tipoMovimento">Entrata
														</label>
														<span class="alLeft">
															<label class="radio inline">
																<input type="radio" value="Impegno" name="tipoMovimento">Spesa
															</label>
														</span>
													</div>
												</div>
												<div class="control-group">
													<label class="control-label" for="annoMovimento">Anno *</label>
													<div class="controls">
														<input type="text" required value="" class="span1 soloNumeri" id="annoMovimento">
														<span class="al">
															<label class="radio inline" for="numeroMovimentoGestione">Numero *</label>
														</span>
														<input type="text" required value="" class="span2 soloNumeri" id="numeroMovimentoGestione">
														<span class="al">
															<label class="radio inline" for="numeroSubmovimentoGestione">Sub</label>
														</span>
														<input type="text" value="" class="span2 soloNumeri" id="numeroSubmovimentoGestione">

														<span class="radio guidata">
															<button type="button" class="btn btn-primary disabled" id="pulsanteCompilazioneGuidataMovimentoGestione">compilazione guidata</button>
														</span>
													</div>
												</div>
												<div class="well">
													<div class="control-group datiVisibili hide" data-cig-cup>
														<label class="control-label" for="cigMovimentoGestione">CIG</label>
														<div class="controls">
															<input type="text" class="span3 forzaMaiuscole" data-allowed-chars="[A-Za-z0-9]" id="cigMovimentoGestione" maxlength="10">
															<span class="al" data-assenza-cig>
																<label for="siopeAssenzaMotivazione" class="radio inline">Motivo di assenza CIG</label>
															</span>
															<s:select list="listaSiopeAssenzaMotivazione" id="siopeAssenzaMotivazione" cssClass="span4"
																headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="false" data-assenza-cig="" />
														</div>
													</div>
													<div class="control-group datiVisibili hide" data-cig-cup>
														<label class="control-label" for="cupMovimentoGestione">CUP</label>
														<div class="controls">
															<input type="text" class="span3 forzaMaiuscole" data-allowed-chars="[A-Za-z0-9]" id="cupMovimentoGestione" maxlength="15">
														</div>
													</div>
													<div class="control-group">
															<label class="control-label">Provvisorio di Cassa</label>
															<div class="controls">
																<span class="alRight">
																	<label for="annoProvvisorioCassaSubdocumento" class="radio inline">Anno</label>
																</span>
																<s:textfield id="annoProvvisorioCassaSubdocumento" name="subdocumento.provvisorioCassa.anno" cssClass="lbTextSmall span2 soloNumeri" placeholder="numero" maxlength="4" />
																<span class="alRight">
																	<label for="numeroProvvisorioCassaSubdocumento" class="radio inline">Numero</label>
																</span>
																<s:textfield id="numeroProvvisorioCassaSubdocumento" name="subdocumento.provvisorioCassa.numero" cssClass="lbTextSmall span2 soloNumeri" placeholder="numero" maxlength="8" />
																&nbsp;
																<span id="causaleProvvisorioCassaSubdocumento"></span>
																<span class="radio guidata">
																	<button type="button" class="btn btn-primary" id="pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento">compilazione guidata</button>
																</span>
															</div>
													</div>
													<div class="control-group">
														<label class="control-label" for="importoInAtto">Importo in atto *</label>
														<div class="controls">
															<input type="text" required class="span2 soloNumeri decimale" id="importoInAtto">
															<div class="radio inline collapse_alert hide" id="divDisponibilitaMovimentoGestione">
																<span class="icon-chevron-right icon-red alRight"></span>Disponibile <span id="disponibilitaMovimentoGestione"></span>
															</div>
														</div>
													</div>
												</div>
												<p>
													<button type="button" class="btn btn-secondary" id="buttonAnnullaAggiungiMovimento">annulla</button>
													<span class=" pull-right">
														<button type="button" class="btn btn-primary disabled" id="buttonConfermaAggiungiMovimento">
															conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_buttonConfermaAggiungiMovimento"></i>
														</button>
													</span>
												</p>
											</fieldset>
										</div>
									</div>
								</div>
								<br/>
								<p>
									<s:submit cssClass="btn btn-primary pull-right" value="salva in allegato" />
								</p>
								<h4>
									Movimenti da collegare -
									Totale spesa: <span class="NumInfo" id="spanTotaleImpegni"><s:property value="totaleSpesa"/></span> -
									Totale entrata: <span class="NumInfo" id="spanTotaleAccertamenti"><s:property value="totaleEntrata"/></span>
								</h4>
								<h5>Movimenti di spesa</h5>
								<table class="table table-hover tab_left" id="tabellaImpegni">
									<thead>
										<tr>
											<th class="span1">Movimento</th>
											<th class="span1">CIG</th>
											<th class="span2">CUP</th>
											<th class="span1"><abbr title="Motivo di assenza CIG">Assenza</abbr></th>
											<th class="span1">Capitolo</th>
											<th class="span1">Provvedimento</th>
											<th class="span1">Provvisorio Di Cassa</th>
											<th class="tab_Right span1">Importo in atto</th>
											<th class="span1">&nbsp;</th>
											<th class="span1">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								<h5>Movimenti di entrata</h5>
								<table class="table table-hover tab_left" id="tabellaAccertamenti">
									<thead>
										<tr>
											<th class="span2">Movimento</th>
											<th class="span2">Capitolo</th>
											<th class="span2">Provvedimento</th>
											<th class="span2">Provvisorio Di Cassa</th>
											<th class="tab_Right span3">Importo in atto</th>
											<th class="span1">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</fieldset>
						</div>
					</div>

					<p class="margin-medium">
						<s:a cssClass="btn" action="associaMovimentoAllegatoAtto_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<button type="button" class="btn btn-secondary reset">annulla</button>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="salva in allegato" />
						</span>
					</p>
					<s:include value="/jsp/movimentoGestione/modaleAccertamento.jsp" />
					<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
					<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />
					<s:include value="/jsp/provvisorioCassa/modaleRicercaProvvisorioCassa.jsp" />
					<s:include value="/jsp/allegatoAtto/associa/movimento/include/modaleModificaMovimentoAssociato.jsp" />
					
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvisorioDiCassa/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaAccertamentoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/associaMovimento_step2.js"></script>
	
</body>
</html>