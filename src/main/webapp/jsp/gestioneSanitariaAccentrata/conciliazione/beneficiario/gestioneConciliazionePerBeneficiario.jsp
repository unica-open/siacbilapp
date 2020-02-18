<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formConciliazioni">
					<s:hidden name="faseBilancioChiuso" id="hidden_faseBilancioChiuso" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Gestione della conciliazione automatica per beneficiario</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<fieldset class="form-horizontal" id="fieldsetRicercaBeneficiario">
								<h4 class="step-pane" id="containerDatiBeneficiarioRicerca"></h4>
								<div class="control-group margin-medium">
									<label class="control-label" for="codiceSoggettoSoggettoRicerca">Soggetto *</label>
									<div class="controls">
										<s:textfield id="codiceSoggettoSoggettoRicerca" name="soggettoRicerca.codiceSoggetto" />
										<button type="button" class="btn btn-primary pull-right" id="buttonCompilazioneGuidataSoggettoRicerca">compilazione guidata</button>
									</div>
								</div>
								<div class="control-group margin-medium">
									<label class="control-label">Capitolo</label>
									<div class="controls">
										<span class="al">
											<label class="radio inline">
												<input type="radio" value="S" name="tipoCapitoloRicerca" <s:if test='%{"S".equals(tipoCapitoloRicerca)}'>checked</s:if>>Spese
											</label>
										</span>
										<span class="al">
											<label class="radio inline">
												<input type="radio" value="E" name="tipoCapitoloRicerca" <s:if test='%{"E".equals(tipoCapitoloRicerca)}'>checked</s:if>>Entrate
											</label>
										</span>
									</div>
								</div>
								<div class="control-group margin-medium">
									<label class="control-label" for="annoCapitoloCapitoloRicerca" >Anno *</label>
									<div class="controls">
										<s:textfield id="annoCapitoloCapitoloRicerca" name="capitoloRicerca.annoCapitolo" cssClass="span2 soloNumeri" />
										<span class="al">
											<label class="radio inline" for="numeroCapitoloCapitoloRicerca">Numero</label>
										</span>
										<s:textfield id="numeroCapitoloCapitoloRicerca" name="capitoloRicerca.numeroCapitolo" cssClass="span2 soloNumeri" />
										<span class="al">
											<label class="radio inline" for="numeroArticoloCapitoloRicerca">Articolo</label>
										</span>
										<s:textfield id="numeroArticoloCapitoloRicerca" name="capitoloRicerca.numeroArticolo" cssClass="span2 soloNumeri" />
										<s:if test="gestioneUEB">
											<span class="al">
												<label class="radio inline" for="numeroUEBCapitoloRicerca">UEB</label>
											</span>
											<s:textfield id="numeroUEBCapitoloRicerca" name="capitoloRicerca.numeroUEB" cssClass="span2 soloNumeri" />
										</s:if><s:else>
											<s:hidden id="numeroUEBCapitoloRicerca" name="capitoloRicerca.numeroUEB" value="1" data-maintain=""/>
										</s:else>
										<button type="button" class="btn btn-primary pull-right" id="buttonRicercaConciliazioniPerBeneficiario">cerca</button>
									</div>
								</div>
							</fieldset>
							<div id="divRisultatiRicerca" class="hide">
								<h4 class="step-pane">Elenco dati di conciliazione per soggetto <span id="filtroRicerca"></span></h4>
								<table class="table table-hover tab_left" id="tabellaRisultatiRicerca">
									<thead>
										<tr>
											<th>Ent/Spe</th>
											<th>Capitolo</th>
											<th>Classe di conciliazione</th>
											<th>Classe conto</th>
											<th>Conto</th>
											<th class="span1"></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<s:if test="%{!faseBilancioChiuso}">
								<p>
									<a id="buttonNuovaConciliazione" class="btn btn-secondary" href="#">Inserisci Conto nella Classe</a>
								</p>
								<div id="divConciliazione_inserimento" class="hide">
									<s:include value="/jsp/gestioneSanitariaAccentrata/conciliazione/beneficiario/collapseInserimentoConciliazioneBeneficiario.jsp" />
								</div>
								<div id="divConciliazione_aggiornamento" class="hide">
									<s:include value="/jsp/gestioneSanitariaAccentrata/conciliazione/beneficiario/collapseAggiornamentoConciliazioneBeneficiario.jsp" />
								</div>
							</s:if>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
				<s:include value="/jsp/include/modaleConfermaEliminazione.jsp" />
				<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}codiceFiscale.js"></script>
	<script type="text/javascript" src="${jspath}soggetto/ricerca.new.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/conciliazione/conciliazionePerBeneficiario.js"></script>
	
</body>
</html>