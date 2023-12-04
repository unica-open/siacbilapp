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
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:hidden name="contiCausale" id="HIDDEN_contiCausale" />
				<s:hidden name="classificatoreGerarchico.uid" id="HIDDEN_uidClassificatoreGerarchico" />
				<s:form cssClass="form-horizontal" action="%{urlSalva}" novalidate="novalidate" id="formGestisciPrimaNotaintegrata">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserimento prima nota integrata</h3>
					<h4><s:property value="intestazioneRichiesta" escapeHtml="false"/></h4>
					
					<div class="accordion" id="accordionMovimento">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionMovimento" href="#collapseMovimento">
									<s:property value="intestazioneMovimentoFinanziario" escapeHtml="true" /><span class="icon">&nbsp;</span>
								</a>
							</div>
							<div id="collapseMovimento" class="accordion-body collapse">
								<div class="accordion-inner">
									<fieldset class="form-horizontal">
										<s:include value="/jsp/contabilitaGenerale/registrazione/consultazione/consultaDati%{consultazioneSubpath}.jsp" />
									</fieldset>
								</div>
							</div>
						</div>
					</div>
					
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal" id="#fieldsetGestisciPNI">
								<h4 class="step-pane">Dati</h4>
								<div class="control-group">
									<label class="control-label">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizionePrimaNota" name="primaNota.descrizione" cssClass="span9" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data Registrazione *</label>
									<div class="controls">
										<s:textfield id="dataRegistrazionePrimaNota" name="primaNota.dataRegistrazione" cssClass="span2 datepicker" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Causale *</label>
									<div class="controls">
										<s:select list="listaCausaleEP" id="uidCausaleEP" name="causaleEP.uid"
												cssClass="span6" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-EP="" disabled="%{aggiornamento}"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Note</label>
									<div class="controls">
										<input id="note" name="note" class="span9" type="text" value="" />
									</div>
								</div>
								
								<s:if test="validazione">
									<div class="control-group">
										<label class="control-label">Classificatori</label>
										<div class="controls">
											<div id="classGSAParent" class="accordion span9 classGSA">
												<div class="accordion-group">
													<div class="accordion-heading">
														<a href="#classGSA" data-toggle="collapse" data-parent="#classGSAParent" class="accordion-toggle collapsed">
															<span id="SPAN_classificatoreGSA">Seleziona il classificatore</span>
														</a>
													</div>
													<div id="classGSA" class="accordion-body collapse">
														<div class="accordion-inner">
															<ul id="classGSATree" class="ztree"></ul>
															<button type="button" class="btn pull-right" data-deseleziona-ztree="classGSATree">Deseleziona</button>
														</div>
													</div>
												</div>
											</div>
										</div>
										<s:hidden id="HIDDEN_classificatoreGSAUid" name="classificatoreGSA.uid" />
									</div>
									<div class="control-group">
										<label class="control-label" for="dataRegistrazionePrimaNota">Data registrazione definitiva *</label>
										<div class="controls">
											<s:textfield id="dataRegistrazionePrimaNota" name="primaNota.dataRegistrazioneLibroGiornale" cssClass="lbTextSmall span2 datepicker" size="10" required="required"/>
										</div>
									</div>
								</s:if>
								
								<h4 class="nostep-pane">Da registrare(D-A): <span id="spanDaRegistrare"><s:property value="daRegistrare" /></span></h4>
								<h4 class="step-pane">Elenco scritture</h4>
								<table class="table table-hover tab_left" id="tabellaScritture">
									<thead>
										<tr>
											<th>Conto</th>
											<th>Descrizione</th>
											<th class="tab_Right">Dare</th>
											<th class="tab_Right">Avere</th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="2">Totale</th>
											<th class="tab_Right" id="totaleDare"></th>
											<th class="tab_Right" id="totaleAvere"></th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<p>
									<button type="button" id="pulsanteInserimentoDati" class="btn btn-secondary">
										inserisci dati in elenco&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserimentoDati"></i>
									</button>
								</p>
								<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/collapseDatiStruttura.jsp" />
							</fieldset>
						</div>
					</div>
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:if test = "%{primaNota == null || primaNota.uid == 0}">
							<s:submit value="salva" cssClass="btn btn-primary pull-right" />
						</s:if>
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleEliminazioneConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleAggiornaConto.jsp" />
				<s:include value="/jsp/gestioneSanitariaAccentrata/primaNotaIntegrata/modaleDettaglioConti.jsp" />
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/classifgsa/ztree.classifgsa.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/primaNotaIntegrata/gestisci.movimento.reg.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/registrazione/consultaRegistrazioneMovFin${consultazioneSubpath}.js"></script>

</body>
</html>