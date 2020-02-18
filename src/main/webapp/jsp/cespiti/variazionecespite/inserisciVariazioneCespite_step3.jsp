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
			<div class="span12 ">
				<div class="contentPage">
					<div id="formInserisciVariazioneCespite_step3" class="form-horizontal">
						<h3 id="titolo"><s:property value="formTitle"/></h3>
						<s:hidden id="HIDDEN_intestazioneInserimento" name="intestazioneInserimentoNuovaVariazione" />
						<s:hidden id="HIDDEN_intestazioneAggiornamento" name="intestazioneAggiornamentoVariazione" />
						<s:hidden id="HIDDEN_flagTipoVariazioneIncremento" name="flagTipoVariazioneIncremento" />
						<s:hidden id="HIDDEN_baseUrl" name="baseUrl" />
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="wizard">
							<ul class="steps">
								<li data-target="#step1">
									<span class="badge badge-success">1</span>cerca cespite <span class="chevron"></span>
								</li>
								<li data-target="#step2">
									<span class="badge badge-success">2</span>seleziona cespite<span class="chevron"></span>
								</li>
								<li class="active" data-target="#step3">
									<span class="badge">3</span><s:property value="wizardStep3" /><span class="chevron"></span>
								</li>
							</ul>
						</div>
						<div class="step-content">
							<div class="step-pane active" id="step3">
								<h4>Dati cespite</h4>
								<ul class="htmlelt">
									<li>
										<dfn>Codice</dfn>
										<dl><s:property value="cespite.codice" /></dl>
									</li>
									<li>
										<dfn>Descrizione</dfn>
										<dl><s:property value="cespite.descrizione" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Tipo bene</dfn>
										<dl><s:property value="codiceDescrizioneTipoBene" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Valore iniziale</dfn>
										<dl><s:property value="cespite.valoreIniziale" /></dl>
									</li>
									<li>
										<dfn>Valore attuale</dfn>
										<dl id="valoreAttualeCespite"><s:property value="cespite.valoreAttuale" /></dl>
									</li>
								</ul>
								<div class="clear"></div>
								<br/>
								<div id="accordionPianoammortamento" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#collapsePianoAmmortamento" data-parent="#accordionPianoammortamento" data-toggle="collapse" class="accordion-toggle collapsed">
												Piano di ammortamento<span class="icon"></span>
											</a>
										</div>
										<div class="accordion-body collapse" id="collapsePianoAmmortamento">
											<div class="accordion-inner container">
												<h4>Totale importi ammortamento accumulati: 0</h4>
												<%-- TODO --%>
											</div>
										</div>
									</div>
								</div>
								<h4><s:property value="tableTitle" /></h4>
								<table id="tabellaVariazioneCespite" class="table table-striped table-bordered table-hover dataTable">
									<thead>
										<tr>
											<th>Anno</th>
											<th>Data inserimento</th>
											<th>Tipo variazione</th>
											<th>Descrizione</th>
											<th>Importo</th>
											<th>Stato</th>
											<th></th>
										</tr>
									</thead>
									<tbody></tbody>
								</table>
								<br/>
								<button type="button" class="btn btn-secondary" id="nuovaVariazione"><s:property value="buttonNewTitle"/></button>
								<div id="divDatiVariazione" class="accordion-body hide">
									<h4 id="intestazioneDatiVariazione"></h4>
									<fieldset class="form-horizontal" id="fieldsetDatiVariazione">
										<div class="control-group">
											<input type="hidden" id="uidVariazioneCespite" name="variazioneCespite.uid" />
											<label for="annoVariazioneCespite" class="control-label">Anno *</label>
											<div class="controls">
												<input id="annoVariazioneCespite" class="span2" readonly value="<s:property value="annoEsercizioInt" />" required name="variazioneCespite.annoVariazione" type="text" />
												<span class="alRight">
													<label for="dataInserimentoVariazioneCespite" class="radio inline">Data inserimento *</label>
												</span>
												<input id="dataInserimentoVariazioneCespite" name="variazioneCespite.dataVariazione" class="datepicker lbTextSmall span2" required maxlength="10" type="text" data-date />
											</div>
										</div>
										<div class="control-group">
											<label for="descrizioneVariazione" class="control-label">Descrizione *</label>
											<div class="controls">
												<textarea id="descrizioneVariazione" name="variazioneCespite.descrizione" rows="2" cols="20" class="span6" maxlength="500" required></textarea>
											</div>
										</div>
										<div class="control-group">
											<label for="tipoVariazioneCespite" class="control-label">Tipo Variazione</label>
											<div class="controls">
												<select id="tipoVariazioneCespite" name="variazioneCespite.tipoVariazione" class="span6" required disabled>
													<option selected><s:property value="testoSelectTipoVariazione" /></option>
												</select>
											</div>
										</div>
										<div class="control-group">
											<label for="importoVariazioneCespite" class="control-label">Importo *</label>
											<div class="controls">
												<input id="importoVariazioneCespite" class="span2 soloNumeri decimale text-right" required name="variazioneCespite.importo" type="text" data-importo />
												<span class="alRight">
													<label for="nuovoValoreAttualeVariazioneCespite" class="radio inline">Nuovo valore attuale</label>
												</span>
												<input id="nuovoValoreAttualeVariazioneCespite" disabled class="lbTextSmall span2 text-right" required value="" type="text" />
											</div>
										</div>
										<button class="btn btn-primary pull-right" id="pulsanteSalvaVariazione">salva</button>
									</fieldset>
								</div>
							</div>
						</div>
						<p class="margin-medium">
							<s:a cssClass="btn" action="%{baseUrl}_%{backUrl}" id="pulsanteRedirezioneIndietro">indietro</s:a>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/cespiti/variazionecespite/modaleEliminazione.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cespiti/variazionecespite/inserisciVariazioneCespite_step3.js"></script>
</body>
</html>